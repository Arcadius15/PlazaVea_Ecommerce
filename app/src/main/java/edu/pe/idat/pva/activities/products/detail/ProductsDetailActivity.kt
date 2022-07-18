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
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref

class ProductsDetailActivity : AppCompatActivity() {

    val TAG = "ProductsDetail"
    var idProducto: Producto? = null
    val gson = Gson()

    var imageView1: ImageView? = null
    var textViewName: TextView? = null
    var textViewDescription: TextView? = null
    var textViewPrice: TextView? = null
    var textViewContador: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonAdd: Button? = null
    var selectProduct = ArrayList<Producto>()



    var contador = 1
    var productPrice = 0.0
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_detail)
        sharedPref = SharedPref(this)
       idProducto = gson.fromJson(intent.getStringExtra("idProducto"), Producto::class.java)

        Log.d(TAG, idProducto!!.toString())

        imageView1 = findViewById(R.id.iv_productImage)
        textViewName = findViewById(R.id.tv_productName)
        textViewDescription = findViewById(R.id.tv_productDescription)
        textViewPrice = findViewById(R.id.tv_price)
        textViewContador = findViewById(R.id.tv_contador)
        imageViewAdd = findViewById(R.id.iv_add)
        imageViewRemove = findViewById(R.id.iv_remove)
        buttonAdd = findViewById(R.id.btn_add_product)



         Glide.with(this@ProductsDetailActivity).load(idProducto?.imagenUrl).into(this.imageView1!!)
        textViewName?.text = idProducto?.nombre
        if(idProducto?.descripciones?.size!! > 0){
            textViewDescription?.text = idProducto?.descripciones?.get(0)?.descripcion
        } else {
            textViewDescription?.text = "Descripción de ejemplo para el producto ${idProducto?.nombre}."
        }
         textViewPrice?.text = "S/${idProducto?.precioRegular}"
        imageViewAdd?.setOnClickListener{addItem()}
        imageViewRemove?.setOnClickListener{removeItem()}
       buttonAdd?.setOnClickListener{ addToBag()}


        getProductsFromSharedPref()

    }

    private fun addToBag(){
        val index = getIndexOf(idProducto?.idProducto!!) //indice del producto si existe

        if(index == -1){ //No existe en sharedPref
            if(idProducto?.quantity == 0){
                idProducto?.quantity = 1
            }
            selectProduct.add(idProducto!!)
        }
        else{// ya existe el producto - debemos editar cantidad
            selectProduct[index].quantity = contador

        }

        sharedPref?.save("shopBag", selectProduct)
        Log.d(TAG, selectProduct.toString())
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show()
    }


    private fun getProductsFromSharedPref(){
        if(!sharedPref?.getData("shopBag").isNullOrBlank()){
            val type = object: TypeToken<ArrayList<Producto>>() {}.type
            selectProduct = gson.fromJson(sharedPref?.getData("shopBag"), type)
            val index = getIndexOf(idProducto?.idProducto!!)

            if(index != -1){
                idProducto?.quantity = selectProduct[index].quantity
                textViewContador?.text = "${idProducto?.quantity}"
                productPrice = idProducto?.precioRegular!! * idProducto?.quantity!!
                textViewPrice?.text = "${productPrice}"
                buttonAdd?.setText("Editar producto")
                buttonAdd?.backgroundTintList= ColorStateList.valueOf(Color.RED)
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
        contador = contador + 1
        productPrice= idProducto?.precioRegular!! * contador
        idProducto?.quantity = contador
        textViewContador?.text= "${idProducto?.quantity}"
        textViewPrice?.text = "S/${productPrice}"
        if(idProducto?.quantity == 10){
            imageViewAdd?.isEnabled = false
        }
    }


    private fun removeItem(){
        if(contador > 1){
            contador--
            productPrice= idProducto?.precioRegular!! * contador
            idProducto?.quantity = contador
            textViewContador?.text= "${idProducto?.quantity}"
            textViewPrice?.text = "S/${productPrice}"
            if (!imageViewAdd?.isEnabled!!){
                imageViewAdd?.isEnabled = true
            }
        }
    }
}