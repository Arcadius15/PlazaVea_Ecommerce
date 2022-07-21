package edu.pe.idat.pva.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.shopping_bag.ShoppingBagActivity
import edu.pe.idat.pva.databinding.ActivityHomeBinding
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.utils.SharedPref

class HomeActivity : AppCompatActivity() {

    val TAG = "HomeActivity"
    private lateinit var sharedPref: SharedPref

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setupRecyclerView()
        sharedPref= SharedPref(this)

        setSupportActionBar(binding.appBarHome.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.categoriaFragment, R.id.productoFragment, R.id.historialFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.btnLogout.setOnClickListener{ logout() }
        binding.btnGoCambiarContra.setOnClickListener{ startActivity(Intent(this,CambiarContraActivity::class.java)) }

        val usuario = getUserFromSession()!!

        var header = binding.navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.tvUsuario).text = usuario.cliente.nombre +
                                                                " " + usuario.cliente.apellidos
        header.findViewById<TextView>(R.id.tvCorreo).text = usuario.email
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_shopping){
            goToShoppingBag()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getUserFromSession(): UsuarioResponse?{
        val gson = Gson()

        return if(sharedPref.getData("user").isNullOrBlank()){
            null
        } else {
            val user = gson.fromJson(sharedPref.getData("user"), UsuarioResponse::class.java)
            user
        }
    }

    private fun logout() {
        sharedPref.remove("user")
        sharedPref.remove("token")
        if(!sharedPref.getData("shopBag").isNullOrBlank()){
            sharedPref.remove(("shopBag"))
        }
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun goToShoppingBag(){
        val i = Intent(this, ShoppingBagActivity::class.java)
        startActivity(i)
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}