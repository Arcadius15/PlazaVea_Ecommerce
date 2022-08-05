package edu.pe.idat.pva.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DireccionDetalleActivity
import edu.pe.idat.pva.activities.DireccionRegistroActivity
import edu.pe.idat.pva.activities.RegTarjetaActivity
import edu.pe.idat.pva.adapter.DireccionAdapter
import edu.pe.idat.pva.databinding.FragmentDireccionesBinding
import edu.pe.idat.pva.models.DireccionResponse
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref

class DireccionesFragment : Fragment(), View.OnClickListener, DireccionAdapter.IDireccionAdapter {

    private var _binding: FragmentDireccionesBinding? = null
    private val binding get() = _binding!!

    private lateinit var clienteProvider: ClienteProvider
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDireccionesBinding.inflate(inflater, container, false)

        clienteProvider = ViewModelProvider(requireActivity())[ClienteProvider::class.java]

        binding.tvMensajeSinDirecciones.visibility = View.GONE

        binding.rvDirecciones.setHasFixedSize(true)
        binding.rvDirecciones.layoutManager = LinearLayoutManager(requireActivity())

        sharedPref = SharedPref(requireActivity())

        getDirecciones()

        binding.btnGoRegDireccion.setOnClickListener(this)

        return binding.root
    }

    private fun getDirecciones() {
        clienteProvider.listarDirecciones(getUserFromSession()!!.cliente.idCliente,
            "Bearer ${getTokenFromSession()!!.token}").observe(viewLifecycleOwner){
            if (it != null){
                var adapter = DireccionAdapter(ArrayList(it.sortedWith(compareBy{ dr ->
                    dr.idDireccion
                })),this)
                adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
                    override fun onChanged() {
                        if (it.size > 0) {
                            binding.tvMensajeSinDirecciones.visibility = View.VISIBLE
                        }
                    }
                })
                binding.rvDirecciones.adapter = adapter
                binding.progressbarDirecciones.visibility = View.GONE
            } else {
                binding.progressbarDirecciones.visibility = View.GONE
                binding.tvMensajeSinDirecciones.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnGoRegDireccion -> launcher.launch(Intent(requireActivity(), DireccionRegistroActivity::class.java)
                .putExtra("origen","lista"))
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

    override fun goToMap(direccionResponse: DireccionResponse) {
        launcher.launch(Intent(requireActivity(), DireccionDetalleActivity::class.java)
            .putExtra("direccion",direccionResponse))
    }
}