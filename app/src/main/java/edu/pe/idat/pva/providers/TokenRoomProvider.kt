package edu.pe.idat.pva.providers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.pe.idat.pva.db.RoomDatabaseMng
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.roomrep.TokenRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TokenRoomProvider(application: Application) : AndroidViewModel(application) {

    private val repository: TokenRoomRepository

    init {
        val tokenDAO = RoomDatabaseMng.getDatabase(application).tokenDAO()
        repository = TokenRoomRepository(tokenDAO)
    }

    fun insertar(tokenEntity: TokenEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.insertar(tokenEntity)
    }

    fun actualizar(tokenEntity: TokenEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.actualizar(tokenEntity)
    }

    fun eliminarToken() = viewModelScope.launch(Dispatchers.IO){
        repository.eliminarToken()
    }

    fun obtener(): LiveData<TokenEntity> {
        return repository.obtener()
    }
}