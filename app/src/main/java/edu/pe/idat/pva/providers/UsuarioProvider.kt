package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.api.ApiRoutes
import edu.pe.idat.pva.apirep.UsuarioApiRepository
import edu.pe.idat.pva.models.UsuarioRequest
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.routes.UsuarioRoutes
import retrofit2.Call

class UsuarioProvider : ViewModel() {
    var usuarioResponse: LiveData<UsuarioResponse>

    private var repository = UsuarioApiRepository()

    init {
        usuarioResponse = repository.usuarioResponse
    }

    fun registrar(usuarioRequest: UsuarioRequest){
        usuarioResponse = repository.registrar(usuarioRequest)
    }
}