package com.storm.core

import cn.afterturn.easypoi.excel.ExcelImportUtil
import cn.afterturn.easypoi.excel.entity.ImportParams
import cn.hutool.core.util.StrUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

val outFile = File("F:\\自动生成文件夹\\exceltopojo\\result")
fun main() {
    val path = "F:\\自动生成文件夹\\exceltopojo\\"
    val file = File(path)

    if (file.isDirectory) {
        val list = file.list { _, n -> n.endsWith(".xlsx") }
        list?.forEach {
            val currentFile = File(path + it)
            getPojo(currentFile)

        }
    }
}

fun getPojo(currentFile: File): Unit {

    val input = currentFile.inputStream()
    val xssfWorkbook = XSSFWorkbook(input)
    var index = 1
    xssfWorkbook.sheetIterator().forEach {
        val sheetName = it.sheetName
        val importParams = ImportParams()
        importParams.sheetNum = index
        val importExcel = ExcelImportUtil.importExcel<ExcelFields>(currentFile, ExcelFields::class.java, importParams)
            .distinctBy { f->f.name}
        createFile(sheetName,
            """
public class ${StrUtil.upperFirst(sheetName)}{
    ${importExcel.joinToString("\n\n") { i-> getOneField(i) }}        
}                
    """
        )
        index++
    }


}


fun getOneField(excelFields: ExcelFields):String {
    return """
    /**
     * ${excelFields.intro}
     */
    private ${getType(excelFields.type,excelFields.name)} ${excelFields.name};        
    """
}

/**
 * type转换可以自己实现
 */
fun getType(type: String,name:String): String {
    val typeToUpperCase = type.trim().toUpperCase()
    return when {
        "NUMBER" == typeToUpperCase -> "BigDecimal"
        "OBJECT" == typeToUpperCase -> StrUtil.upperFirst(name.trim())
        "LIST" == typeToUpperCase -> "List<${StrUtil.upperFirst(name.trim())}>"
        checkDouble(type) -> "Double"
        else -> type.trim()
    }
}

fun checkDouble(str:String):Boolean{
    return str.toUpperCase().contains("DOUBLE")
}

fun createFile(fileName:String,content:String){
    val fileOutputStream = FileOutputStream("${outFile}/${StrUtil.upperFirst(fileName)}.java", false)

    val outputStreamWriter = OutputStreamWriter(fileOutputStream, Charset.defaultCharset())
    outputStreamWriter.write(content)
    outputStreamWriter.write("\n\n")
    outputStreamWriter.close()
    fileOutputStream.close()
}
