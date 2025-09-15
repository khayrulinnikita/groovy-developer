import io.github.khayrulinnikita.Ruble
import spock.lang.Specification

class RubleTest extends Specification {
    def "Создаем 100 рублей"() {
        when:
        def oneHundredRubles = new Ruble(100)

        then:
        noExceptionThrown()
        oneHundredRubles.value == 100
        oneHundredRubles.currency == "RUB"
    }

    def "Создаем 123 рублей"() {
        when:
        new Ruble(123)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Номинал 123 недопустим"
    }
}
