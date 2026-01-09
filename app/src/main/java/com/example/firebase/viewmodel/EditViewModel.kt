package com.example.firebase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.modeldata.DetailSiswa
import com.example.firebase.modeldata.UIStateSiswa
import com.example.firebase.modeldata.toUIStateSiswa
import com.example.firebase.repositori.RepositorySiswa
import com.example.firebase.view.route.DestinasiDetail
import kotlinx.coroutines.launch

class EditViewModel(savedStateHandle: SavedStateHandle, private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private val idSiswa: Long =
        savedStateHandle.get<String>(DestinasiDetail.itemIdArg)?.toLong()
            ?: error("idSiswa tidak ditemukan di SavedStateHandle")

    init {
        viewModelScope.launch {
            uiStateSiswa = repositorySiswa.getSatuSiswa(idSiswa)!!
                .toUIStateSiswa(true)
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa =
            UIStateSiswa(detailSiswa = detailSiswa, isEntryValid = validasiInput(detailSiswa))
    }
}