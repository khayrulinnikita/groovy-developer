import io.github.khayrulinnikita.Ruble
import io.github.khayrulinnikita.RubleATM
import spock.lang.Specification

class RubleATMTest extends Specification {
    def "проверка пополнения баланса"() {
        setup:
        def atm = new RubleATM()

        when:
        atm += new Ruble(100)
        atm += new Ruble(500)

        then:
        atm.getBalance() == 600
    }

    def "проверка валидной выдачи"() {
        setup:
        def atm = new RubleATM()
        atm = atm + new Ruble(500) + new Ruble(100) + new Ruble(5000)

        when:
        def cash = atm.withdraw(5100)

        then:
        cash*.value.sort() == [100, 5000]
        cash*.currency.unique() == ["RUB"]

        and:
        atm.getBalance() == 500
    }

    def "проверка невалидной выдачи"() {
        setup:
        def atm = new RubleATM()
        atm = atm + new Ruble(500) + new Ruble(100) + new Ruble(5000)

        when:
        atm.withdraw(4100)

        then:
        thrown(IllegalArgumentException)
    }
}
