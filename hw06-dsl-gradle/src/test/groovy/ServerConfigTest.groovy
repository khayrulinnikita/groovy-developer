import io.github.khayrulinnikita.ServerConfig
import io.github.khayrulinnikita.ServerConfigDSL
import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

class ServerConfigTest extends Specification {
    def "Запуск конфигурирования"() {
        when:
        def dsl = new ServerConfigDSL()
        def resource = Thread.currentThread().contextClassLoader.getResource("test-parent-config.conf")
        CompilerConfiguration cc = new CompilerConfiguration()
        cc.setScriptBaseClass(DelegatingScript.class.getName())
        GroovyShell shell = new GroovyShell(new Binding(),cc)
        DelegatingScript script = (DelegatingScript)shell.parse(resource.text)

        script.setDelegate(dsl)
        script.run()

        ServerConfig dslConfig = dsl.build()

        then:
        noExceptionThrown()
        dslConfig.name == "MyTest"
        dslConfig.description == "Apache Tomcat"
        dslConfig.http.port == 8080
        dslConfig.https.port == 8443
        !dslConfig.http.secure
        dslConfig.https.secure

    }
}