package com.example.firebase.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase.R
import com.example.firebase.modeldata.Siswa
import com.example.firebase.view.route.DestinasiDetail
import com.example.firebase.viewmodel.DetailViewModel
import com.example.firebase.viewmodel.PenyediaViewModel
import com.example.firebase.viewmodel.StatusUIDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateBack: () -> Unit,
    navigateToEditItem: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val stateDetailSiswa = viewModel.statusUIDetail

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (stateDetailSiswa is StatusUIDetail.Success) {
                        navigateToEditItem(stateDetailSiswa.satusiswa?.id.toString())
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_siswa)
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (stateDetailSiswa) {
                is StatusUIDetail.Loading -> OnLoading(modifier = Modifier.fillMaxSize())
                is StatusUIDetail.Success -> {
                    if (stateDetailSiswa.satusiswa != null) {
                        DetailSiswaBody(
                            siswa = stateDetailSiswa.satusiswa,
                            onDelete = {
                                viewModel.hapusSatuSiswa()
                                navigateBack()
                            }
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.deskripsi_no_item),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is StatusUIDetail.Error -> OnError(retryAction = viewModel::getSatuSiswa, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun DetailSiswaBody(
    siswa: Siswa,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemDetailSiswa(
            siswa = siswa,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun ItemDetailSiswa(
    siswa: Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ComponentDetailSiswa(
                judul = stringResource(id = R.string.nama),
                isinya = siswa.nama
            )
            ComponentDetailSiswa(
                judul = stringResource(id = R.string.alamat),
                isinya = siswa.alamat
            )
            ComponentDetailSiswa(
                judul = stringResource(id = R.string.telpon),
                isinya = siswa.telpon
            )
        }
    }
}

@Composable
fun ComponentDetailSiswa(
    judul: String,
    isinya: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = judul,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = isinya,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}