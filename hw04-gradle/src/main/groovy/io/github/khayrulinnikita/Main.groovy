package io.github.khayrulinnikita

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook

class XlsxBuilder {
    private String filename
    SXSSFWorkbook workbook
    List<Sheet> sheets = []
    Map<ColorName, CellStyle> styleCache = [:]

    XlsxBuilder(String filename) {
        if (!filename.toLowerCase().endsWith(".xlsx")) {
            filename += ".xlsx"
        }
        this.workbook = new SXSSFWorkbook()
        this.filename = filename
    }

    CellStyle getCellStyle(Style style) {
        if (style == null) return null

        if (!styleCache.containsKey(style.color)) {
            CellStyle cs = workbook.createCellStyle()
            cs.setFillForegroundColor(getColor(style.color))
            cs.setFillPattern(FillPatternType.SOLID_FOREGROUND)
            cs.setBorderTop(BorderStyle.THIN)
            cs.setBorderBottom(BorderStyle.THIN)
            cs.setBorderLeft(BorderStyle.THIN)
            cs.setBorderRight(BorderStyle.THIN)

            styleCache[style.color] = cs
        }

        return styleCache[style.color]
    }

    private short getColor(ColorName colorName) {
        switch (colorName) {
            case ColorName.AQUA: return IndexedColors.AQUA.getIndex()
            case ColorName.BLACK: return IndexedColors.BLACK.getIndex()
            case ColorName.CORAL: return IndexedColors.CORAL.getIndex()
            case ColorName.GOLD: return IndexedColors.GOLD.getIndex()
            case ColorName.GREEN: return IndexedColors.GREEN.getIndex()
            case ColorName.WHITE: return IndexedColors.WHITE.getIndex()
        }
    }

    private Sheet currentSheet
    private Row currentRow

    def sheet(Map args = [:], Closure cl) {
        Sheet sheet = new Sheet(idx: args.idx, name: args.name)
        sheets << sheet
        currentSheet = sheet

        cl.delegate = this
        cl.resolveStrategy = Closure.DELEGATE_ONLY
        cl.call()
    }

    def row(Integer idx, Closure cl) {
        Row row = new Row(idx: idx)
        currentSheet.rows << row
        currentRow = row

        cl.delegate = this
        cl.resolveStrategy = Closure.DELEGATE_ONLY
        cl.call()
    }

    def cell(@DelegatesTo(MyCell) Closure cl) {
        MyCell cell = new MyCell()
        cl.delegate = cell
        cl.resolveStrategy = Closure.DELEGATE_ONLY
        cl.call()
        currentRow.cells << cell
    }

    def build() {
        sheets.each {sheet ->
            SXSSFSheet xSheet = workbook.createSheet(sheet.name ?: sheet.idx.toString())
            sheet.rows.each { row ->
                SXSSFRow xRow = xSheet.createRow(row.idx)
                row.cells.eachWithIndex{ MyCell cell, int i ->
                    Cell xCell = xRow.createCell(i)
                    CellStyle cs = getCellStyle(cell.style)
                    xCell.setCellValue(cell.value)
                    if (cs != null) xCell.setCellStyle(cs)
                }
            }
        }

        FileOutputStream fos = new FileOutputStream(new File("${this.filename}"))
        try {
            workbook.write(fos)
            workbook.close()
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
        println("${this.filename} successfull generated")
    }

    static XlsxBuilder create(String filename, @DelegatesTo(XlsxBuilder) Closure config) {
        XlsxBuilder builder = new XlsxBuilder(filename)
        config.delegate = builder
        config.resolveStrategy = Closure.DELEGATE_ONLY
        config.call()
        builder.build()
        return builder
    }
}

class MyCell {
    def value
    Style style
}

class Row {
    int idx
    List<MyCell> cells = []
}

class Sheet {
    int idx
    String name
    List<Row> rows = []
}

IndexedColors.WHITE

enum ColorName {
    AQUA,
    BLACK,
    CORAL,
    GOLD,
    GREEN,
    WHITE
}
class Style {
    ColorName color
}