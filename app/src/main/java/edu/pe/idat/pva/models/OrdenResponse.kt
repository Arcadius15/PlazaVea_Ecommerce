package edu.pe.idat.pva.models

data class OrdenResponse(
    var direccion: String,
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
    var ordendetalle: OrdendetalleRequest
)