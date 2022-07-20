package edu.pe.idat.pva.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDireccionRegistroBinding
import edu.pe.idat.pva.models.*
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref
import java.io.IOException

class DireccionRegistroActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDireccionRegistroBinding

    private lateinit var sharedPref: SharedPref

    private lateinit var usuario: UsuarioResponse

    private var marker: Marker? = null

    private lateinit var clienteProvider: ClienteProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDireccionRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
                    if(marker != null){
                        marker!!.remove()
                    }

                    var location: String = query!!
                    var addressList: List<Address>

                    if (location != null) {
                        var geocoder = Geocoder(this@DireccionRegistroActivity)
                        addressList = geocoder.getFromLocationName(location,1)

                        var address = addressList[0]

                        var latLng = LatLng(address.latitude,address.longitude)
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
            guardarDireccion()
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

            val i = Intent(this, RegTarjetaActivity::class.java)
            i.putExtra("direccion",binding.svLocation.query.toString())
            i.putExtra("tipo",intent.getStringExtra("tipo").toString())
            if (!intent.getStringExtra("ruc").toString().equals(null)) {
                i.putExtra("ruc",intent.getStringExtra("ruc").toString())
            }
            startActivity(i)
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
            usuario = getUserFromSession()!!

            var clienteIDRequest = ClienteIDRequest(
                usuario.cliente.idCliente
            )

            var direccionRequest = DireccionRequest(
                binding.svLocation.query.toString(),
                marker!!.position.latitude,
                marker!!.position.longitude,
                clienteIDRequest
            )

            clienteProvider.registrarDireccion(direccionRequest,
                "Bearer " + getTokenFromSession()!!.token)
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
        var posicion = p0.position
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

    private fun getUserFromSession(): UsuarioResponse?{
        val gson = Gson()

        return if(sharedPref.getData("user").isNullOrBlank()){
            null
        } else {
            val user = gson.fromJson(sharedPref.getData("user"), UsuarioResponse::class.java)
            user
        }
    }

    private fun getTokenFromSession(): LoginResponse?{
        val gson = Gson()

        return if(sharedPref.getData("token").isNullOrBlank()){
            null
        } else {
            val token = gson.fromJson(sharedPref.getData("token"), LoginResponse::class.java)
            token
        }
    }
}