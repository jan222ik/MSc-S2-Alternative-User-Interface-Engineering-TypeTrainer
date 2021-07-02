package com.github.jan222ik.desktop.textgen.database.schema

import org.jetbrains.exposed.sql.Table

object UserSettings : Table() {
    val id = integer("id")
    val locale = varchar("locale", 3)

    override val primaryKey = PrimaryKey(id)

    const val CONST_ID = 1
}
