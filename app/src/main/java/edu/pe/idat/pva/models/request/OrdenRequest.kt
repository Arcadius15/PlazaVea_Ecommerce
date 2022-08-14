package edu.pe.idat.pva.models.request

data class OrdenRequest(
    var cliente: ClienteIDRequest,
    var direccion: String,
    var fecha: String,
    var formaPago: String,
    var igv: Double,
    var monto: Double,
    var ordendetalle: ArrayList<OrdendetalleRequest>,
    var status: String,
    var tienda: TiendaIDRequest,
    var tipoFop: Int,
    var total: Double
)