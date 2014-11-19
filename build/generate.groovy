import java.io.File
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

def className = args[0]
def templateFile = new File(args[1])
def outputFile = new File(args[2])

elements = className.split("\\.")
def simpleClassName = elements[elements.length - 1]

new SimpleTemplateEngine().createTemplate(templateFile).make(
	[className: className, simpleClassName: simpleClassName]
	).writeTo(outputFile.newWriter())
