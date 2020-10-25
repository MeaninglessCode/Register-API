package com.github.meaninglesscode.registerclient.activities

import android.content.Intent
import android.os.Bundle
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.models.api.employee.types.Employee
import com.github.meaninglesscode.registerclient.models.interfaces.BaseActivity
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class MainMenuActivity : BaseActivity() {
    private var activeEmployee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        viewToHide = main_menu
        progressBarToShow = main_menu_progress
        activeEmployee = getCurrentEmployee()

        current_user_display.text = getString(R.string.text_view_currentEmployee,
                activeEmployee!!.employeeId, activeEmployee!!.first, activeEmployee!!.last)

        logout_button.setOnClickListener { logoutCurrentUser() }

        product_main_button.setOnClickListener { startProductsActivity() }
        shopping_cart_button.setOnClickListener { startTransactionsActivity() }
    }

    private fun logoutCurrentUser()
    {
        showProgress(true)

        val result = async(CommonPool) {
            userLogoutTask(activeEmployee!!)
        }

        if (result.isCancelled) {
            showProgress(false)
        }
    }

    private fun startProductsActivity() {
        val intent = Intent(this@MainMenuActivity, ProductsMainActivity::class.java)
        startActivity(intent)
    }

    private fun startTransactionsActivity() {
        val intent = Intent(this@MainMenuActivity, TransactionsMainActivity::class.java)
        startActivity(intent)
    }
}