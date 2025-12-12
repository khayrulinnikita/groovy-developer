package io.github.khayrulinnikita

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

static void main(String[] args) {
    def url = new URL('https://raw.githubusercontent.com/Groovy-Developer/groovy-homeworks/refs/heads/main/hw-5/test.json')
    def jsonSlurper = new JsonSlurper()
    def response = jsonSlurper.parse(url)

    def HtmlWriter = new StringWriter()
    def HtmlBuilder = new MarkupBuilder(HtmlWriter)

    HtmlBuilder.div {
        div(id: 'employee') {
            p(response.name)
            br()
            p(response.age)
            br()
            p(response.secretIdentity)
            br()
            ul(id: 'powers') {
                response.powers.each { power ->
                    li(power)
                }
            }
        }
    }
    println("HTML:")
    println(HtmlWriter.toString())

    def XmlWriter = new StringWriter()
    def XmlBuilder = new MarkupBuilder(XmlWriter)

    XmlBuilder.employee {
        name(response.name)
        age(response.age)
        secretIdentity(response.secretIdentity)
        powers {
            response.powers.each { powerValue ->
                power(powerValue)
            }
        }
    }
    println("XML:")
    println(XmlWriter.toString())

}