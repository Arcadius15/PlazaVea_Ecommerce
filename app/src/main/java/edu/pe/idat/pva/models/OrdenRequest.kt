package edu.pe.idat.pva.models

data class OrdenRequest(
    var cliente: ClienteIDRequest,
    var direccion: String,
    var fecha: String,
    var fechaEntrega: String,
    var formaPago: String,
    var igv: Double,
    var monto: Double,
    var ordendetalle: List<OrdendetalleRequest>,
    var status: String,
    var tienda: TiendaIDRequest,
    var tipoFop: Int,
    var total: Double
)