/*
 * $Id: generateIANALanguageTags.groovy 60 2010-04-21 10:55:50Z t-nakaguchi $
 *
 * langridライセンス条項
 */
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Date
import groovy.io.PlatformLineWriter
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * IANA language-tagsの定義を行うクラスのソースコードを生成するスクリプト。
 */
// tabを取り除き、"["の位置を調整したものを使用
def sourceFile = new File("iana_language-tags.txt")
def destinationFile = new File("../src/jp/go/nict/langrid/language/IANALanguageTags.java")
def templateFile = new File("IANALanguageTags.java.template")

def utf8Reader(file){
  return new InputStreamReader(
    new FileInputStream(file)
    , 'UTF-8'
    )
}


class IANATag{
  def name
  def firstParam
  def secondParam
  def additionalParams
  def description
  def deprecated
}

def createTag(tag, description, deprecated)
{
  def params = tag.tokenize("-")
  def first = params[0]
  def name = params.join("_")
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

  new IANATag(
    name: name
    , firstParam: first
    , secondParam: second
    , additionalParams: params
    , description: description
    , deprecated: deprecated.replace("Deprecated ", "")
    )
}


def tags = []

def parsing = false
def tag
def description = ""
def deprecated = ""

sourceFile.eachLine{
  line ->
  if((line.trim().length() == 0) || (line[0] == "#")){
    if(parsing){
      tags << createTag(
        tag
        , description.toString().trim()
        , deprecated.toString().trim()
        )
      tag = ""
      description = ""
      deprecated = ""
    }
    parsing = false
    return
  }

  def len = line.length()
  def desc = line.substring(13, Math.min(len, 53)).trim();
  def dep = ""

  if(!parsing){
    tag = line.substring(0, 13).trim();

    def closeBraceIndex = line.indexOf("]")
    if(closeBraceIndex != -1){
      dep = line.substring(closeBraceIndex + 1).trim()
    }

    parsing = true
  } else{
    if(line.length() > 64){
      dep = line.substring(65).trim()
    }
  }

  description <<= " "
  description <<= desc
  deprecated <<= " "
  deprecated <<= dep
}
if(parsing){
  tags << createTag(
    tag
    , description.toString().trim()
    , deprecated.toString().trim()
    )
}


def engine = new SimpleTemplateEngine()
def bindings = [tags: tags, date: new Date()]

destinationFile.withWriter('UTF-8'){
  w ->
  new PlatformLineWriter(w).write(engine.createTemplate(utf8Reader(templateFile)).make(bindings))
}
