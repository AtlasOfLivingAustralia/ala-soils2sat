const parseString = require('xml2js').parseString

function parseXml(xmlStr) {
  return new Promise((resolve, reject) => {
    parseString(xmlStr, function (err, result) {
      if (err) {
        return reject(err)
      }
      return resolve(result)
    })
  })
}

module.exports = {
  parseXml,
}
