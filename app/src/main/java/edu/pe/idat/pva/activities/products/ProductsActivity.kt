package edu.pe.idat.pva.activities.products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ProductsAdapter
import edu.pe.idat.pva.databinding.ActivityMainBinding
import edu.pe.idat.pva.databinding.ActivityProductsBinding
import edu.pe.idat.pva.databinding.FragmentProductoBinding
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.models.ProductosCategoriaResponse
import edu.pe.idat.pva.models.User
import edu.pe.idat.pva.providers.ProductsProvider
import edu.pe.idat.pva.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {

    val TAG = "ProductsActivity"

    private lateinit var productsProvider: ProductsProvider

    private lateinit var binding: ActivityProductsBinding

    var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    var user: User? = null

    private lateinit var products: List<Producto>
    var sharedPref: SharedPref? = null

    var idSubcategoria: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productsProvider = ViewModelProvider(this).get(ProductsProvider::class.java)

        idSubcategoria = intent.getIntExtra("idSubcategoria", 0).toString()
        Log.d(TAG, idSubcategoria!!)

        sharedPref = SharedPref(this)
        // productsProvider = ProductsProvider(user?.sessionToken!!)
        recyclerViewProducts = findViewById(R.id.rvProducts)
        recyclerViewProducts?.setHasFixedSize(true)
        recyclerViewProducts?.layoutManager= GridLayoutManager(this, 2)

       getProducts(idSubcategoria!!)

        productsProvider.productResponse.observe(this){
            findProductById(it!!)
        }

        getUserFromSession()

    }

    private fun findProductById(productResponse: ProductosCategoriaResponse) {
        if(productResponse.content.isNotEmpty()){
            Toast.makeText(
                applicationContext,
                "El primer producto es: ${productResponse.content[0].nombre}",
                Toast.LENGTH_LONG
            ).show()
            products = productResponse.content
            adapter = ProductsAdapter(this, ArrayList(products))
            recyclerViewProducts?.adapter = adapter
        }
        else {
            Log.d(TAG, "Error: Hubo un problema con la consulta")
            Toast.makeText(this, "Error: Hubo un problema con la consulta", Toast.LENGTH_LONG).show()
        }
    }

    private fun getProducts(idSubcategoria: String){
        productsProvider.findByCategory(idSubcategoria)
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }
}