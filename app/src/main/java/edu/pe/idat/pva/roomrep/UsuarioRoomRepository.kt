package edu.pe.idat.pva.roomrep

import androidx.lifecycle.LiveData
import edu.pe.idat.pva.db.dao.UsuarioDAO
import edu.pe.idat.pva.db.entity.UsuarioEntity

class UsuarioRoomRepository(private val usuarioDAO: UsuarioDAO) {
    suspend fun insertar(usuarioEntity: UsuarioEntity){
        usuarioDAO.insertar(usuarioEntity)
    }
    suspend fun actualizar(usuarioEntity: UsuarioEntity){
        usuarioDAO.actualizar(usuarioEntity)
    }
    suspend fun eliminarTodo(){
        usuarioDAO.eliminarTodo()
    }
    fun obtener(): LiveData<UsuarioEntity>{
        return usuarioDAO.obtener()
    }
}