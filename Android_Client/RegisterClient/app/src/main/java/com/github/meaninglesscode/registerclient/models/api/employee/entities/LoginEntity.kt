package com.github.meaninglesscode.registerclient.models.api.employee.entities

import com.github.meaninglesscode.registerclient.models.api.employee.constants.EmployeeFieldNames
import com.squareup.moshi.Json

class LoginEntity(employeeId: String = "", password: String = "") {
    @Json(name = EmployeeFieldNames.EMPLOYEE_ID)
    val employeeId: String = employeeId

    @Json(name = EmployeeFieldNames.PASSWORD)
    val password: String = password
}