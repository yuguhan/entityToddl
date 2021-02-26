package com.storm.core

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
