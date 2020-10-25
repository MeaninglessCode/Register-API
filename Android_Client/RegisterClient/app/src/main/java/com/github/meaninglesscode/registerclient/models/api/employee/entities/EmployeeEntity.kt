package com.github.meaninglesscode.registerclient.models.api.employee.entities

import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresSimpleDateFormat
import com.github.meaninglesscode.registerclient.models.api.employee.constants.EmployeeFieldNames
import com.github.meaninglesscode.registerclient.models.api.employee.types.Employee
import com.squareup.moshi.Json
import java.sql.Timestamp
import java.util.*

class EmployeeEntity(id: String = "", employeeId: String = "", first: String = "", last: String = "",
                     role: String = "", password: String = "", active: Boolean = true,
                     manager: String = "", created: String = "")
{
    @Json(name = EmployeeFieldNames.ID)
    var id: String = id

    @Json(name = EmployeeFieldNames.EMPLOYEE_ID)
    var employeeId: String = employeeId

    @Json(name = EmployeeFieldNames.FIRST_NAME)
    var first: String = first

    @Json(name = EmployeeFieldNames.LAST_NAME)
    var last: String = last

    @Json(name = EmployeeFieldNames.ROLE)
    var role: String = role

    @Json(name = EmployeeFieldNames.PASSWORD)
    var password: String = password

    @Json(name = EmployeeFieldNames.ACTIVE)
    var active: Boolean = active

    @Json(name = EmployeeFieldNames.MANAGER)
    var manager: String = manager

    @Json(name = EmployeeFieldNames.CREATED)
    var created: String = created

    override fun toString(): String {
        return "{" +
                "\"${EmployeeFieldNames.ID}\": \"$id\"," +
                "\"${EmployeeFieldNames.EMPLOYEE_ID}\": \"$employeeId\"," +
                "\"${EmployeeFieldNames.FIRST_NAME}\": \"$first\"," +
                "\"${EmployeeFieldNames.LAST_NAME}\": \"$last\"," +
                "\"${EmployeeFieldNames.ROLE}\": \"$role\"," +
                "\"${EmployeeFieldNames.PASSWORD}\": \"$password\"," +
                "\"${EmployeeFieldNames.ACTIVE}\": $active," +
                "\"${EmployeeFieldNames.MANAGER}\": \"$manager\"," +
                "\"${EmployeeFieldNames.CREATED}\": \"$created\"" +
                "}"
    }

    fun toEmployee(): Employee {
        val newId = if (id == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(id)
        val newManager = if (manager == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(manager)

        val newCreated = created.replace("T", " ").replace("Z", "")
        val timestamp = Timestamp(PostgresSimpleDateFormat.parse(newCreated).time)

        return Employee(newId, employeeId, first, last, role, password, active, newManager, timestamp)
    }
}