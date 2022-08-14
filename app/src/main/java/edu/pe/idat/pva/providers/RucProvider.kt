package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.RucApiRepository
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.models.request.RucPatchRequest

class RucProvider : ViewModel() {

    var responseHttp: LiveData<ResponseHttp>

    private var repository = RucApiRepository()

    init {
        responseHttp = repository.responseHttp
    }

    fun editarRuc(idRuc: Int, rucPatchRequest: RucPatchRequest, token: String){
        responseHttp = repository.editarRuc(idRuc, rucPatchRequest, token)
    }

    fun borrarRuc(idRuc: Int, token: String){
        responseHttp = repository.borrarRuc(idRuc,token)
    }
}