package edu.pe.idat.pva.activities.products.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.models.Product

class ProductsDetailActivity : AppCompatActivity() {

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

    var contador = 1
    var productPrice = 0.0

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


//        Glide.with(this@ProductsDetailActivity).load(product?.image1).into(this.imageView1!!)
//        textViewName?.text = product?.name
//        textViewDescription?.text = product?.description
//        textViewPrice?.text = "S/${product?.price}"
//        imageViewAdd?.setOnClickListener{addItem()}
//        imageViewRemove?.setOnClickListener{removeItem()}

    }

    private fun addItem(){
//        contador = contador + 1
//        productPrice= product?.price!! * contador
//        product?.quantity = contador
//        textViewContador?.text= "${product?.quantity}"
//        textViewPrice?.text = "${productPrice}"
    }


    private fun removeItem(){
//        if(contador > 1){
//            contador--
//            productPrice= product?.price!! * contador
//            product?.quantity = contador
//            textViewContador?.text= "${product?.quantity}"
//            textViewPrice?.text = "${productPrice}"
//        }

    }
}