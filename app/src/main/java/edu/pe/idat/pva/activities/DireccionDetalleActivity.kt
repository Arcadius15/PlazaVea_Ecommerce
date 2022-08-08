package edu.pe.idat.pva.activities

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import edu.pe.idat.pva.databinding.ActivityDireccionDetalleBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.models.DireccionPatchRequest
import edu.pe.idat.pva.models.DireccionResponse
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.providers.DireccionProvider
import edu.pe.idat.pva.providers.TokenRoomProvider

class DireccionDetalleActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDireccionDetalleBinding

    private lateinit var direccionProvider: DireccionProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private var marker: Marker? = null

    private lateinit var direccion: DireccionResponse
    private lateinit var token: TokenEntity

    private var accion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDireccionDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        direccionProvider = ViewModelProvider(this)[DireccionProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        direccion = intent.getSerializableExtra("direccion") as DireccionResponse

        binding.svLocation.setQuery(direccion.direccion,false)

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
                    if(marker != null){
                        marker!!.remove()
                    }

                    val location: String = query!!
                    val addressList: List<Address>

                    if (location != null) {
                        val geocoder = Geocoder(this@DireccionDetalleActivity)
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

        binding.btnEditarDir.setOnClickListener(this)
        binding.btnBorrarDir.setOnClickListener(this)

        direccionProvider.responseHttp.observe(this){
            obtenerRespuesta(it)
        }

        mapFragment.getMapAsync(this)
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess){
            Toast.makeText(
                applicationContext,
                "Dirección $accion",
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
        binding.btnEditarDir.isEnabled = true
        binding.btnBorrarDir.isEnabled = true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerDragListener(this)

        val puntoFijo = LatLng(direccion.latitud, direccion.longitud)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoFijo, 16.0F))

        marker = mMap.addMarker(MarkerOptions()
            .position(puntoFijo)
            .title(direccion.direccion)
            .draggable(true))
        marker!!.showInfoWindow()
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

    private fun getTokenFromDB(origen: String){
        tokenRoomProvider.obtener().observe(this){
            token = it
            when (origen) {
                "e" -> editarDireccion()
                "b" -> borrarDireccion()
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnEditarDir -> getTokenFromDB("e")
            R.id.btnBorrarDir -> getTokenFromDB("b")
        }
    }

    private fun confirmarEdicion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Edición")
            .setMessage("¿Seguro que desea editar esta dirección?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                accion = "editada"

                val direccionPatchRequest = DireccionPatchRequest(
                    binding.svLocation.query.toString(),
                    marker!!.position.latitude,
                    marker!!.position.longitude,
                )

                direccionProvider.editarDireccion(direccion.idDireccion,
                    direccionPatchRequest,
                    "Bearer " + token.token)
                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btnEditarDir.isEnabled = true
                binding.btnBorrarDir.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }

    private fun confirmarBorrar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Borrar")
            .setMessage("¿Seguro que desea borrar esta dirección?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                accion = "borrada"

                direccionProvider.borrarDireccion(direccion.idDireccion,
                    "Bearer " + token.token)
                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btnEditarDir.isEnabled = true
                binding.btnBorrarDir.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }

    private fun borrarDireccion() {
        binding.btnEditarDir.isEnabled = false
        binding.btnBorrarDir.isEnabled = false

        confirmarBorrar()
    }

    private fun editarDireccion() {
        binding.btnEditarDir.isEnabled = false
        binding.btnBorrarDir.isEnabled = false
        if (!binding.svLocation.query.toString().isNullOrBlank() && marker != null) {
            confirmarEdicion()
        } else {
            Toast.makeText(
                applicationContext,
                "Por favor, introduzca una dirección",
                Toast.LENGTH_LONG
            ).show()
            binding.btnEditarDir.isEnabled = true
            binding.btnBorrarDir.isEnabled = true
        }
    }
}