package edu.pe.idat.pva.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.RucAdapter
import edu.pe.idat.pva.databinding.ActivityListRucBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.RucResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref

class ListRucActivity : AppCompatActivity(), View.OnClickListener, RucAdapter.IRucAdapter {

    private lateinit var binding: ActivityListRucBinding

    private lateinit var clienteProvider: ClienteProvider
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRucBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]

        binding.tvMensajeSinRuc.visibility = View.GONE

        binding.rvRucs.setHasFixedSize(true)
        binding.rvRucs.layoutManager = LinearLayoutManager(this)

        getRucs()

        binding.btnGoListTarjetas.setOnClickListener(this)
        binding.btnGoRegRuc.setOnClickListener(this)
    }

    private fun getRucs() {
        clienteProvider.listarRuc(getUserFromSession()!!.cliente.idCliente,
                                  "Bearer " + getTokenFromSession()!!.token)
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
}