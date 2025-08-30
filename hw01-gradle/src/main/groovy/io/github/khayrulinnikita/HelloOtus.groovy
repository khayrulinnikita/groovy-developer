package io.github.khayrulinnikita

import com.google.common.base.CaseFormat

class Main {
    static void main(String[] args) {
        def str = "SOME_VARIABLE"
        def camelCaseStr = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str)
        println "$str превратилось в $camelCaseStr"
    }
}