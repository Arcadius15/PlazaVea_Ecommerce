package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.CategoriaApiRepository
import edu.pe.idat.pva.models.SubCategoriaResponse

class CategoriaProvider : ViewModel() {
    var subCategoriaResponse: LiveData<ArrayList<SubCategoriaResponse>>

    private var repository = CategoriaApiRepository()

    init {
        subCategoriaResponse = repository.subCategoriaResponse
    }

    fun getAll() {
        subCategoriaResponse = repository.getAll()
    }
}