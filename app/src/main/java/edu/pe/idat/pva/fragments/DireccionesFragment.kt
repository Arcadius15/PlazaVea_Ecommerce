package edu.pe.idat.pva.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DireccionDetalleActivity
import edu.pe.idat.pva.activities.DireccionRegistroActivity
import edu.pe.idat.pva.adapter.DireccionAdapter
import edu.pe.idat.pva.databinding.FragmentDireccionesBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.DireccionResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class DireccionesFragment : Fragment(), View.OnClickListener, DireccionAdapter.IDireccionAdapter {

    private var _binding: FragmentDireccionesBinding? = null
    private val binding get() = _binding!!

    private lateinit var clienteProvider: ClienteProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDireccionesBinding.inflate(inflater, container, false)

        clienteProvider = ViewModelProvider(requireActivity())[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(requireActivity())[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(requireActivity())[TokenRoomProvider::class.java]

        binding.tvMensajeSinDirecciones.visibility = View.GONE

        binding.rvDirecciones.setHasFixedSize(true)
        binding.rvDirecciones.layoutManager = LinearLayoutManager(requireActivity())

        getUserFromDB()

        binding.btnGoRegDireccion.setOnClickListener(this)

        return binding.root
    }

    private fun getDirecciones() {
        clienteProvider.listarDirecciones(usuario.idCliente,
            "Bearer ${token.token}").observe(viewLifecycleOwner){
            if (it != null){
                val adapter = DireccionAdapter(ArrayList(it.sortedWith(compareBy{ dr ->
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

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(requireActivity()){
            ue -> ue?.let {
                usuario = ue
                getTokenFromDB()
            }
        }
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(requireActivity()){
            te -> te?.let {
                token = te
                getDirecciones()
            }
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                parentFragmentManager.beginTransaction().detach(this).commitNow()
                parentFragmentManager.beginTransaction().attach(this).commitNow()
            } else {
                parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
            }
        }
    }

    override fun goToMap(direccionResponse: DireccionResponse) {
        launcher.launch(Intent(requireActivity(), DireccionDetalleActivity::class.java)
            .putExtra("direccion",direccionResponse))
    }
}