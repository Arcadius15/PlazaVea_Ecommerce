package edu.pe.idat.pva.activities

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDireccionRegistroBinding
import java.io.IOException

class DireccionRegistroActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDireccionRegistroBinding

    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDireccionRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                } catch (e: IOException){
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
            imprimirDatos()
        }

        mapFragment.getMapAsync(this)
    }

    private fun imprimirDatos() {
        if (!binding.svLocation.query.toString().isNullOrBlank() && marker != null) {
            Toast.makeText(applicationContext,
                "Eso brad lat y long: ${marker!!.position.latitude} - ${marker!!.position.longitude}",
                Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext,
                "Direccion (Enr Seg): ${binding.svLocation.query}",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                applicationContext,
                "Por favor, introduzca una dirección",
                Toast.LENGTH_LONG
            ).show()
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
}