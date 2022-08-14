package edu.pe.idat.pva.models.response

data class OrdenPageResponse(
    var content: ArrayList<OrdenResponse>,
    var totalPages: Int
)
