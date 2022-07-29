package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.TarjetaApiRepository
import edu.pe.idat.pva.models.ResponseHttp

class TarjetaProvider : ViewModel() {

    var responseHttp: LiveData<ResponseHttp>

    private var repository = TarjetaApiRepository()

    init {
        responseHttp = repository.responseHttp
    }

}