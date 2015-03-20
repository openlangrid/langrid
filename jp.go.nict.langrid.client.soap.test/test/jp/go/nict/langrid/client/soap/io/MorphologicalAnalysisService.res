<?xml version="1.0" encoding="utf-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Body>
    <ns1:analyzeResponse
      soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"
      xmlns:ns1="servicegrid:servicetype:nict.nlp:MorphologicalAnalysis">
      <analyzeReturn
        soapenc:arrayType="ns2:Morpheme[1]"
        xsi:type="soapenc:Array"
        xmlns:ns2="http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/"
        xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">
        <analyzeReturn href="#id0"/>
      </analyzeReturn>
    </ns1:analyzeResponse>
    <multiRef
      id="id0" soapenc:root="0" soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"
      xsi:type="ns3:Morpheme" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
      xmlns:ns3="http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/">
      <lemma xsi:type="soapenc:string">hello world</lemma>
      <partOfSpeech xsi:type="soapenc:string">other</partOfSpeech>
      <word xsi:type="soapenc:string">hello world</word>
    </multiRef>
  </soapenv:Body>
</soapenv:Envelope>