package com.github.meaninglesscode.registerclient.models.api.constants

enum class ApiURLs(private val value: String) {
    BASE_URL("https://team-hammer-app.herokuapp.com/api/"),
    PRODUCT("${BASE_URL}product/"),
    EMPLOYEE("${BASE_URL}employee/"),
    LOGIN_URL("${BASE_URL}employee/login/");

    override fun toString() = value
}