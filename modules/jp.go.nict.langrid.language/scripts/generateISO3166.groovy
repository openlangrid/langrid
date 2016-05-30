/*
 * $Id: generateISO3166.groovy 60 2010-04-21 10:55:50Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Date
import groovy.io.PlatformLineWriter
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * ISO639: 2/3 letter codes用の列挙クラスのソースコードを生成するスクリプト。
 * $Author: t-nakaguchi $
 * $Revision: 60 $
 */
def sourceFile = new File("ISO3166.txt")
def destinationFile = new File("../src/jp/go/nict/langrid/language/ISO3166.java")
def templateFile = new File("ISO3166.java.template")

def utf8Reader(file){
  return new InputStreamReader(
    new FileInputStream(file)
    , 'UTF-8'
    )
}

class CountryTag{
  def descriptions
  def code
}

def tags = []

sourceFile.eachLine{
  line ->
  line = line.trim()
  if(line.length() == 0) return
  if(line[0] == "#") return

  def tokens = line.tokenize(";")

  def descriptions = tokens[0].tokenize(",")
  def code = tokens[1]

  tag = new CountryTag(
    descriptions: "\"" + descriptions.join("\", \"") + "\""
    , code: code
    )

  tags << tag
}

def engine = new SimpleTemplateEngine()
def bindings = [tags: tags, date: new Date()]

destinationFile.withWriter('UTF-8'){
 w ->
 new PlatformLineWriter(w).write(engine.createTemplate(utf8Reader(templateFile)).make(bindings))
}
