/*
 * $Id: generateLanguageDesc.groovy 60 2010-04-21 10:55:50Z t-nakaguchi $
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
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Date
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * ISO639_1言語コードの説明を設定するpropertiesファイルを生成するスクリプト。
 */
def sourceFiles = [new File("ISO639_1.ja.txt"), new File("ISO639_1.en.txt")]
def sourceFiles2 = [new File("ISO-639-2_utf-8.txt")]
def sourceFiles3 = [new File("langrid_language-tags.txt")]
def sourceFiles4 = [new File("iana_language-tags.txt")]
def destFiles = [new File("Language.properties"), new File("Language_en.properties")]

def utf8Reader(file){
  return new InputStreamReader(
    new FileInputStream(file)
    , 'UTF-8'
    )
}

def windows31JReader(file){
  return new InputStreamReader(
    new FileInputStream(file)
    , 'Windows-31J'
    )
}

def tags639_2 = [:]
  utf8Reader(sourceFiles2[0]).eachLine{
    line ->
    line = line.trim()
    if((line.length() == 0) || (line[0] == "#") || (line[1] == "#")) return
    value = line.split('\\|')
    if(value[0].length() > 0){
      tags639_2[value[0]] = value[3]
    }
    if(value[1].length() > 0){
      tags639_2[value[1]] = value[3]
    }
    if(value[2].length() > 0){
      tags639_2[value[2]] = value[3]
    }
  }

for(i in 0..1){
  def dest = destFiles[i]
  dest.write("")
  utf8Reader(sourceFiles[i]).eachLine{
    line ->
	line = line.trim()
    if((line.length() == 0) || (line[0] == "#")) return
    value = line.split('\\t')
	dest.append(value[0].split('\\(')[0].trim(), 'UTF-8')
	dest.append("=")
	dest.append(value[1], 'UTF-8')
	dest.append("\n")
	tags639_2.remove(value[0])
  }
  for(e in tags639_2){
    dest.append(e.key, 'UTF-8')
    dest.append('=')
    dest.append(e.value, 'UTF-8')
    dest.append('\n')
  }
  windows31JReader(sourceFiles3[0]).eachLine{
    line ->
    line = line.trim()
    if((line.length() == 0) || (line[0] == "#") || (line[1] == "#")) return
    value = line.split('\\t')
    if(value[0].length() > 0){
      dest.append(value[0], 'UTF-8')
      dest.append('=')
      dest.append(value[i + 1], 'UTF-8')
      dest.append('\n')
    }
  }
  def parsing = false
  utf8Reader(sourceFiles4[0]).eachLine{
    line ->
    def trimmed = line.trim()
    if(trimmed.length() == 0){
      if(parsing){
        dest.append('\n')
        parsing = false
      }
      return
    }
    if(trimmed[0] == "#") return
    if(parsing){
      line = line.substring(0, Math.min(line.length(), 65)).trim()
      if(line.length() > 0){
        dest.append(' ');
        dest.append(line, 'UTF-8')
      }
    } else{
   	  def values = line.substring(0, Math.min(line.length(), 65)).split("\\s", 2)
      dest.append(values[0], 'UTF-8')
      dest.append('=')
      dest.append(values[1].split("\\[")[0].trim(), 'UTF-8')
      parsing = true
    }
  }
  dest.append('\n')
}
