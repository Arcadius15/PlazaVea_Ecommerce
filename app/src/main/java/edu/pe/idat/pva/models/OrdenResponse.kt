package edu.pe.idat.pva.models

import java.io.Serializable

data class OrdenResponse(
    var direccion: String,
    var lat: Double,
    var lng: Double,
    var fecha: String,
    var fechaEntrega: String,
    var formaPago: String,
    var idOrden: String,
    var igv: Double,
    var monto: Double,
    var status: String,
    var tipoFop: Int,
    var total: Double,
    var cliente: ClienteIDRequest,
    var tienda: TiendaIDRequest,
    var ordendetalle: ArrayList<OrdenDetalleResponse>
) : Serializable