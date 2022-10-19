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


class CountryDictData{
    @Excel(name = "码")
    var value = ""
    @Excel(name = "名")
    var label = ""
}


class CountryDictOut{
    @Excel(name = "DT")
    var oldValue = ""
    @Excel(name = "ZZ")
    var newValue = ""
    @Excel(name = "国家名称")
    var label = ""
}


class CustUserVo {
    /**
     * 客户号
     */
    @Excel(name = "客户号")
    var custNo = "";

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    var custName = "";

    /**
     * 联系手机号
     */
    @Excel(name = "手机号")
    var phone = "";

    /**
     * 联系人名称
     */
    @Excel(name = "用户名")
    var name = "";

}


