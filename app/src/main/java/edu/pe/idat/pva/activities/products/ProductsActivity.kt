package edu.pe.idat.pva.activities.products

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ProductsAdapter
import edu.pe.idat.pva.databinding.ActivityProductsBinding
import edu.pe.idat.pva.models.response.Producto
import edu.pe.idat.pva.models.response.ProductosCategoriaResponse
import edu.pe.idat.pva.providers.ProductsProvider

class ProductsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "ProductsActivity"

    private lateinit var productsProvider: ProductsProvider

    private lateinit var binding: ActivityProductsBinding

    private var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    private lateinit var products: List<Producto>

    private var idSubcategoria: String? = null

    private var primeraCarga = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productsProvider = ViewModelProvider(this)[ProductsProvider::class.java]

        binding.llPaginaProductos.visibility = View.GONE

        idSubcategoria = intent.getIntExtra("idSubcategoria", 0).toString()

        recyclerViewProducts = findViewById(R.id.rvProducts)
        recyclerViewProducts?.setHasFixedSize(true)
        recyclerViewProducts?.layoutManager= GridLayoutManager(this, 2)

        getProducts(idSubcategoria!!)

        productsProvider.productResponse.observe(this){
            findProductById(it!!)
            binding.progressbar.visibility = View.GONE
        }
    }

    private fun findProductById(productResponse: ProductosCategoriaResponse) {
        if(productResponse.content.isNotEmpty()){
            if (primeraCarga) {
                val paginas = (1..productResponse.totalPages).toList()
                val adapterPaginas = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    paginas)
                binding.spPaginaProductos.adapter = adapterPaginas
                binding.spPaginaProductos.onItemSelectedListener = this
                binding.llPaginaProductos.visibility = View.VISIBLE
            }

            products = productResponse.content
            adapter = ProductsAdapter(this, ArrayList(products))
            recyclerViewProducts?.adapter = adapter
        }
        else {
            Log.d(TAG, "Error: Hubo un problema con la consulta")
        }
    }

    private fun getProducts(idSubcategoria: String){
        productsProvider.findByCategory(idSubcategoria, 0)
    }

    override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
        val pagseleccionada = p0.getItemAtPosition(p2).toString().toInt() - 1
        getPagedProducts(idSubcategoria!!, pagseleccionada)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this,"Seleccione una página",Toast.LENGTH_SHORT).show()
    }

    private fun getPagedProducts(idSubcategoria: String, pagseleccionada: Int) {
        primeraCarga = false
        productsProvider.findByCategory(idSubcategoria, pagseleccionada)
    }

}