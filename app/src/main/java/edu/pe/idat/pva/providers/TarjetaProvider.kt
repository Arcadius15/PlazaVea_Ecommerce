package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.TarjetaApiRepository
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.models.request.TarjetaPatchRequest

class TarjetaProvider : ViewModel() {

    var responseHttp: LiveData<ResponseHttp>

    private var repository = TarjetaApiRepository()

    init {
        responseHttp = repository.responseHttp
    }

    fun editarTarjeta(idTarjeta: Int, tarjetaPatchRequest: TarjetaPatchRequest, token: String){
        responseHttp = repository.editarTarjeta(idTarjeta, tarjetaPatchRequest, token)
    }

    fun borrarTarjeta(idTarjeta: Int, token: String){
        responseHttp = repository.borrarTarjeta(idTarjeta,token)
    }
}