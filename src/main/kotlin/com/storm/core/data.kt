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
    val ids: List<String>,
    val fields: List<Field>,
    val tableComment: String
)

/**
 * excel生成实体类
 */
class PoJoExcelFields {
    @Excel(name = "name")
    var name = ""

    @Excel(name = "intro")
    var intro = ""

    @Excel(name = "type")
    var type = ""

    @Excel(name = "length")
    var length =  ""

    @Excel(name = "must")
    var mustHave = ""

    @Excel(name = "remarks")
    var remarks = ""
}

class CustData{
    @Excel(name = "UC客户号")
    var ucNo = ""
    @Excel(name = "金证客户号")
    var csNo = ""
}