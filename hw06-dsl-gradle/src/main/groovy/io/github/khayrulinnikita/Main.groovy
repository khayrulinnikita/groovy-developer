package io.github.khayrulinnikita

import org.codehaus.groovy.control.CompilerConfiguration

static void main(String[] args) {
    def dsl = new ServerConfigDSL()
    def resource = Thread.currentThread().contextClassLoader.getResource("parent-config.conf")
    CompilerConfiguration cc = new CompilerConfiguration()
    cc.setScriptBaseClass(DelegatingScript.class.getName())
    GroovyShell shell = new GroovyShell(new Binding(),cc)
    DelegatingScript script = (DelegatingScript)shell.parse(resource.text)

    script.setDelegate(dsl)
    script.run()

    ServerConfig dslConfig = dsl.build()
    println(dslConfig)
}