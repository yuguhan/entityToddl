package com.storm.core

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.VariableDeclarator
import com.google.common.base.CaseFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import kotlin.io.path.ExperimentalPathApi

object Mapping {
    private val map = hashMapOf<String, String>(
        "boolean" to "tinyint(1)",
        "int" to "int",
        "short" to "int",
        "long" to "bigint",
        "float" to "float",
        "double" to "double",
        "char" to "varchar(255)",
        "String" to "varchar(255)",
        "Integer" to "int",
        "byte[]" to "blob",
        "Boolean" to "tinyint(1)",
        "Long" to "integer unsigned",
        "BigInteger" to "bigint unsigned",
        "Float" to "float",
        "Double" to "float",
        "Date" to "datetime",
        "Time" to "time",
        "Timestamp" to "datetime",
        "Date" to "datetime",
        "LocalDateTime" to "datetime",
        "LocalDate" to "date",
        "Byte" to "tinyint",
        "BigDecimal" to "decimal",
    )

    fun getType(type:String):String?{
        return map[type]
    }

}

@ExperimentalPathApi
fun main() {
    println(Mapping.getType("boolean"))

//    generate()

}

fun generate(){
    val path = "E:\\Users\\admin\\sourceCode\\infovalley-mall\\src\\main\\java\\com\\infovalley\\mall\\housekeeping\\model\\entity\\"
    val file = File(path)
    val outFile = File("C:\\Users\\admin\\Desktop\\ddl\\ddl.sql")
    val fileOutputStream = FileOutputStream(outFile, true)
    val outputStreamWriter = OutputStreamWriter(fileOutputStream, Charset.defaultCharset())

//    val sourceRoot = SourceRoot(Path(path))
    if (file.isDirectory) {
        val list = file.list { _, n -> n.endsWith(".java") }
        list?.forEach {
            val parse = StaticJavaParser.parse(File(path+it))
            val generateDDL = generateDDL(getTable(parse))
            generateDDL?.apply { outputStreamWriter.write(this) }
            outputStreamWriter.write("\n\n")
        }
    }
    outputStreamWriter.close()
    fileOutputStream.close()
}

fun generateDDL(table: Table?):String? {

    return table?.let {
        """
        CREATE TABLE ${it.table} (
          ${table.fields.joinToString (",\n"){field-> columnStr(field,table.ids) }}
          ${primaryStr(it.ids)}
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='${it.tableComment}' ;"""
    }
}

fun primaryStr(ids: List<String>): String {
    if (ids.isNotEmpty()) {
        ",PRIMARY KEY (${ids.joinToString()})"
    }
    return ""
}

/**
 * 根据字段 生成列语句
 */
fun columnStr(field: Field, ids: List<String>):String {
    return "${field.column} ${Mapping.getType(field.type)} COLLATE utf8mb4_bin ${if (ids.contains(field.column)) "NOT NULL" else "DEFAULT NULL"} COMMENT '${deleteNoUserChar(field.info)}' "
}

fun getTable(parse: CompilationUnit):Table?{
    var tableName = ""
    val fields = ArrayList<Field>()
    val ids = ArrayList<String>()
    return parse.primaryType.map {
        //处理表名
        it.annotations.forEach { annotationExpr ->
            if ("TableName" == annotationExpr.nameAsString) {
                annotationExpr.childNodes.forEach { node ->
                    if ("TableName" != node.toString()) {
                        tableName = checkQuotation(node.toString())
                    }
                }
            }
        }
        it.fields.forEach { field ->
            val variables: NodeList<VariableDeclarator> = field.variables
            val column = column(variables)
            fields += Field(
                column,
                field.elementType.asString(),
                field.comment.map { comment ->
                    comment.content
                }.orElse("没有列描述")
            )
            field.annotations.forEach { annotationExpr ->
                if ("TableId" == annotationExpr.nameAsString) {
                    ids += column

                }
            }
        }
        Table(tableName, ids, fields, it.comment.map { comment -> deleteNoUserChar(comment.content) }.orElse("没有表名"))

    }.orElse(null)

}

/**
 * 检查引号 并删除
 */
fun checkQuotation(tableName: String): String {
    return Regex("\"").replace(tableName, "")
}

fun deleteNoUserChar(str: String?): String {
    return str?.let {
        Regex("\\*|\\s*|\t|\r|\n").replace(str, "")
    }?:""
}

/**
 * 得到列名
 */
fun column(variables: NodeList<VariableDeclarator>): String {
    return CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE)
        .convert(variables.first.map { it.nameAsString }.orElse("列名为空")).orEmpty()
}