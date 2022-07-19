package edu.pe.idat.pva.activities.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityProductsDetailBinding
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref

class ProductsDetailActivity : AppCompatActivity() {

    val TAG = "ProductsDetail"
    var producto: Producto? = null
    val gson = Gson()

    private lateinit var binding: ActivityProductsDetailBinding

    var selectProduct = ArrayList<Producto>()



    var contador = 1
    var productPrice = 0.0
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        producto = gson.fromJson(intent.getStringExtra("producto"), Producto::class.java)

        Log.d(TAG, producto!!.toString())

        Glide.with(this@ProductsDetailActivity).load(producto?.imagenUrl).into(binding.ivProductImage)
        binding.tvProductName.text = producto?.nombre
        if(producto?.descripciones?.size!! > 0){
            var desc = ""
            producto?.descripciones!!.forEach{
                desc += it.descripcion + "\n"
            }
            binding.tvProductDescription.text = desc
        } else {
            binding.tvProductDescription.text = "Descripción de ejemplo para el producto ${producto?.nombre}."
        }
        binding.tvPrice.text = "S/${String.format("%.2f",producto?.precioRegular)}"
        binding.ivAdd.setOnClickListener{addItem()}
        binding.ivRemove.setOnClickListener{removeItem()}
        binding.btnAddProduct.setOnClickListener{ addToBag()}


        getProductsFromSharedPref()
    }

    private fun addToBag(){
        val index = getIndexOf(producto?.idProducto!!)

        if(index == -1){ //No existe en sharedPref
            if(binding.tvContador.text.toString().toInt() == 1){
                producto?.quantity = 1
            }
            selectProduct.add(producto!!)
        }
        else{
            selectProduct[index].quantity = contador
        }



        sharedPref?.save("shopBag", selectProduct)
        Log.d(TAG, selectProduct.toString())
        Toast.makeText(this, "Producto agregado  ${producto?.quantity}", Toast.LENGTH_LONG).show()
    }


    private fun getProductsFromSharedPref(){
        if(!sharedPref?.getData("shopBag").isNullOrBlank()){
            val type = object: TypeToken<ArrayList<Producto>>() {}.type
            selectProduct = gson.fromJson(sharedPref?.getData("shopBag"), type)
            val index = getIndexOf(producto?.idProducto!!)

            if(index != -1){
                producto?.quantity = selectProduct[index].quantity
                binding.tvContador.text = "${producto?.quantity}"
                contador = producto?.quantity!!.toInt()
                productPrice = producto?.precioRegular!! * producto?.quantity!!
                binding.tvPrice.text = "S/${String.format("%.2f",productPrice)}"
                binding.btnAddProduct.setText("Editar producto")
                binding.btnAddProduct.backgroundTintList= ColorStateList.valueOf(Color.RED)
            }

            for(p in selectProduct){
                Log.d(TAG, "Shared pref: $p")
            }
        }
    }

    private fun getIndexOf(idProduct: String): Int{
        var ps = 0
        for(p in selectProduct){
            if(p.idProducto == idProduct){
                return ps
            }
            ps++
        }

        return -1
    }

    private fun addItem(){
        contador++
        productPrice= producto?.precioRegular!! * contador
        producto?.quantity = contador
        binding.tvContador.text= "${producto?.quantity}"
        binding.tvPrice.text = "S/${String.format("%.2f",productPrice)}"
        if(producto?.quantity == 10){
            binding.ivAdd.isEnabled = false
        }
    }

    private fun removeItem(){
        if(contador > 1){
            contador--
            productPrice= producto?.precioRegular!! * contador
            producto?.quantity = contador
            binding.tvContador.text= "${producto?.quantity}"
            binding.tvPrice.text = "S/${String.format("%.2f",productPrice)}"
            if (!binding.ivAdd.isEnabled){
                binding.ivAdd.isEnabled = true
            }
        }
    }
}