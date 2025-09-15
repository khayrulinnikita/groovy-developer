package io.github.khayrulinnikita

abstract class Banknote {
    int value
    String currency

    Banknote(int value, String currency) {
        this.value = value
        this.currency = currency
    }

    String toString() {
        return "Купюра $value $currency"
    }
}

class Ruble extends Banknote {
    private static final Set<Integer> ALLOWED_VALUES = [10, 50, 100, 500, 1000, 5000]
    Ruble(int value) {
        super(value, "RUB")
        if (!(value in ALLOWED_VALUES)) throw new IllegalArgumentException("Номинал $value недопустим")
    }
}

abstract class ATM {
    protected String currency
    protected Map<Integer, Integer> cells = [:].withDefault {0}

    ATM(String currency) {
        this.currency = currency
    }

    abstract ATM plus(Banknote b)
    abstract List<Banknote> withdraw(int amount)

    ATM plus(List<Banknote> banknotes) {
        banknotes.each{plus(it)}
        return this
    }

    int getBalance() {
        return cells.collect { denom, count -> denom * count}.sum() ?: 0
    }

    String toString() {
        return getBalance()
    }
}

class RubleATM extends ATM {
    RubleATM() { super("RUB") }

    RubleATM plus(Banknote b) {
        if (b instanceof Ruble) {
            cells[b.value]++
        } else {
            throw new IllegalArgumentException("Банкомат принимает только рублевые купюры")
        }
        return this
    }

    List<Banknote> withdraw(int amount) {
        def sortedBanknotesNominals = cells.keySet().sort().reverse()
        int remainder = amount
        Map<Integer, Integer> result = [:].withDefault { 0 }

        // проходимся от самых крупных купюр к самым мелким и считаем сколько их необходимо выдать
        // если есть возможность выдать купюру, уменьшаем остаток (remainder)
        sortedBanknotesNominals.each { nominal ->
            int need = remainder.intdiv(nominal)
            int able = Math.min(need, cells[nominal])
            if (able) {
                result[nominal] = able
                remainder -= nominal * able
            }
        }

        // если остаток ненулевой, значит выдать не можем
        if (remainder > 0) {
            throw new IllegalArgumentException("Невозможно выдать сумму $amount")
        }

        // если выдать сумму можем, уменьшаем остаток в банкомате по ячейкам
        result.each {nominal, count ->
            cells[nominal] -= count
        }

        // преобразуем номинал:количество в список купюр для выдачи
        return result.collectMany {nominal, count ->
            (1..count).collect{ new Ruble(nominal) }
        }
    }
}

class Main {
    static void main(String[] args) {
        println "qwe"
        RubleATM atm = new RubleATM()
        def threeHundredRubles = (1..3).collect{new Ruble(100) }
        atm += threeHundredRubles
        println(atm)
        atm += new Ruble(1000)
        atm += new Ruble(5000)
        atm += new Ruble(500)

        def result = atm.withdraw(800)
        println(result)
    }
}