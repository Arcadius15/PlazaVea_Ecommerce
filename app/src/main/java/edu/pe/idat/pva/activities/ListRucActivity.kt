package edu.pe.idat.pva.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.RucAdapter
import edu.pe.idat.pva.databinding.ActivityListRucBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.RucResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class ListRucActivity : AppCompatActivity(), View.OnClickListener, RucAdapter.IRucAdapter {

    private lateinit var binding: ActivityListRucBinding

    private lateinit var clienteProvider: ClienteProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRucBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        binding.tvMensajeSinRuc.visibility = View.GONE

        binding.rvRucs.setHasFixedSize(true)
        binding.rvRucs.layoutManager = LinearLayoutManager(this)

        getUserFromDB()

        binding.btnGoListTarjetas.setOnClickListener(this)
        binding.btnGoRegRuc.setOnClickListener(this)
    }

    private fun getRucs() {
        clienteProvider.listarRuc(usuario.idCliente,
                                  "Bearer " + token.token)
            .observe(this){
                if (it != null){
                    binding.rvRucs.adapter = RucAdapter(ArrayList(it.sortedWith(compareBy { rr ->
                        rr.idRuc
                    })),this)
                    binding.progressbarRuc.visibility = View.GONE
                } else {
                    binding.progressbarRuc.visibility = View.GONE
                    binding.tvMensajeSinRuc.visibility = View.VISIBLE
                }
            }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnGoListTarjetas -> finish()
            R.id.btnGoRegRuc -> launcher.launch(Intent(this,RegRucActivity::class.java)
                .putExtra("origen","lista"))
        }
    }

    override fun goToRucDetail(rucResponse: RucResponse) {
        launcher.launch(Intent(applicationContext,DetalleRucActivity::class.java)
            .putExtra("ruc",rucResponse))
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(this){
            usuario = it
            getTokenFromDB()
        }
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(this){
            token = it
            getRucs()
        }
    }
}