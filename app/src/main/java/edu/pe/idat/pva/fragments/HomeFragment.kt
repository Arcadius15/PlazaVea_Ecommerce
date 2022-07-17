package edu.pe.idat.pva.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.CategoriaAdapter
import edu.pe.idat.pva.models.SubCategoriaResponse
import edu.pe.idat.pva.providers.CategoriaProvider
import edu.pe.idat.pva.utils.SharedPref


class HomeFragment : Fragment() {

    val TAG = "HomeFragment"
    var myView: View? = null
    var rvCategorias: RecyclerView? = null

    private lateinit var categoriaProvider: CategoriaProvider

    var adapter: CategoriaAdapter? = null
    var sharedPref: SharedPref? = null

    var categories = ArrayList<SubCategoriaResponse>()

    var manager = GridLayoutManager(context, 2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriaProvider = ViewModelProvider(this)
            .get(CategoriaProvider::class.java)

        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_home, container, false)
        rvCategorias = myView?.findViewById(R.id.rvCategorias)
        rvCategorias?.setHasFixedSize(true)
        rvCategorias?.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = SharedPref(requireActivity())

        rvCategorias?.setLayoutManager(manager)
        getCagories()

        categoriaProvider.subCategoriaResponse.observe(viewLifecycleOwner){
            obtenerCategoriasList(it!!)
        }

        return myView
    }

    private fun obtenerCategoriasList(subCategoriaResponse: ArrayList<SubCategoriaResponse>) {
        if (subCategoriaResponse != null){
            categories = subCategoriaResponse
            adapter = CategoriaAdapter(requireActivity(), categories)
            rvCategorias?.adapter = adapter
        } else {
            Toast.makeText(requireContext(), "Error: Hubo un problema con la consulta", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCagories(){
        categoriaProvider.getAll()
    }


}