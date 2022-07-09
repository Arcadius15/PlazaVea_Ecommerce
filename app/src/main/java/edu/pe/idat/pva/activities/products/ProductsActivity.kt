package edu.pe.idat.pva.activities.products

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ProductsAdapter
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.User
import edu.pe.idat.pva.providers.ProductsProvider
import edu.pe.idat.pva.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {

    val TAG = "ProductsActivity"
    var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    var user: User? = null
    var productsProvider: ProductsProvider? = null
    var products: ArrayList<Product> = ArrayList()
    var sharedPref: SharedPref? = null

    var idCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        idCategory = intent.getStringExtra("idCategory")

        sharedPref = SharedPref(this)

        getUserFromSession()

        productsProvider = ProductsProvider(user?.sessionToken!!)
        recyclerViewProducts = findViewById(R.id.rvProducts)
        recyclerViewProducts?.layoutManager= GridLayoutManager(this, 2)

        getProducts()

    }

    private fun getProducts(){
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object : Callback<ArrayList<Product>>{
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if(response.body() != null){
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ProductsActivity, products)
                    recyclerViewProducts?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(this@ProductsActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }
}