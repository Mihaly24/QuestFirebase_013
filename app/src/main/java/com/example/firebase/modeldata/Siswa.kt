package com.example.firebase.modeldata

data class Siswa(
    val id: Long = 0,
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

data class DetailSiswa(
    val id: Long = 0,
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

fun DetailSiswa.toDataSiswa(): Siswa = Siswa(id, nama, alamat, telpon)
fun Siswa.toDetailSiswa(): DetailSiswa = DetailSiswa(id, nama, alamat, telpon)

data class UIStateSiswa(
    val detailSiswa: DetailSiswa = DetailSiswa(),
    val isEntryValid: Boolean = false
)

fun Siswa.toUIStateSiswa(isEntryValid: Boolean = false): UIStateSiswa = UIStateSiswa(
    detailSiswa = this.toDetailSiswa(),
    isEntryValid = isEntryValid
)