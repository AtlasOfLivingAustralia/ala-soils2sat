const objectUnderTest = require('../src/lib.js')

describe('parseXml', () => {
  it('should parse a valid XML string for successful minting', async () => {
    const xml =
      '<?xml version="1.0"?>\n<response type="success">\n            <responsecode>MT001</responsecode>\n            <message>DOI 10.80374/mj1b-f121 was successfully minted.</message>\n            <doi>10.80374/mj1b-f121</doi>\n            <url>http://tern.org.au</url>\n            <verbosemessage>OK</verbosemessage>\n            </response>\n'
    const result = await objectUnderTest.parseXml(xml)
    expect(result).toStrictEqual({
      response: {
        $: { type: 'success' },
        doi: ['10.80374/mj1b-f121'],
        message: ['DOI 10.80374/mj1b-f121 was successfully minted.'],
        responsecode: ['MT001'],
        url: ['http://tern.org.au'],
        verbosemessage: ['OK'],
      },
    })
  })

  it('should parse a valid XML string for failed minting', async () => {
    const xml =
      '<?xml version="1.0"?>\n<response type="failure">\n<responsecode>errUrlNotResolvable</responsecode>\n<message>Failed to mint DOI</message>\n<verbosemessage>[TERN-DOI] Error: The landing page URL is not registered in our system, or has not yet been approved (https://example.org).</verbosemessage>\n</response>\n'
    const result = await objectUnderTest.parseXml(xml)
    expect(result).toStrictEqual({
      response: {
        $: { type: 'failure' },
        message: ['Failed to mint DOI'],
        responsecode: ['errUrlNotResolvable'],
        verbosemessage: [
          '[TERN-DOI] Error: The landing page URL is not registered in our system, or has not yet been approved (https://example.org).',
        ],
      },
    })
  })
})
