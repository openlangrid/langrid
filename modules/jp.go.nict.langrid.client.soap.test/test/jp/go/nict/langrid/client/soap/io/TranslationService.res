<soapenv:Envelope
 xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header>
    <ns1:calltree
     soapenv:actor="http://schemas.xmlsoap.org/soap/actor/next"
     soapenv:mustUnderstand="0"
     xsi:type="soapenc:string"
     xmlns:ns1="http://langrid.nict.go.jp/process/calltree"
     xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">[]</ns1:calltree>
  </soapenv:Header>
  <soapenv:Body>
    <ns2:translateResponse
     soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"
     xmlns:ns2="servicegrid:servicetype:nict.nlp:Translation">
      <translateReturn xsi:type="soapenc:string" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">こんにちは</translateReturn>
    </ns2:translateResponse>
  </soapenv:Body>
</soapenv:Envelope>
