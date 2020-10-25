package com.github.meaninglesscode.registerclient.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.employee.entities.EmployeeEntity
import com.github.meaninglesscode.registerclient.models.constants.PreferenceManagerNames
import com.github.meaninglesscode.registerclient.models.interfaces.BaseActivity
import com.github.meaninglesscode.registerclient.models.interfaces.RegisterApiClient
import com.github.meaninglesscode.registerclient.models.interfaces.handleError
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.experimental.runBlocking
import java.lang.ref.WeakReference

class LoginActivity : BaseActivity() {
    private var mAuthTask: UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewToHide = login_form
        progressBarToShow = login_progress

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        sign_in_button.setOnClickListener { attemptLogin() }
        register_button.setOnClickListener { showRegisterEmployeeActivity() }
    }

    private fun populateAutoComplete() {
        //loaderManager.initLoader(0, null, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray)
    {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }

    private fun showRegisterEmployeeActivity()
    {
        val intent = Intent(this@LoginActivity, RegisterEmployeeActivity::class.java)
        startActivity(intent)
    }

    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        employee_id.error = null
        password.error = null

        val employeeIdStr = employee_id.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }

        if (TextUtils.isEmpty(employeeIdStr)) {
            employee_id.error = getString(R.string.error_field_required)
            focusView = employee_id
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        }
        else {
            showProgress(true)
            mAuthTask = UserLoginTask(employeeIdStr, passwordStr, WeakReference(this))
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun addEmployeesToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@LoginActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        employee_id.setAdapter(adapter)
    }

    companion object {
        private const val REQUEST_READ_CONTACTS = 0

        class UserLoginTask internal constructor(private val mEmployeeId: String, private val mPassword: String,
                                                 private val loginActivity: WeakReference<LoginActivity>) : AsyncTask<Void, Void, Pair<Int, EmployeeEntity?>>()
        {
            private val weakReference: LoginActivity
                get() = loginActivity.get()!!

            override fun doInBackground(vararg params: Void): Pair<Int, EmployeeEntity?> {
                lateinit var loginResult: Pair<Int, EmployeeEntity?>

                runBlocking(block = {
                    loginResult = RegisterApiClient.employeeLogin(mEmployeeId, mPassword)
                })

                return Pair(loginResult.first, loginResult.second)
            }

            override fun onPostExecute(result: Pair<Int, EmployeeEntity?>) {
                weakReference.mAuthTask = null
                weakReference.showProgress(false)

                when {
                    result.first == HTTPResponseCodes.OK -> {
                        val intent = Intent(weakReference, MainMenuActivity::class.java)

                        val preferences = PreferenceManager.getDefaultSharedPreferences(weakReference)
                        preferences.edit().putString(
                                PreferenceManagerNames.LOGIN_DATA, result.second!!.toString()).apply()

                        weakReference.startActivity(intent)
                    }
                    result.first == HTTPResponseCodes.UNAUTHORIZED -> {
                        weakReference.password.error = weakReference.getString(R.string.error_incorrect_password)
                        weakReference.password.requestFocus()
                    }
                    result.first == HTTPResponseCodes.NOT_FOUND -> {
                        weakReference.employee_id.error = weakReference.getString(R.string.error_employee_not_found)
                        weakReference.employee_id.requestFocus()
                    }
                    else -> handleError(weakReference, result.first)
                }
            }

            override fun onCancelled() {
                weakReference.mAuthTask = null
                weakReference.showProgress(false)
            }
        }
    }
}