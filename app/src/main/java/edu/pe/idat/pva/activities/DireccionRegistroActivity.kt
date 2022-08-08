package edu.pe.idat.pva.activities

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDireccionRegistroBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.ClienteIDRequest
import edu.pe.idat.pva.models.DireccionRequest
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class DireccionRegistroActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDireccionRegistroBinding

    private var marker: Marker? = null

    private lateinit var clienteProvider: ClienteProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDireccionRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
                    if(marker != null){
                        marker!!.remove()
                    }

                    val location: String = query!!
                    val addressList: List<Address>

                    if (location != null) {
                        val geocoder = Geocoder(this@DireccionRegistroActivity)
                        addressList = geocoder.getFromLocationName(location,1)

                        val address = addressList[0]

                        val latLng = LatLng(address.latitude,address.longitude)
                        marker = mMap.addMarker(MarkerOptions()
                            .position(latLng)
                            .title(location)
                            .draggable(true))
                        marker!!.showInfoWindow()
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0F))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Por favor, introduzca una dirección",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    return false
                } catch (e: Exception){
                    Toast.makeText(
                        applicationContext,
                        "Por favor, introduzca una dirección válida",
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                    return false
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        binding.btnGuardarDir.setOnClickListener{
            getUserFromDB()
        }

        clienteProvider.responseHttp.observe(this){
            obtenerRespuesta(it!!)
        }

        mapFragment.getMapAsync(this)
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess){
            Toast.makeText(
                applicationContext,
                "Dirección registrada.",
                Toast.LENGTH_LONG
            ).show()

            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(
                applicationContext,
                "Hubo un problema con el servicio",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btnGuardarDir.isEnabled = true
    }

    private fun guardarDireccion() {
        binding.btnGuardarDir.isEnabled = false
        if (!binding.svLocation.query.toString().isNullOrBlank() && marker != null) {
            val clienteIDRequest = ClienteIDRequest(
                usuario.idCliente
            )

            val direccionRequest = DireccionRequest(
                binding.svLocation.query.toString(),
                marker!!.position.latitude,
                marker!!.position.longitude,
                clienteIDRequest
            )

            clienteProvider.registrarDireccion(direccionRequest,
                "Bearer " + token.token)
        } else {
            Toast.makeText(
                applicationContext,
                "Por favor, introduzca una dirección",
                Toast.LENGTH_LONG
            ).show()
            binding.btnGuardarDir.isEnabled = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerDragListener(this)

        val puntoFijo = LatLng(-12.017195995642483, -77.0737393231985)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoFijo, 16.0F))
    }

    override fun onMarkerDrag(p0: Marker) {
        val posicion = p0.position
        p0.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(posicion))
    }

    override fun onMarkerDragEnd(p0: Marker) {
        p0.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p0.position))
    }

    override fun onMarkerDragStart(p0: Marker) {
        p0.showInfoWindow()
    }

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(this){
            usuario = it
            getTokenFromDB()
        }
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(this){
            token = it
            guardarDireccion()
        }
    }

}