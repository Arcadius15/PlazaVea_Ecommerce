package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.OrdenApiRepository
import edu.pe.idat.pva.models.OrdenHistorialRequest
import edu.pe.idat.pva.models.OrdenRequest
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.ResponseHttp

class OrdenProvider : ViewModel() {

    var ordenId: LiveData<String>
    var responseHttp: LiveData<ResponseHttp>

    private var repository = OrdenApiRepository()

    init {
        ordenId = repository.ordenId
        responseHttp = repository.responseHttp
    }

    fun registrarOrden(ordenRequest: OrdenRequest, token: String){
        ordenId = repository.registrarOrden(ordenRequest,token)
    }

    fun registrarHistorial(ordenHistorialRequest: OrdenHistorialRequest, token: String){
        responseHttp = repository.registrarHistorial(ordenHistorialRequest,token)
    }

    fun getOrden(idOrden: String, token: String): LiveData<OrdenResponse>{
        return repository.getOrden(idOrden,token)
    }
}