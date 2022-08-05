package edu.pe.idat.pva.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DetalleOrdenActivity
import edu.pe.idat.pva.adapter.HistorialAdapter
import edu.pe.idat.pva.databinding.FragmentHistorialBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.providers.ProductsProvider
import edu.pe.idat.pva.utils.SharedPref

class HistorialFragment : Fragment(), HistorialAdapter.IHistorialAdapter {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var ordenProvider: OrdenProvider
    private lateinit var productsProvider: ProductsProvider
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)

        ordenProvider = ViewModelProvider(requireActivity())[OrdenProvider::class.java]
        productsProvider = ViewModelProvider(requireActivity())[ProductsProvider::class.java]

        binding.tvMensajeSinCompras.visibility = View.GONE

        binding.rvHistorial.setHasFixedSize(true)
        binding.rvHistorial.layoutManager = LinearLayoutManager(requireActivity())

        sharedPref = SharedPref(requireActivity())

        getOrdenes()

        return binding.root
    }

    private fun getOrdenes() {
        ordenProvider.getAllByCliente(getUserFromSession()!!.cliente.idCliente,
                                        "Bearer ${getTokenFromSession()!!.token}").observe(viewLifecycleOwner){
                                            if (it != null){
                                                binding.rvHistorial.adapter = HistorialAdapter(ArrayList(it.content.sortedWith(
                                                    compareBy{ or ->
                                                        or.idOrden
                                                    })),this)
                                                binding.progressbarHistorial.visibility = View.GONE
                                            } else {
                                                binding.progressbarHistorial.visibility = View.GONE
                                                binding.tvMensajeSinCompras.visibility = View.VISIBLE
                                            }
        }
    }

    private fun getUserFromSession(): UsuarioResponse?{
        val gson = Gson()

        return if(sharedPref.getData("user").isNullOrBlank()){
            null
        } else {
            val user = gson.fromJson(sharedPref.getData("user"), UsuarioResponse::class.java)
            user
        }
    }

    private fun getTokenFromSession(): LoginResponse?{
        val gson = Gson()

        return if(sharedPref.getData("token").isNullOrBlank()){
            null
        } else {
            val token = gson.fromJson(sharedPref.getData("token"), LoginResponse::class.java)
            token
        }
    }

    override fun goDetail(ordenResponse: OrdenResponse) {
        startActivity(Intent(requireActivity(),DetalleOrdenActivity::class.java)
            .putExtra("orden",ordenResponse))
    }
}