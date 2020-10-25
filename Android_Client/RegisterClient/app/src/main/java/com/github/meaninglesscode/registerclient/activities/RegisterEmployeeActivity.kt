package com.github.meaninglesscode.registerclient.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.employee.entities.EmployeeEntity
import com.github.meaninglesscode.registerclient.models.constants.PreferenceManagerNames
import com.github.meaninglesscode.registerclient.models.interfaces.BaseActivity
import com.github.meaninglesscode.registerclient.models.interfaces.RegisterApiClient
import com.github.meaninglesscode.registerclient.models.interfaces.handleError
import kotlinx.android.synthetic.main.activity_employee_register.*
import kotlinx.coroutines.experimental.runBlocking
import java.lang.ref.WeakReference

class RegisterEmployeeActivity : BaseActivity()
{
    private var mRegisterTask: RegisterEmployeeTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_register)

        this.viewToHide = register_form
        this.progressBarToShow = register_progress

        register_button.setOnClickListener { attemptRegister() }
    }

    private fun attemptRegister()
    {
        employee_id.error = null
        first_name.error = null
        last_name.error = null
        password.error = null

        val employeeIdStr = employee_id.text.toString()
        val firstNameStr = first_name.text.toString()
        val lastNameStr = last_name.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(employeeIdStr))
        {
            employee_id.error = getString(R.string.error_field_required)
            focusView = employee_id
            cancel = true
        }

        if (TextUtils.isEmpty(firstNameStr))
        {
            first_name.error = getString(R.string.error_field_required)
            focusView = first_name
            cancel = true
        }

        if (TextUtils.isEmpty(lastNameStr))
        {
            last_name.error = getString(R.string.error_field_required)
            focusView = last_name
            cancel = true
        }

        if (TextUtils.isEmpty(passwordStr))
        {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }
        else if (!isPasswordValid(passwordStr))
        {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        if (cancel)
        {
            focusView?.requestFocus()
        }
        else
        {
            showProgress(true)
            mRegisterTask = RegisterEmployeeTask(employeeIdStr, firstNameStr, lastNameStr, passwordStr, WeakReference(this))
            mRegisterTask!!.execute(null as Void?)
        }
    }

    private fun isPasswordValid(password: String): Boolean
    {
        return password.length > 4
    }

    companion object
    {
        class RegisterEmployeeTask internal constructor(private val mEmployeeId: String, private val mFirstName: String,
                                                        private val mLastName: String, private val mPassword: String,
                                                        private val registerEmployeeActivity: WeakReference<RegisterEmployeeActivity>): AsyncTask<Void, Void, Pair<Int, EmployeeEntity?>>()
        {
            private val weakReference: RegisterEmployeeActivity
                get() = registerEmployeeActivity.get()!!

            override fun doInBackground(vararg params: Void?): Pair<Int, EmployeeEntity?>
            {
                lateinit var registerResult: Pair<Int, EmployeeEntity?>

                runBlocking(block = {
                    registerResult = RegisterApiClient.createEmployee(mEmployeeId, mFirstName, mLastName, "", mPassword)
                })

                return Pair(registerResult.first, registerResult.second)
            }

            override fun onPostExecute(result: Pair<Int, EmployeeEntity?>)
            {
                weakReference.mRegisterTask = null
                weakReference.showProgress(false)

                when
                {
                    result.first == HTTPResponseCodes.OK ->
                    {
                        val intent = Intent(weakReference, MainMenuActivity::class.java)

                        val preferences = PreferenceManager.getDefaultSharedPreferences(weakReference)
                        preferences.edit().putString(
                                PreferenceManagerNames.LOGIN_DATA, result.second!!.toString()).apply()

                        weakReference.startActivity(intent)
                    }
                    result.first == HTTPResponseCodes.CONFLICT ->
                    {
                        weakReference.employee_id.error = weakReference.getString(R.string.error_employeeId_in_use)
                        weakReference.employee_id.requestFocus()
                    }
                    result.first == HTTPResponseCodes.UNPROCESSABLE_ENTITY ->
                    {
                        weakReference.employee_id.error = weakReference.getString(R.string.error_invalid_employeeId)
                        weakReference.employee_id.requestFocus()
                    }
                    else -> handleError(weakReference, result.first)
                }
            }

            override fun onCancelled()
            {
                weakReference.mRegisterTask = null
                weakReference.showProgress(false)
            }
        }
    }
}