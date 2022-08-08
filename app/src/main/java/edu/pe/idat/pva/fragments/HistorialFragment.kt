package edu.pe.idat.pva.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.pva.activities.DetalleOrdenActivity
import edu.pe.idat.pva.adapter.HistorialAdapter
import edu.pe.idat.pva.databinding.FragmentHistorialBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class HistorialFragment : Fragment(), HistorialAdapter.IHistorialAdapter {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var ordenProvider: OrdenProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)

        ordenProvider = ViewModelProvider(requireActivity())[OrdenProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(requireActivity())[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(requireActivity())[TokenRoomProvider::class.java]

        binding.tvMensajeSinCompras.visibility = View.GONE

        binding.rvHistorial.setHasFixedSize(true)
        binding.rvHistorial.layoutManager = LinearLayoutManager(requireActivity())

        getUserFromDB()

        return binding.root
    }

    private fun getOrdenes() {
        ordenProvider.getAllByCliente(usuario.idCliente,
                                        "Bearer ${token.token}").observe(viewLifecycleOwner){
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

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(requireActivity()){
            usuario = it
            getTokenFromDB()
        }
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(requireActivity()){
            token = it
            getOrdenes()
        }
    }

    override fun goDetail(ordenResponse: OrdenResponse) {
        startActivity(Intent(requireActivity(),DetalleOrdenActivity::class.java)
            .putExtra("orden",ordenResponse))
    }
}