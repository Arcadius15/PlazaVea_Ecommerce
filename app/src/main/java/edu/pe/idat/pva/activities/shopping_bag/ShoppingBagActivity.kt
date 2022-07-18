package edu.pe.idat.pva.activities.shopping_bag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ShoppingBagAdapter
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref

class ShoppingBagActivity : AppCompatActivity() {

    val TAG = "ShoppingBag"
    var recyclerViewShoppingBag: RecyclerView? = null
    var  textViewTotal: TextView? = null
    var buttonContinuar: Button? = null

    var adapter: ShoppingBagAdapter? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectProduct = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_bag)

        sharedPref = SharedPref(this)

        recyclerViewShoppingBag = findViewById(R.id.rvShoppingBag)
        textViewTotal = findViewById(R.id.tv_total)
        buttonContinuar = findViewById(R.id.btn_continuar)

        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()
        Log.d(TAG, selectProduct.toString())

    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("shopBag").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Producto>>() {}.type
            selectProduct = gson.fromJson(sharedPref?.getData("shopBag"), type)
            adapter = ShoppingBagAdapter(this, selectProduct)
            recyclerViewShoppingBag?.adapter = adapter

        }

    }
}