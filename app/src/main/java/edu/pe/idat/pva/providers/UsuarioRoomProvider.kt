package edu.pe.idat.pva.providers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.pe.idat.pva.db.RoomDatabaseMng
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.roomrep.UsuarioRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioRoomProvider(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRoomRepository

    init {
        val usuarioDAO = RoomDatabaseMng.getDatabase(application).usuarioDAO()
        repository = UsuarioRoomRepository(usuarioDAO)
    }

    fun insertar(usuarioEntity: UsuarioEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.insertar(usuarioEntity)
    }

    fun actualizar(usuarioEntity: UsuarioEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.actualizar(usuarioEntity)
    }

    fun eliminarTodo() = viewModelScope.launch(Dispatchers.IO){
        repository.eliminarTodo()
    }

    fun obtener(): LiveData<UsuarioEntity> {
        return repository.obtener()
    }
}