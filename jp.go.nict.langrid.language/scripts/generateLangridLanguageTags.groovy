/*
 * $Id: generateLangridLanguageTags.groovy 60 2010-04-21 10:55:50Z t-nakaguchi $
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
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * Langridで使用する一般的な言語表現の定義を行うクラスのソースコードを生成する
 * スクリプト。
 */
// "20051118_藤代_言語グリッド上での言語表現.doc"からコピペしたものを使用
def sourceFile = new File("langrid_language-tags.txt")
def destinationFile = new File("../src/jp/go/nict/langrid/language/LangridLanguageTags.java")
def templateFile = new File("LangridLanguageTags.java.template")

def utf8Reader(file){
  return new InputStreamReader(
    new FileInputStream(file)
    , 'UTF-8'
    )
}

class LangridTag{
  def name
  def firstParam
  def secondParam
  def additionalParams
  def description
}

def createTag(tag, description)
{
     def params = tag.tokenize("-")
     def name = params.join("_")

     def first = params[0]
     params = params.reverse()
     params.pop()
     params = params.reverse()

     def second = ""
     if(params.size() > 0){
       second = params[0]
       params = params.reverse()
       params.pop()
       params = params.reverse()
     }

     def additional = ""
     if(params.size() > 0){
       additional = ", \"" + params.join("\", \"") + "\""
     }

     new LangridTag(
       name: name
       , firstParam: first
       , secondParam: second
       , additionalParams: additional
       , description: description
       )
}

def tags = []

sourceFile.eachLine{
  line ->
  if((line.trim().length() == 0) || (line[0] == "#")){
    return;
  }

  def elements = line.split("\t")
  def tag = elements[0]
  def description = elements[1]
  description <<= ", "
  description <<= elements[2]

  tags << createTag(tag, description.toString().trim())
}


def engine = new SimpleTemplateEngine()
def bindings = [tags: tags, date: new Date()]

destinationFile.withWriter("UTF-8"){
  w ->
  new PlatformLineWriter(w).write(engine.createTemplate(utf8Reader(templateFile)).make(bindings))
}
