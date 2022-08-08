package edu.pe.idat.pva.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.pe.idat.pva.db.entity.TokenEntity

@Dao
interface TokenDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertar(vararg token: TokenEntity)

    @Update
    fun actualizar(vararg token: TokenEntity)

    @Query("DELETE FROM token")
    fun eliminarToken()

    @Query("SELECT * FROM token LIMIT 1")
    fun obtener(): LiveData<TokenEntity>
}