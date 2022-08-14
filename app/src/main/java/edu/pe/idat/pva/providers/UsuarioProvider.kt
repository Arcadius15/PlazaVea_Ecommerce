package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.UsuarioApiRepository
import edu.pe.idat.pva.models.request.LoginRequest
import edu.pe.idat.pva.models.request.UsuarioPswRequest
import edu.pe.idat.pva.models.request.UsuarioRequest
import edu.pe.idat.pva.models.response.LoginResponse
import edu.pe.idat.pva.models.response.Mensaje
import edu.pe.idat.pva.models.response.UsuarioResponse

class UsuarioProvider : ViewModel() {
    var usuarioResponse: LiveData<UsuarioResponse>
    var loginResponse: LiveData<LoginResponse>
    var mensaje: LiveData<Mensaje>

    private var repository = UsuarioApiRepository()

    init {
        usuarioResponse = repository.usuarioResponse
        loginResponse = repository.loginResponse
        mensaje = repository.mensaje
    }

    fun registrar(usuarioRequest: UsuarioRequest){
        usuarioResponse = repository.registrar(usuarioRequest)
    }

    fun autenticar(loginRequest: LoginRequest){
        loginResponse = repository.autenticar(loginRequest)
    }

    fun getUserByEmail(loginRequest: LoginRequest, token: String){
        usuarioResponse = repository.getUserByEmail(loginRequest, token)
    }

    fun editPassword(usuarioPswRequest: UsuarioPswRequest){
        mensaje = repository.editPassword(usuarioPswRequest)
    }
}