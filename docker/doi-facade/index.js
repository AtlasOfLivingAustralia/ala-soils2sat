const util = require('util')
const express = require('express')
const axios = require('axios')
const pino = require('pino')
const expressPino = require('express-pino-logger')
const Sentry = require('@sentry/node')
const dayjs = require('dayjs')

const lib = require('./src/lib.js')

const port = process.env.PORT || 3000
const doiServerUrl = (() => {
  const defaultValue = 'https://doi.tern.uq.edu.au/test/'
  return process.env.DOI_URL || defaultValue
})()
const doiPublisher = (() => {
  const defaultValue = 'Atlas of Living Australia'
  return process.env.DOI_PUBLISHER || defaultValue
})()
const doiPubYear = (() => {
  // yes, this does seem weird that it's hardcoded, but I'm just copying what
  // was in the DOI minting service that I replaced.
  const defaultValue = '2013'
  return process.env.DOI_PUB_YEAR || defaultValue
})()
const doiUser = requiredEnvVar('DOI_USER')
const doiAppId = requiredEnvVar('DOI_APP_ID')
const sentryDsn = process.env.SENTRY_DSN

const app = express()
const logger = pino({ level: process.env.LOG_LEVEL || 'info' })
if (sentryDsn) {
  logger.debug('Initialising Sentry')
  Sentry.init({ dsn: sentryDsn })
  // must be first middleware
  app.use(Sentry.Handlers.requestHandler())
} else {
  logger.warn('No Sentry DSN, refusing to initialise Sentry')
}
app.use(express.json())
const expressLogger = expressPino({ logger })
app.use(expressLogger)

app.post('/mint', async (req, res) => {
  try {
    const creatorName = req.body.creator
    const title = req.body.title
    const landingPageUrl = req.body.landingPageUrl
    if (!creatorName || !title || !landingPageUrl) {
      return res.status(400).send({
        status: 400,
        message:
          'Error: you must supply all values: ' +
          '{creator:"",title:"",landingPageUrl:""}',
      })
    }
    const { status, respBody } = await doDoiMint({
      creatorName,
      title,
      landingPageUrl,
    })
    return (
      res
        .status(status)
        // yeah, it's wrong. But that's what the DOI sends back.
        .set('Content-Type', 'text/html; charset=UTF-8')
        .send(respBody)
    )
  } catch (err) {
    Sentry.captureException(err)
    logger.error(err)
    res.status(500).send({ status: 500, msg: 'Internal Server Error' })
  }
})

async function doDoiMint({ creatorName, title, landingPageUrl }) {
  logger.debug(
    `Minting DOI for params: creator=${creatorName}, title=${title}, ` +
      `landingPageUrl=${landingPageUrl}`,
  )
  const body = createReqBody({
    creatorName,
    title,
    publisher: doiPublisher,
    publicationYear: doiPubYear,
    createdDateStr: dayjs().format('YYYY-MM-DD'),
  })
  const url =
    doiServerUrl +
    `?r=api/create` +
    `&user_id=${doiUser}` +
    `&app_id=${doiAppId}` +
    `&url=${landingPageUrl}`
  logger.debug('Using DOI server URL: ' + url)
  logger.debug('Sending body to DOI server: ' + body)
  const resp = await axios.post(url, body, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  })
  const respBody = resp.data
  const parsedRespBody = await lib.parseXml(respBody)
  if (isFailure(parsedRespBody)) {
    sentryWarn(
      `DOI minting failure
      reqBody: ${body}
      respBody: ${respBody}
    `,
      'DoiMintingError',
    )
    return {
      status: 400,
      respBody,
    }
  }
  logger.debug('Response body from DOI server: ' + respBody)
  return {
    status: resp.status,
    respBody: parsedRespBody.response.doi[0],
  }
}

function createReqBody({
  creatorName,
  title,
  publisher,
  publicationYear,
  createdDateStr,
}) {
  const xmlDocument = `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <resource
      xmlns="http://datacite.org/schema/kernel-4"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://datacite.org/schema/kernel-4 http://schema.datacite.org/meta/kernel-4/metadata.xsd">
        <identifier identifierType="DOI"/>
        <creators>
          <creator>
            <creatorName>${creatorName}</creatorName>
          </creator>
        </creators>
        <titles>
          <title>${title}</title>
        </titles>
        <publisher>${publisher}</publisher>
        <publicationYear>${publicationYear}</publicationYear>
        <resourceType resourceTypeGeneral="Collection">Dataset</resourceType>
        <dates>
          <date dateType="Created">${createdDateStr}</date>
        </dates>
    </resource>`
  const urlEncodedBody = encodeURIComponent(xmlDocument)
  return `xml=${urlEncodedBody}`
}

if (sentryDsn) {
  // must be defined after all controllers
  app.use(Sentry.Handlers.errorHandler())
}

app.listen(port, () => {
  logger.info(`S2S DOI facade server running!
    Listening on port:    ${port}
    DOI server URL:       ${doiServerUrl}
    DOI app ID:           ${doiAppId}
    DOI user:             ${doiUser}
    Sentry DSN:           ${sentryDsn}`)
})

function sentryWarn(msg, errorName = 'Error') {
  logger.warn(msg)
  Sentry.withScope(scope => {
    scope.setLevel('warning')
    const err = new Error(msg)
    err.name = errorName
    Sentry.captureException(err)
  })
}

function requiredEnvVar(varName) {
  const result = process.env[varName]
  if (!result) {
    throw new Error(`Required env var ${varName} not found`)
  }
  return result
}

function isFailure(respBodyObj) {
  const type = (((respBodyObj || {}).response || {}).$ || {}).type
  return type === 'failure'
}
