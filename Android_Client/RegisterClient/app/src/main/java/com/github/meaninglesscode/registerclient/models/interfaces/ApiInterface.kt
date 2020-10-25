package com.github.meaninglesscode.registerclient.models.interfaces

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.models.api.constants.ApiURLs
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.employee.entities.EmployeeCreateEntity
import com.github.meaninglesscode.registerclient.models.api.employee.entities.EmployeeEntity
import com.github.meaninglesscode.registerclient.models.api.employee.entities.LoginEntity
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductCreateEntity
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionCreateEntity
import com.github.meaninglesscode.registerclient.models.constants.ErrorCodes
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.experimental.suspendCoroutine

object RegisterApiClient {
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()

    private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .baseUrl(ApiURLs.BASE_URL.toString())
            .build()

    private val registerApi = retrofit.create(RegisterApiService::class.java)

    // Product Endpoint Functions

    suspend fun getAllProducts(): Pair<Int, List<ProductEntity>?> {
        return try {
            val adapterType = Types.newParameterizedType(List::class.java, ProductEntity::class.java)
            val converter: JsonAdapter<List<ProductEntity>> = Moshi.Builder().build().adapter(adapterType)

            val req = Request.Builder().url("${ApiURLs.BASE_URL}product/").build()
            val res = client.newCall(req).await()
            val result = converter.fromJson(res.body()!!.string())

            Pair(res.code(), result)
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun searchProductsByTerm(term: String): Pair<Int, List<ProductEntity>?> {
        return try {
            val adapterType = Types.newParameterizedType(List::class.java, ProductEntity::class.java)
            val converter: JsonAdapter<List<ProductEntity>> = Moshi.Builder().build().adapter(adapterType)

            val req = Request.Builder().url("${ApiURLs.BASE_URL}product/search/$term").build()
            val res = client.newCall(req).await()
            val result = converter.fromJson(res.body()!!.string())

            Pair(res.code(), result)
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun createProduct(lookupCode: String, name: String, description: String, price: String, inventory: String): Pair<Int, ProductEntity?> {
        return try {
            val newProduct = ProductCreateEntity(lookupCode, name, description, price, inventory)
            val res = registerApi.createProduct(newProduct).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun updateProduct(id: String, productEntity: ProductEntity): Pair<Int, ProductEntity?> {
        return try {
            val res = registerApi.updateProduct(id, productEntity).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun deleteProduct(id: String): Int {
        return try {
            val res = registerApi.deleteProduct(id).await()
            res.code()
        }
        catch (t: Throwable) {
            handleThrowable(t)
        }

    }

    // Employee Endpoint Functions

    suspend fun employeeExists(): Int {
        return try {
            val res = registerApi.employeesExist().await()
            res.code()
        }
        catch (t: Throwable) {
            handleThrowable(t)
        }
    }

    suspend fun getEmployeeById(id: String): Pair<Int, EmployeeEntity?> {
        return try {
            val res = registerApi.getEmployeeById(id).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun getEmployeeByEmployeeId(id: String): Pair<Int, EmployeeEntity?> {
        return try {
            val res = registerApi.getEmployeeByEmployeeId(id).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun createEmployee(employeeId: String, first: String, last: String, role: String, password: String): Pair<Int, EmployeeEntity?> {
        return try {
            val newEmployee = EmployeeCreateEntity(employeeId, first, last, role, password)
            val res = registerApi.createEmployee(newEmployee).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun employeeLogin(employeeId: String, password: String): Pair<Int, EmployeeEntity?> {
        return try {
            val login = LoginEntity(employeeId, password)
            val res = registerApi.employeeLogin(login).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    suspend fun updateEmployee(employeeId: String, employeeEntity: EmployeeEntity): Pair<Int, EmployeeEntity?> {
        return try {
            val res = registerApi.updateEmployee(employeeId, employeeEntity).await()
            Pair(res.code(), res.body())
        }
        catch (t: Throwable) {
            Pair(handleThrowable(t), null)
        }
    }

    // Transaction Endpoint Functions

    suspend fun createTransaction(entity: TransactionCreateEntity): Int {
        return try {
            val body = RequestBody.create(JSON, entity.toString())
            val req = Request.Builder().url("${ApiURLs.BASE_URL}transaction/").post(body).build()
            val res = client.newCall(req).await()

            res.code()
        }
        catch (t: Throwable) {
            handleThrowable(t)
        }
    }

    // Helper Functions

    private fun handleThrowable(t: Throwable): Int {
        return when (t) {
            is IOException -> ErrorCodes.IO_EXCEPTION
            is SocketTimeoutException -> ErrorCodes.SOCKET_TIMEOUT_EXCEPTION
            is HttpException -> ErrorCodes.HTTP_EXCEPTION
            else -> ErrorCodes.OTHER_ERROR
        }
    }

    private suspend fun<T> Call<T>.await(): Response<T> = suspendCoroutine { cont ->
        enqueue(object: Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                cont.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                cont.resume(response)
            }
        })
    }

    private suspend fun okhttp3.Call.await(): okhttp3.Response = suspendCoroutine { cont ->
        enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                cont.resumeWithException(e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                cont.resume(response)
            }
        })
    }
}

fun handleError(context: Context, code: Int) {
    when (code) {
        HTTPResponseCodes.INTERNAL_SERVER_ERROR -> {
            val toast = Toast.makeText(context, context.getString(R.string.error_internal_server),
                    Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        HTTPResponseCodes.BAD_REQUEST -> {
            val toast = Toast.makeText(context, context.getString(R.string.error_io_exception),
                    Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        ErrorCodes.IO_EXCEPTION -> {
            val toast = Toast.makeText(context, context.getString(R.string.error_io_exception),
                    Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        ErrorCodes.SOCKET_TIMEOUT_EXCEPTION -> {
            val toast = Toast.makeText(context, context.getString(R.string.error_socket_timeout),
                    Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        ErrorCodes.HTTP_EXCEPTION -> {
            val toast = Toast.makeText(context, context.getString(R.string.error_io_exception),
                    Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        ErrorCodes.OTHER_ERROR -> {
            val toast = Toast.makeText(context, context.getString(R.string.error_io_exception),
                    Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}

private interface RegisterApiService {
    // Product Endpoints

    @GET("product/{id}")
    fun getProductById(@Path("id") id: String): Call<ProductEntity>

    @GET("product/lookup/{lookupCode}")
    fun getProductByLookupCode(@Path("lookupCode") lookupCode: String): Call<ProductEntity>

    @POST("product/")
    fun createProduct(@Body productEntity: ProductCreateEntity): Call<ProductEntity>

    @PUT("product/{id}")
    fun updateProduct(@Path("id") id: String, @Body productEntity: ProductEntity): Call<ProductEntity>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") id: String): Call<Int>

    // Employee Endpoints

    @GET("employee/")
    fun employeesExist(): Call<Int>

    @GET("employee/{id}")
    fun getEmployeeById(@Path("id") id: String): Call<EmployeeEntity>

    @GET("employee/lookup/{employeeId}")
    fun getEmployeeByEmployeeId(@Path("employeeId") employeeId: String): Call<EmployeeEntity>

    @POST("employee/")
    fun createEmployee(@Body employeeEntity: EmployeeCreateEntity): Call<EmployeeEntity>

    @POST("employee/login/")
    fun employeeLogin(@Body loginEntity: LoginEntity): Call<EmployeeEntity>

    @PUT("employee/{id}")
    fun updateEmployee(@Path("id") id: String, @Body employeeEntity: EmployeeEntity): Call<EmployeeEntity>

    @DELETE("employee/{id}")
    fun deleteEmployee(@Path("id") id: String): Call<Int>

    // Transaction Endpoints

    @POST("transaction/")
    fun createTransaction(@Body transactionEntity: TransactionCreateEntity): Call<Int>
}