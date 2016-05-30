/*
 * $Id: generateISO639.groovy 60 2010-04-21 10:55:50Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File
import java.util.Date
import groovy.io.PlatformLineWriter
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * ISO639: 2/3 letter codes用の列挙クラスのソースコードを生成するスクリプト。
 */
def sourceFile = new File("ISO-639-2_utf-8.txt")
def sourceFile2 = new File("ISO639_1.en.txt")
def destinationFile1 = new File("../src/jp/go/nict/langrid/language/ISO639_1.java")
def destinationFile2 = new File("../src/jp/go/nict/langrid/language/ISO639_2.java")
def destinationFile3 = new File("../src/jp/go/nict/langrid/language/ISO639_1LanguageTags.java")
def templateFile1 = new File("ISO639_1.java.template")
def templateFile2 = new File("ISO639_2.java.template")
def templateFile3 = new File("ISO639_1LanguageTags.java.template")

def utf8Reader(file){
  return new InputStreamReader(
    new FileInputStream(file)
    , 'UTF-8'
    )
}

class LanguageTag{
  def descriptions
  def threeLetterCodeBibliographic
  def threeLetterCodeTerminology
  def twoLetterCode
}

def langs = [:]

sourceFile2.eachLine{
  line ->
  line = line.trim()
  if(line.length() == 0) return
  if(line[0] == "#") return
  values = line.split("\\t")
  langs[values[0].toUpperCase()] = values[1]
}

def tags = []

sourceFile.eachLine{
  line ->
  line = line.trim()
  if(line.length() == 0) return
  if(line[0] == "#") return
  def values = line.split("\\|")
  def tlc1 = values[0].toUpperCase()
  def tlc2 = values[1].toUpperCase()
  def twoLetterCode = values[2].toUpperCase()
  def description = values[3]
  langs.remove(twoLetterCode)
  if(tlc1.equals("QAA-QTZ")) return
  if(tlc1.length() == 0) tlc1 = null
  if(tlc2.length() == 0) tlc2 = null
  if(twoLetterCode.length() == 0) twoLetterCode = null
  if(description.length() == 0) description = null

  def descriptions = description.tokenize(";").collect(
    {t -> t.trim().replace("\"", "\\\"")}
    )

  tags << new LanguageTag(
    descriptions: "\"" + descriptions.join("\", \"") + "\""
    , threeLetterCodeBibliographic: tlc1
    , threeLetterCodeTerminology: tlc2
    , twoLetterCode: twoLetterCode
    )
}

for(e in langs){
  tags << new LanguageTag(
    descriptions: "\"" + e.value + "\""
    , threeLetterCodeBibliographic: null
    , threeLetterCodeTerminology: null
    , twoLetterCode: e.key
    )
}

def engine = new SimpleTemplateEngine()
def bindings = [tags: tags, date: new Date()]

destinationFile1.withWriter('UTF-8'){
 w -> new PlatformLineWriter(w).write(engine.createTemplate(utf8Reader(templateFile1)).make(bindings))
}

destinationFile2.withWriter('UTF-8'){
 w -> new PlatformLineWriter(w).write(engine.createTemplate(utf8Reader(templateFile2)).make(bindings))
}

destinationFile3.withWriter('UTF-8'){
 w -> new PlatformLineWriter(w).write(engine.createTemplate(utf8Reader(templateFile3)).make(bindings))
}
