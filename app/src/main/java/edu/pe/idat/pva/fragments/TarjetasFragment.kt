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
import edu.pe.idat.pva.activities.RegTarjetaActivity
import edu.pe.idat.pva.adapter.TarjetaAdapter
import edu.pe.idat.pva.databinding.FragmentTarjetasBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref

class TarjetasFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTarjetasBinding? = null
    private val binding get() = _binding!!

    private lateinit var clienteProvider: ClienteProvider
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTarjetasBinding.inflate(inflater, container, false)

        clienteProvider = ViewModelProvider(requireActivity())[ClienteProvider::class.java]

        binding.tvMensajeSinTarjetas.visibility = View.GONE

        binding.rvTarjetas.setHasFixedSize(true)
        binding.rvTarjetas.layoutManager = LinearLayoutManager(requireActivity())

        sharedPref = SharedPref(requireActivity())

        getTarjetas()

        binding.btnGoRegTarjeta.setOnClickListener(this)

        return binding.root
    }

    private fun getTarjetas() {
        clienteProvider.listarTarjetas(getUserFromSession()!!.cliente.idCliente,
            "Bearer ${getTokenFromSession()!!.token}").observe(viewLifecycleOwner){
                if (it != null){
                    binding.rvTarjetas.adapter = TarjetaAdapter(it)
                    binding.progressbarTarjetas.visibility = View.GONE
                } else {
                    binding.progressbarTarjetas.visibility = View.GONE
                    binding.tvMensajeSinTarjetas.visibility = View.VISIBLE
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

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnGoRegTarjeta -> startActivity(Intent(requireActivity(),RegTarjetaActivity::class.java)
                .putExtra("origen","lista"))
        }
    }

}