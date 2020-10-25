package com.github.meaninglesscode.registerclient.models.api.constants

import java.text.SimpleDateFormat

enum class PostgresConstants(val value: String) {
    EMPTY_UUID("00000000-0000-0000-0000-000000000000"),
    TIMESTAMP_FORMAT("yyyy-MM-dd HH:mm:ss.SSS");
}

val PostgresSimpleDateFormat = SimpleDateFormat(PostgresConstants.TIMESTAMP_FORMAT.value)
val PostgresHumanReadableDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")