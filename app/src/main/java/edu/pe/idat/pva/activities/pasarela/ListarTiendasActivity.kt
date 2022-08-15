package edu.pe.idat.pva.activities.pasarela

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityListarTiendasBinding
import edu.pe.idat.pva.providers.TiendaProvider


class ListarTiendasActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener,
                                GoogleMap.OnMapClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityListarTiendasBinding

    private lateinit var tiendaProvider: TiendaProvider

    private var idTienda = "0"
    private var nombreTienda = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarTiendasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapTiendas) as SupportMapFragment

        tiendaProvider = ViewModelProvider(this)[TiendaProvider::class.java]

        binding.btnEscogerTienda.isEnabled = false

        getTiendas()

        binding.btnEscogerTienda.setOnClickListener(this)

        mapFragment.getMapAsync(this)
    }

    private fun getTiendas() {
        tiendaProvider.getTiendas().observe(this){
            if (it != null) {
                it.forEach { tr ->
                    val latLng = LatLng(tr.lat,tr.lng)

                    val marker = mMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(tr.nombre)
                        .snippet(tr.idTienda)
                        .icon(BitmapDescriptorFactory.fromBitmap(reducirIcono("tiendamarker",125,125))))

                    marker!!.showInfoWindow()
                }
            } else {
                Toast.makeText(this,"Ninguna tienda encontrada",Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)

        val puntoFijo = LatLng(-12.017195995642483, -77.0737393231985)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoFijo, 16.0F))
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        binding.btnEscogerTienda.isEnabled = true

        idTienda = p0.snippet!!
        nombreTienda = p0.title!!

        return false
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnEscogerTienda -> escogerTienda()
        }
    }

    private fun escogerTienda() {
        if (idTienda == "0" || nombreTienda == "0") {
            Toast.makeText(this,"Seleccione una tienda",Toast.LENGTH_LONG)
        } else {
            setResult(Activity.RESULT_OK, Intent()
                .putExtra("idTienda",idTienda)
                .putExtra("nombreTienda",nombreTienda))
            finish()
        }
    }

    override fun onMapClick(p0: LatLng) {
        if (binding.btnEscogerTienda.isEnabled) {
            binding.btnEscogerTienda.isEnabled = false
            idTienda = "0"
            nombreTienda = "0"
        }
    }

    fun reducirIcono(nombre: String, width: Int, height: Int): Bitmap {
        val imagen = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                nombre, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imagen, width, height, false)
    }
}