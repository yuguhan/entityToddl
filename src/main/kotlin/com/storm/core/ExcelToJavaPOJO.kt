package com.storm.core

import cn.afterturn.easypoi.excel.ExcelImportUtil
import cn.afterturn.easypoi.excel.entity.ImportParams
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

fun main() {
    val path = "F:\\自动生成文件夹\\exceltopojo\\"
    val file = File(path)
    val outFile = File("F:\\自动生成文件夹\\exceltopojo\\result\\result.txt")
    val fileOutputStream = FileOutputStream(outFile, true)
    val outputStreamWriter = OutputStreamWriter(fileOutputStream, Charset.defaultCharset())
    if (file.isDirectory) {
        val list = file.list { _, n -> n.endsWith(".xlsx") }
        list?.forEach {
            val currentFile = File(path + it)
            val pojoStr = getPojo(currentFile)
            pojoStr.apply { outputStreamWriter.write(this) }
            outputStreamWriter.write("\n\n")
        }
    }
    outputStreamWriter.close()
    fileOutputStream.close()
}

fun getPojo(currentFile: File): String {

    val importExcel = ExcelImportUtil.importExcel<ExcelFields>(currentFile, ExcelFields::class.java, ImportParams())
    return """
//----------------开始--------------------
        
${importExcel.joinToString("\n\n") { getOneField(it) }}        
        
//----------------结束-------------------
        
    """.trimIndent()
}


fun getOneField(excelFields: ExcelFields):String {
   return """
        /**
         * ${excelFields.intro}
         */
        private ${excelFields.type} ${excelFields.name};
    """.trimIndent()
}

/**
 * type转换可以自己实现
 */
fun getType(type:String):String{
    return type
}
