package edu.pe.idat.pva.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.pva.activities.DetalleOrdenActivity
import edu.pe.idat.pva.adapter.HistorialAdapter
import edu.pe.idat.pva.databinding.FragmentHistorialBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.response.OrdenResponse
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class HistorialFragment : Fragment(), HistorialAdapter.IHistorialAdapter, AdapterView.OnItemSelectedListener {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var ordenProvider: OrdenProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    private var primeraCarga = true
    private var pagina = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)

        ordenProvider = ViewModelProvider(requireActivity())[OrdenProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(requireActivity())[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(requireActivity())[TokenRoomProvider::class.java]

        binding.tvMensajeSinCompras.visibility = View.GONE
        binding.llPaginaHistorial.visibility = View.GONE

        binding.rvHistorial.setHasFixedSize(true)
        binding.rvHistorial.layoutManager = LinearLayoutManager(requireActivity())

        getUserFromDB()

        return binding.root
    }

    private fun getOrdenes() {
        ordenProvider.getAllByCliente(usuario.idCliente, pagina,
                                        "Bearer ${token.token}").observe(viewLifecycleOwner){
                                            if (it != null){
                                                if (primeraCarga) {
                                                    val paginas = (1..it.totalPages).toList()
                                                    val adapterPaginas = ArrayAdapter(requireActivity(), R.layout.support_simple_spinner_dropdown_item,
                                                        paginas)
                                                    binding.spPaginaHistorial.adapter = adapterPaginas
                                                    binding.spPaginaHistorial.onItemSelectedListener = this
                                                    binding.llPaginaHistorial.visibility = View.VISIBLE
                                                }
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
                getOrdenes()
            }
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                parentFragmentManager.beginTransaction().detach(this).commitNow()
                parentFragmentManager.beginTransaction().attach(this).commitNow()
                primeraCarga = true
                pagina = 0
            } else {
                parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
            }
        }
    }

    override fun goDetail(ordenResponse: OrdenResponse) {
        launcher.launch(Intent(requireActivity(),DetalleOrdenActivity::class.java)
            .putExtra("orden",ordenResponse))
    }

    override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
        pagina = p0.getItemAtPosition(p2).toString().toInt() - 1
        primeraCarga = false
        getOrdenes()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(requireActivity(),"Seleccione una página", Toast.LENGTH_SHORT).show()
    }
}