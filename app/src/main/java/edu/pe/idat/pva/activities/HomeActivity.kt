package edu.pe.idat.pva.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.shopping_bag.ShoppingBagActivity
import edu.pe.idat.pva.databinding.ActivityHomeBinding
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.providers.UsuarioRoomProvider
import edu.pe.idat.pva.utils.SharedPref


class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private lateinit var usuarioRoomProvider: UsuarioRoomProvider

    private lateinit var usuarioEntity: UsuarioEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]

        setSupportActionBar(binding.appBarHome.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.direccionesFragment, R.id.tarjetasFragment, R.id.historialFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.btnLogout.setOnClickListener{ logout() }
        binding.btnGoCambiarContra.setOnClickListener{ startActivity(Intent(this,CambiarContraActivity::class.java)) }

        usuarioRoomProvider.obtener().observe(this){
                usuario -> usuario?.let {
                    usuarioEntity = usuario
                    val header = binding.navView.getHeaderView(0)
                    header.findViewById<TextView>(R.id.tvUsuario).text = usuarioEntity.nombre +
                            " " + usuarioEntity.apellidos
                    header.findViewById<TextView>(R.id.tvCorreo).text = usuarioEntity.email
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    private fun logout() {
        if (!sharedPref.getData("shopBag").isNullOrBlank()) {
            sharedPref.remove("shopBag")
        }
        if (sharedPref.getSomeBooleanValue("mantener")) {
            sharedPref.remove("mantener")
        }
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun goToShoppingBag(){
        val i = Intent(this, ShoppingBagActivity::class.java)
        startActivity(i)
    }

}