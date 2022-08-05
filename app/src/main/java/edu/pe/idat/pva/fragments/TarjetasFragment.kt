package edu.pe.idat.pva.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DetalleTarjetaActivity
import edu.pe.idat.pva.activities.ListRucActivity
import edu.pe.idat.pva.activities.RegTarjetaActivity
import edu.pe.idat.pva.adapter.TarjetaAdapter
import edu.pe.idat.pva.databinding.FragmentTarjetasBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.TarjetaResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref

class TarjetasFragment : Fragment(), View.OnClickListener, TarjetaAdapter.ITarjetaAdapter {

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
        binding.btnGoListRuc.setOnClickListener(this)

        return binding.root
    }

    private fun getTarjetas() {
        clienteProvider.listarTarjetas(getUserFromSession()!!.cliente.idCliente,
            "Bearer ${getTokenFromSession()!!.token}").observe(viewLifecycleOwner){
                if (it != null){
                    binding.rvTarjetas.adapter = TarjetaAdapter(ArrayList(it.sortedWith(compareBy{ tr ->
                        tr.idTarjeta
                    })),this)
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
            R.id.btnGoRegTarjeta -> launcher.launch(Intent(requireActivity(),RegTarjetaActivity::class.java)
                .putExtra("origen","lista"))
            R.id.btnGoListRuc -> startActivity(Intent(requireActivity(),ListRucActivity::class.java))
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                parentFragmentManager.beginTransaction().detach(this).commitNow();
                parentFragmentManager.beginTransaction().attach(this).commitNow();
            } else {
                parentFragmentManager.beginTransaction().detach(this).attach(this).commit();
            }
        }
    }

    override fun goToTarjetaDet(tarjetaResponse: TarjetaResponse) {
        launcher.launch(Intent(requireActivity(),DetalleTarjetaActivity::class.java)
            .putExtra("tarjeta",tarjetaResponse))
    }
}