package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.DireccionApiRepository
import edu.pe.idat.pva.models.request.DireccionPatchRequest
import edu.pe.idat.pva.models.response.ResponseHttp

class DireccionProvider : ViewModel()  {

    var responseHttp: LiveData<ResponseHttp>

    private var repository = DireccionApiRepository()

    init{
        responseHttp = repository.responseHttp
    }

    fun borrarDireccion(idDireccion: Int, token: String){
        responseHttp = repository.borrarDireccion(idDireccion,token)
    }

    fun editarDireccion(idDireccion: Int, direccionPatchRequest: DireccionPatchRequest, token: String){
        responseHttp = repository.editarDireccion(idDireccion, direccionPatchRequest, token)
    }
}