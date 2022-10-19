package com.storm.core

import cn.afterturn.easypoi.excel.ExcelExportUtil
import cn.afterturn.easypoi.excel.ExcelImportUtil
import cn.afterturn.easypoi.excel.entity.ExportParams
import cn.afterturn.easypoi.excel.entity.ImportParams
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File


fun main() {
//    toOutExcel()
    printCust()
}



fun toOutExcel(){
    val oldPath = "/Users/xuyunpeng/Documents/个人文档/20220905-135450.xlsx"
    val newPath = "/Users/xuyunpeng/Documents/个人文档/国家新.xlsx"

    val oldValues:List<CountryDictData> = getDicts(File(oldPath))
    val newValues: List<CountryDictData> = getDicts(File(newPath))
    val result = mutableListOf<CountryDictOut>()

    newValues.forEach {new ->
        val current = CountryDictOut()
        current.newValue = new.value
        current.label = new.label
        oldValues.firstOrNull() { old -> old.label == new.label }?.run {
            current.oldValue = this.value
        }
        result.add(current)
    }
    val workbook = ExcelExportUtil.exportExcel(ExportParams("计算机一班学生", "sheet1",ExcelType.XSSF), CountryDictOut::class.java, result)

    val file = File("/Users/xuyunpeng/Documents/个人文档/result.xlsx")
    // 创建文件夹
    val fos = file.outputStream()
    workbook.write(fos)
}

inline fun <reified T> getDicts(file: File):List<T>{
    val importParams = ImportParams()
    importParams.startSheetIndex = 0
    importParams.sheetNum = 1
    return ExcelImportUtil.importExcel(file, T::class.java, importParams)
}



fun printCust(){
    val path = "/Users/xuyunpeng/Documents/项目总体文档/企业金融迭代/安益宝下线赎回短信/测试.xlsx"
    val custs: List<CustUserVo> = getDicts(File(path))
    println(ObjectMapper().writeValueAsString(custs))

}