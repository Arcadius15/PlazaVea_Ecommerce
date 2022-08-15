package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.TiendaApiRepository
import edu.pe.idat.pva.models.response.TiendaResponse

class TiendaProvider : ViewModel() {

    private var repository = TiendaApiRepository()

    fun getTiendas() : LiveData<ArrayList<TiendaResponse>> {
        return repository.getTiendas()
    }

}