package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import edu.pe.idat.pva.R
import edu.pe.idat.pva.api.UsuarioApi
import edu.pe.idat.pva.databinding.ActivityRegisterBinding
import edu.pe.idat.pva.models.Cliente
import edu.pe.idat.pva.models.Rol
import edu.pe.idat.pva.models.UsuarioRequest
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToMain.setOnClickListener (this)
        binding.btnRegistrar.setOnClickListener(this)
    }

    private fun goToLogin(){
        this.finish()
    }

    private fun toast(){
        Toast.makeText(this, "hizo click", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View) {
        if (p0 is ImageView){
            goToLogin()
        } else if (p0.id == R.id.btn_registrar){
            registrarUsuario()
        }
    }

    private fun registrarUsuario(){
        val cliente = Cliente(
            binding.edtApellidos.text.toString(),
            binding.edtDNI.text.toString().toInt(),
            binding.edtFechaNacimiento.text.toString(),
            binding.edtNombre.text.toString(),
            binding.edtPhone.text.toString().toInt()
        )

        val lstRol = ArrayList<Rol>()

        val rol = Rol("cliente")
        lstRol.add(rol)

        val usuarioRequest = UsuarioRequest(
            cliente,
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString(),
            lstRol
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://plazavea-webservice.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val usuarioApi : UsuarioApi = retrofit.create(UsuarioApi::class.java)

        val call : Call<UsuarioRequest> = usuarioApi.registrar(usuarioRequest)
        call.enqueue(object : Callback<UsuarioRequest>{
            override fun onFailure(call: Call<UsuarioRequest>, t: Throwable) {
                Toast.makeText(applicationContext,"ERROR! Revisa la consola.",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UsuarioRequest>, response: Response<UsuarioRequest>){
                if (response.body() != null) {
                    val usuarioData = response.body()!!

                    Toast.makeText(
                        applicationContext,
                        "${usuarioData.email} Registrado con éxito. Recuerda confirmar tu correo.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "ERROR! Hubo un problema con el WS.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}