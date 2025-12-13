package io.github.khayrulinnikita

import org.codehaus.groovy.control.CompilerConfiguration

class ServerConfigDSL {
    ServerConfig config = new ServerConfig()

    void setProperty(String name, value) {
        println("set prop ${name}")
    }

    def propertyMissing(String name, value) {
        if (name != 'mappings') {
            if (config.hasProperty(name)) {
                config.setProperty(name, value)
            } else {
                println("Property ${name} ignored")
            }
        } else {
            setMappings(value)
        }
    }

    void http(@DelegatesTo(HttpConfig) Closure cl) {
        HttpConfig httpConfig = new HttpConfig()
        cl.delegate = httpConfig
        cl.resolveStrategy = Closure.DELEGATE_ONLY
        cl.call()
        config.http = httpConfig
    }

    void include(String fileName) {
        def resource = Thread.currentThread().contextClassLoader.getResource(fileName)
        CompilerConfiguration cc = new CompilerConfiguration()
        cc.setScriptBaseClass(DelegatingScript.class.getName())
        GroovyShell shell = new GroovyShell(new Binding(),cc)
        DelegatingScript script = (DelegatingScript)shell.parse(resource.text)

        script.setDelegate(this)
        script.run()
    }

    void https(@DelegatesTo(HttpsConfig) Closure cl) {
        HttpsConfig httpsConfig = new HttpsConfig()
        cl.delegate = httpsConfig
        cl.resolveStrategy = Closure.DELEGATE_ONLY
        cl.call()
        config.https = httpsConfig
    }

    void setMappings(List<Closure> cls) {
        cls.each { cl ->
            Mapping mapping = new Mapping()
            cl.delegate = mapping
            cl.resolveStrategy = Closure.DELEGATE_ONLY
            cl.call()
            config.mappings << mapping
        }
    }

    ServerConfig build() {
        config
    }
}
