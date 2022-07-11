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
import edu.pe.idat.pva.utils.SharedPref

class ProductsDetailActivity : AppCompatActivity() {

    val TAG = "ProductsDetail"
    var product: Product? = null
    val gson = Gson()

    var imageView1: ImageView? = null
    var textViewName: TextView? = null
    var textViewDescription: TextView? = null
    var textViewPrice: TextView? = null
    var textViewContador: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonAdd: Button? = null
    var selectProduct = ArrayList<Product>()

    var contador = 1
    var productPrice = 0.0
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_detail)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        imageView1 = findViewById(R.id.iv_productImage)
        textViewName = findViewById(R.id.tv_productName)
        textViewDescription = findViewById(R.id.tv_productDescription)
        textViewPrice = findViewById(R.id.tv_price)
        textViewContador = findViewById(R.id.tv_contador)
        imageViewAdd = findViewById(R.id.iv_add)
        imageViewRemove = findViewById(R.id.iv_remove)
        buttonAdd = findViewById(R.id.btn_add_product)


        Glide.with(this@ProductsDetailActivity).load(product?.image1).into(this.imageView1!!)
        textViewName?.text = product?.name
        textViewDescription?.text = product?.description
        textViewPrice?.text = "S/${product?.price}"
        imageViewAdd?.setOnClickListener{addItem()}
        imageViewRemove?.setOnClickListener{removeItem()}
        buttonAdd?.setOnClickListener{ addToBag()}

        getProductsFromSharedPref()

    }

    private fun addToBag(){
        val index = getIndexOf(product?.id!!) //indice del producto si existe

        if(index == -1){ //No existe en sharedPref
            if(product?.quantity == 0){
                product?.quantity = 1
            }
            selectProduct.add(product!!)
        }
        else{// ya existe el producto - debemos editar cantidad
            selectProduct[index].quantity = contador

        }

        sharedPref?.save("orden", selectProduct)
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show()
    }

    private fun getProductsFromSharedPref(){
        if(!sharedPref?.getData("orden").isNullOrBlank()){
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectProduct = gson.fromJson(sharedPref?.getData("orden"), type)
            val index = getIndexOf(product?.id!!)

            if(index != -1){
                product?.quantity = selectProduct[index].quantity
                textViewContador?.text = "${product?.quantity}"
                productPrice = product?.price!! * product?.quantity!!
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
            if(p.id == idProduct){
                return ps
            }
            ps++
        }

        return -1
    }

    private fun addItem(){
        contador = contador + 1
        productPrice= product?.price!! * contador
        product?.quantity = contador
        textViewContador?.text= "${product?.quantity}"
        textViewPrice?.text = "${productPrice}"
    }


    private fun removeItem(){
        if(contador > 1){
            contador--
            productPrice= product?.price!! * contador
            product?.quantity = contador
            textViewContador?.text= "${product?.quantity}"
            textViewPrice?.text = "${productPrice}"
        }

    }
}