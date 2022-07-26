package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.ClienteApiRepository
import edu.pe.idat.pva.models.*

class ClienteProvider : ViewModel() {

    var responseHttp: LiveData<ResponseHttp>

    private var repository = ClienteApiRepository()

    init{
        responseHttp = repository.responseHttp
    }

    fun registrarDireccion(direccionRequest: DireccionRequest, token: String){
        responseHttp = repository.registrarDireccion(direccionRequest,token)
    }

    fun registrarRuc(rucRequest: RucRequest, token: String){
        responseHttp = repository.registrarRuc(rucRequest, token)
    }

    fun registrarTarjeta(tarjetaRequest: TarjetaRequest, token: String){
        responseHttp = repository.registrarTarjeta(tarjetaRequest, token)
    }

    fun listarTarjetas(idCliente: String, token: String) : LiveData<ArrayList<TarjetaResponse>> {
        return repository.listarTarjetas(idCliente, token)
    }

    fun listarDirecciones(idCliente: String, token: String) : LiveData<ArrayList<DireccionResponse>> {
        return repository.listarDirecciones(idCliente, token)
    }
}