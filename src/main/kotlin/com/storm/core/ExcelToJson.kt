package com.storm.core

import cn.afterturn.easypoi.excel.ExcelImportUtil
import cn.afterturn.easypoi.excel.entity.ImportParams
import cn.hutool.core.util.StrUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

fun main() {
    val path = "/Users/xuyunpeng/Documents/企业金融文件/自动生成文件夹/exceltopojo/转托管/"
    val file = File(path)

    if (file.isDirectory) {
        val list = file.list { _, n -> n.endsWith(".xlsx") }
        list?.forEach {
            val currentFile = File(path + it)
            printListBean(currentFile)
        }
    }
}

fun printListBean(currentFile: File) {
    val importParams = ImportParams()
    importParams.startSheetIndex = 0
    importParams.sheetNum = 1

    val toMutableList = ExcelImportUtil.importExcel<PoJoExcelFields>(currentFile, CustData::class.java, importParams)
        .toMutableList()
    println(ObjectMapper().writeValueAsString(toMutableList))

}