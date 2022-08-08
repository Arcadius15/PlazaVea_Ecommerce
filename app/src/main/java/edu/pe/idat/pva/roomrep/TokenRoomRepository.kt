package edu.pe.idat.pva.roomrep

import androidx.lifecycle.LiveData
import edu.pe.idat.pva.db.dao.TokenDAO
import edu.pe.idat.pva.db.entity.TokenEntity

class TokenRoomRepository(private val tokenDAO: TokenDAO) {
    suspend fun insertar(tokenEntity: TokenEntity){
        tokenDAO.insertar(tokenEntity)
    }
    suspend fun actualizar(tokenEntity: TokenEntity){
        tokenDAO.actualizar(tokenEntity)
    }
    suspend fun eliminarToken(){
        tokenDAO.eliminarToken()
    }
    fun obtener(): LiveData<TokenEntity> {
        return tokenDAO.obtener()
    }
}