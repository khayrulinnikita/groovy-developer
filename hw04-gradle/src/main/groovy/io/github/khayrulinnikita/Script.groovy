package io.github.khayrulinnikita

def builder = XlsxBuilder.create("test.xlsx") {
    sheet(idx: 0, name: "qweasd") {
        row(0) {
            cell {
                value = "1"
                style = new Style(color: ColorName.GOLD)
            }
            cell {
                value = "test"
                style = new Style(color: ColorName.AQUA)
            }
            cell {
                value = 1
                style = new Style(color: ColorName.AQUA)
            }
            cell {
                value = true
            }
        }
    }
}