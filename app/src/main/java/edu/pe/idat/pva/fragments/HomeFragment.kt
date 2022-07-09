package edu.pe.idat.pva.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.CategoriaAdapter
import edu.pe.idat.pva.models.Categoria
import edu.pe.idat.pva.models.User
import edu.pe.idat.pva.providers.CategoriaProvider
import edu.pe.idat.pva.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    val TAG = "HomeFragment"
    var myView: View? = null
    var rvCategorias: RecyclerView? = null
    var categoriaProvider: CategoriaProvider? = null
    var adapter: CategoriaAdapter? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Categoria>()
    var manager = GridLayoutManager(context, 2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_home, container, false)
        rvCategorias = myView?.findViewById(R.id.rvCategorias)
        rvCategorias?.setHasFixedSize(true)
        rvCategorias?.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriaProvider = CategoriaProvider(user?.sessionToken!!)

        rvCategorias?.setLayoutManager(manager)
        getCagories()
        return myView
    }

    private fun getCagories(){
        categoriaProvider?.getAll()?.enqueue(object: Callback<ArrayList<Categoria>> {
            override fun onResponse(
                call: Call<ArrayList<Categoria>>,
                response: Response<ArrayList<Categoria>>
            ) {
                if(response.body() != null) {
                    categories = response.body()!!
                    adapter = CategoriaAdapter(requireActivity(), categories)
                    rvCategorias?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Categoria>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }


}