package edu.pe.idat.pva.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.pe.idat.pva.db.dao.TokenDAO
import edu.pe.idat.pva.db.dao.UsuarioDAO
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class, TokenEntity::class], version = 1)
abstract class RoomDatabaseMng : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun tokenDAO(): TokenDAO

    companion object{
        @Volatile
        private var INSTANCE: RoomDatabaseMng? = null

        fun getDatabase(context: Context) : RoomDatabaseMng{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabaseMng::class.java,
                    "plazaveadb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}