package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.UsuarioApiRepository
import edu.pe.idat.pva.models.LoginRequest
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.UsuarioRequest
import edu.pe.idat.pva.models.UsuarioResponse

class UsuarioProvider : ViewModel() {
    var usuarioResponse: LiveData<UsuarioResponse>
    var loginResponse: LiveData<LoginResponse>

    private var repository = UsuarioApiRepository()

    init {
        usuarioResponse = repository.usuarioResponse
        loginResponse = repository.loginResponse
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
}