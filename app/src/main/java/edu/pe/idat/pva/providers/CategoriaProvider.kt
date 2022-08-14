package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.CategoriaApiRepository
import edu.pe.idat.pva.models.response.SubCategoriaResponse

class CategoriaProvider : ViewModel() {
    private var repository = CategoriaApiRepository()

    fun getAll() : LiveData<ArrayList<SubCategoriaResponse>> {
        return repository.getAll()
    }
}