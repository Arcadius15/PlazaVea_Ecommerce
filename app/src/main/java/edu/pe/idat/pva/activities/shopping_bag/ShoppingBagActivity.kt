package edu.pe.idat.pva.activities.shopping_bag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.HomeActivity
import edu.pe.idat.pva.activities.pasarela.ResumenActivity
import edu.pe.idat.pva.adapter.ShoppingBagAdapter
import edu.pe.idat.pva.databinding.ActivityShoppingBagBinding
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref

class ShoppingBagActivity : AppCompatActivity(), View.OnClickListener {

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

        binding.btnContinuar.setOnClickListener(this)
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("shopBag").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Producto>>() {}.type
            selectProduct = gson.fromJson(sharedPref?.getData("shopBag"), type)
            adapter = ShoppingBagAdapter(this, selectProduct)
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

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_continuar -> procederPasarela()
        }
    }

    private fun procederPasarela() {
        if (selectProduct.size > 0){
            AlertDialog.Builder(this)
                .setTitle("Continuar a Pasarela")
                .setMessage("¿Seguro que desea proceder a pagar?")
                .setPositiveButton("Sí") { dialogInterface, i ->
                    startActivity(Intent(applicationContext, ResumenActivity::class.java))
                    dialogInterface.cancel()
                }
                .setNegativeButton("No"){ dialogInterface, i ->
                    dialogInterface.cancel()
                }
                .show()
        } else {
            Toast.makeText(
                applicationContext,
                "Debe agregar al menos un producto al carrito.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,HomeActivity::class.java))
    }
}