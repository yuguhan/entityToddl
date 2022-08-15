package com.storm.core

import cn.afterturn.easypoi.excel.ExcelImportUtil
import cn.afterturn.easypoi.excel.entity.ImportParams
import cn.hutool.core.util.StrUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.util.*

val outFile = File("/Users/xuyunpeng/Documents/企业金融文件/自动生成文件夹/exceltopojo/result/")
fun main() {
    val path = "/Users/xuyunpeng/Documents/企业金融文件/自动生成文件夹/exceltopojo/"
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
    var index = 0
    xssfWorkbook.sheetIterator().forEach {
        val sheetName = StrUtil.upperFirst(StrUtil.toCamelCase(it.sheetName))
        val importParams = ImportParams()
        importParams.startSheetIndex = index
        importParams.sheetNum = 1
        val importExcel = ExcelImportUtil.importExcel<PoJoExcelFields>(currentFile, PoJoExcelFields::class.java, importParams)
            .distinctBy { f->f.name}
        createFile(sheetName,
            """
               
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class $sheetName{
    ${importExcel.filter { i->StrUtil.isNotEmpty(i.name) }.joinToString("") { i-> getOneField(i) }}        
}                
    """
        )
        index++
    }


}


fun getOneField(poJoExcelFields: PoJoExcelFields):String {
    return """/**
     * ${poJoExcelFields.intro}${getMust(poJoExcelFields.mustHave.trim())}${getRemarks(poJoExcelFields.remarks)}
     */
    private ${getType(poJoExcelFields.type, poJoExcelFields.name)} ${poJoExcelFields.name};
    """
}

fun getRemarks(remarks: String): String {
    if (remarks.isNullOrBlank()) {
        return ""
    }
    return "\n     * $remarks"

}

fun getMust(trim: String): String {
    return when (trim) {
        "1", "是", "必须" -> "\n     * 必传"
        else -> ""
    }
}

/**
 * type转换可以自己实现
 */
fun getType(type: String,name:String): String {
    val typeToUpperCase = type.trim().uppercase(Locale.getDefault())
    return when {
        "STRING" == typeToUpperCase -> "String"
        "NUMBER" == typeToUpperCase -> "BigDecimal"
        "CHAR" == typeToUpperCase -> "String"
        "OBJECT" == typeToUpperCase -> StrUtil.upperFirst(name.trim())
        "LIST" == typeToUpperCase -> "List<${StrUtil.upperFirst(name.trim())}>"
        checkDouble(type) -> "Double"
        else -> type.trim()
    }
}

fun checkDouble(str:String):Boolean{
    return str.uppercase() .contains("DOUBLE")
}

fun createFile(fileName:String,content:String){
    val fileOutputStream = FileOutputStream("${outFile}/$fileName.java", false)

    val outputStreamWriter = OutputStreamWriter(fileOutputStream, Charset.defaultCharset())
    outputStreamWriter.write(content)
    outputStreamWriter.write("\n\n")
    outputStreamWriter.close()
    fileOutputStream.close()
}
