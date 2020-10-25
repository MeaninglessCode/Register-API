package com.github.meaninglesscode.registerclient.models.api.employee.types

import android.os.Parcel
import android.os.Parcelable
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresSimpleDateFormat
import com.github.meaninglesscode.registerclient.models.api.employee.entities.EmployeeEntity
import java.sql.Timestamp
import java.util.*

class Employee(id: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value), employeeId: String = "",
               first: String = "", last: String = "", role: String = "", password: String = "",
               active: Boolean = true, manager: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value),
               created: Timestamp = Timestamp(System.currentTimeMillis())) : Parcelable
{
    var id: UUID = id
    var employeeId: String = employeeId
    var first: String = first
    var last: String = last
    var role: String = role
    var password: String = password
    var active: Boolean = active
    var manager: UUID = manager
    var created: Timestamp = created

    fun toEmployeeEntity(): EmployeeEntity {
        val createdString = "${created.toString().replace(" ", "T")}Z"

        return EmployeeEntity(id.toString(), employeeId, first, last, role, password, active,
                manager.toString(), createdString)
    }

    constructor(parcel: Parcel) : this() {
        id = UUID.fromString(parcel.readString())
        employeeId = parcel.readString()
        first = parcel.readString()
        last = parcel.readString()
        password = parcel.readString()
        active = parcel.readByte() != 0.toByte()
        manager = UUID.fromString(parcel.readString())
        created = Timestamp(PostgresSimpleDateFormat.parse(parcel.readString()).time)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id.toString())
        parcel.writeString(employeeId)
        parcel.writeString(first)
        parcel.writeString(last)
        parcel.writeString(password)
        parcel.writeByte(if (active) 1 else 0)
        parcel.writeString(manager.toString())
        parcel.writeString(created.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Employee> {
        override fun createFromParcel(parcel: Parcel): Employee {
            return Employee(parcel)
        }

        override fun newArray(size: Int): Array<Employee?> {
            return arrayOfNulls(size)
        }
    }

}