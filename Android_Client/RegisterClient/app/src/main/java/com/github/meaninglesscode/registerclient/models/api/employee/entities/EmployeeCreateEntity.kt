package com.github.meaninglesscode.registerclient.models.api.employee.entities

import com.github.meaninglesscode.registerclient.models.api.employee.constants.EmployeeFieldNames
import com.squareup.moshi.Json

class EmployeeCreateEntity(employeeId: String = "", first: String = "", last: String = "",
                           role: String = "", password: String = "")
{
    @Json(name = EmployeeFieldNames.EMPLOYEE_ID)
    val employeeId: String = employeeId

    @Json(name = EmployeeFieldNames.FIRST_NAME)
    val first: String = first

    @Json(name = EmployeeFieldNames.LAST_NAME)
    val last: String = last

    @Json(name = EmployeeFieldNames.ROLE)
    val role: String = role

    @Json(name = EmployeeFieldNames.PASSWORD)
    val password: String = password
}