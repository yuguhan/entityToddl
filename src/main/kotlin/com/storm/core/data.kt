package com.storm.core

import cn.afterturn.easypoi.excel.annotation.Excel

data class Field(
    val column: String?,
    val type: String,
    val info: String?,
    var canNull: Boolean = true
)

data class Table(
    val table: String,
    val ids:List<String>,
    val fields:List<Field>,
    val tableComment:String
)

/**
 * excel生成实体类
 */
class ExcelFields{
    @Excel(name="name")
    lateinit var name: String
    @Excel(name="intro")
    lateinit var intro: String
    @Excel(name="type")
    lateinit var type: String

}