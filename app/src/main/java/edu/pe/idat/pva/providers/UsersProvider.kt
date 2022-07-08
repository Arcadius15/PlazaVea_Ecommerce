package edu.pe.idat.pva.providers


import android.provider.ContactsContract
import edu.pe.idat.pva.api.ApiRoutes
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.User
import edu.pe.idat.pva.routes.UserRoutes
import retrofit2.Call

class UsersProvider {
    private var usersRoutes: UserRoutes? = null;

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
    return  usersRoutes?.registrar(user)
    }

    fun login(email: String, password: String): Call<ResponseHttp>?{
        return usersRoutes?.login(email, password)
    }
}