package edu.pe.idat.pva.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.pe.idat.pva.db.entity.UsuarioEntity

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertar(vararg usuario: UsuarioEntity)

    @Update
    fun actualizar(vararg usuario: UsuarioEntity)

    @Query("DELETE FROM usuario")
    fun eliminarTodo()

    @Query("SELECT * FROM usuario LIMIT 1")
    fun obtener(): LiveData<UsuarioEntity>

}