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
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DetalleTarjetaActivity
import edu.pe.idat.pva.activities.ListRucActivity
import edu.pe.idat.pva.activities.RegTarjetaActivity
import edu.pe.idat.pva.adapter.TarjetaAdapter
import edu.pe.idat.pva.databinding.FragmentTarjetasBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.TarjetaResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider
import edu.pe.idat.pva.utils.SharedPref

class TarjetasFragment : Fragment(), View.OnClickListener, TarjetaAdapter.ITarjetaAdapter {

    private var _binding: FragmentTarjetasBinding? = null
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
        _binding = FragmentTarjetasBinding.inflate(inflater, container, false)

        clienteProvider = ViewModelProvider(requireActivity())[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(requireActivity())[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(requireActivity())[TokenRoomProvider::class.java]

        binding.tvMensajeSinTarjetas.visibility = View.GONE

        binding.rvTarjetas.setHasFixedSize(true)
        binding.rvTarjetas.layoutManager = LinearLayoutManager(requireActivity())

        getUserFromDB()

        binding.btnGoRegTarjeta.setOnClickListener(this)
        binding.btnGoListRuc.setOnClickListener(this)

        return binding.root
    }

    private fun getTarjetas() {
        clienteProvider.listarTarjetas(usuario.idCliente,
            "Bearer ${token.token}").observe(viewLifecycleOwner){
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
                getTarjetas()
            }
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
                parentFragmentManager.beginTransaction().detach(this).commitNow()
                parentFragmentManager.beginTransaction().attach(this).commitNow()
            } else {
                parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
            }
        }
    }

    override fun goToTarjetaDet(tarjetaResponse: TarjetaResponse) {
        launcher.launch(Intent(requireActivity(),DetalleTarjetaActivity::class.java)
            .putExtra("tarjeta",tarjetaResponse))
    }
}