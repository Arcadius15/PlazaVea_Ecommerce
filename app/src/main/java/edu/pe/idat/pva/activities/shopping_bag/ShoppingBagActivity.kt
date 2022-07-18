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
import edu.pe.idat.pva.databinding.ActivityShoppingBagBinding
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref

class ShoppingBagActivity : AppCompatActivity() {

    val TAG = "ShoppingBag"
    private lateinit var binding: ActivityShoppingBagBinding

    var adapter: ShoppingBagAdapter? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectProduct = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        binding.rvShoppingBag.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                if (selectProduct.size > 0) {
                    setearPrecios(selectProduct)
                } else {
                    binding.tvMonto.text = "S/0"
                    binding.tvIgv.text = "S/0"
                    binding.tvTotal.text = "S/0"
                }
            }
        })
        Log.d(TAG, selectProduct.toString())
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("shopBag").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Producto>>() {}.type
            selectProduct = gson.fromJson(sharedPref?.getData("shopBag"), type)
            adapter = ShoppingBagAdapter(this, selectProduct)
            binding.rvShoppingBag.adapter = adapter

            setearPrecios(selectProduct)
        } else {
            binding.tvMonto.text = "S/0"
            binding.tvIgv.text = "S/0"
            binding.tvTotal.text = "S/0"
        }

    }

    private fun setearPrecios(lst: ArrayList<Producto>) {
        var monto = 0.0
        var igv = 0.0
        var total = 0.0

        lst.forEach{
            monto += it.precioRegular * it.quantity!!
            igv += monto * 0.18
            total += monto + igv

            binding.tvMonto.text = "S/${String.format("%.2f",monto)}"
            binding.tvIgv.text = "S/${String.format("%.2f",igv)}"
            binding.tvTotal.text = "S/${String.format("%.2f",total)}"
        }
    }
}