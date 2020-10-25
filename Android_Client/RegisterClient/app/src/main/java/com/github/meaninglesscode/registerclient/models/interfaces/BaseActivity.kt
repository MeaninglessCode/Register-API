package com.github.meaninglesscode.registerclient.models.interfaces

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.meaninglesscode.registerclient.activities.LoginActivity
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.employee.entities.EmployeeEntity
import com.github.meaninglesscode.registerclient.models.api.employee.types.Employee
import com.github.meaninglesscode.registerclient.models.constants.PreferenceManagerNames
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking

abstract class BaseActivity: AppCompatActivity() {
    protected lateinit var viewToHide: View
    protected lateinit var progressBarToShow: View

    protected fun setCurrentEmployee(employee: EmployeeEntity) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.edit().putString(PreferenceManagerNames.LOGIN_DATA, employee.toString()).apply()
    }

    protected fun getCurrentEmployee(): Employee {
        val userString = PreferenceManager.getDefaultSharedPreferences(this).getString(PreferenceManagerNames.LOGIN_DATA, null)
        val converter: JsonAdapter<EmployeeEntity> = Moshi.Builder().build().adapter(EmployeeEntity::class.java)
        return converter.fromJson(userString).toEmployee()
    }

    protected suspend fun userLogoutTask(mEmployee: Employee) {
        lateinit var logoutResult: Pair<Int, EmployeeEntity?>
        mEmployee.active = false

        runBlocking(CommonPool) {
            logoutResult = RegisterApiClient.updateEmployee(mEmployee.id.toString(), mEmployee.toEmployeeEntity())
        }

        if (logoutResult.first == HTTPResponseCodes.OK) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            preferences.edit().remove(PreferenceManagerNames.LOGIN_DATA).apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else {
            handleError(this, logoutResult.first)
        }
    }

    protected fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        viewToHide.visibility = if (show) View.GONE else View.VISIBLE
        viewToHide.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewToHide.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        progressBarToShow.visibility = if (show) View.VISIBLE else View.GONE
        progressBarToShow.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        progressBarToShow.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }
}