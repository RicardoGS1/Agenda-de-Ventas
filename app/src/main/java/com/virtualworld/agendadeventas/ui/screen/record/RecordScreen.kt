package com.virtualworld.agendadeventas.ui.screen.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.core.Model.SoldForStore
import com.virtualworld.agendadeventas.ui.screen.common.DropDownMenuStoresView

@Composable
fun RecordScreen(viewModel: RecordViewModel = hiltViewModel()) {


    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val uiMessengerState by viewModel.messengerState.collectAsState()
    val soldForStore by viewModel.soldForStore.collectAsState()

    RecordScreenContent(
        storesActiveState,
        uiMessengerState,
        soldForStore,
        onStoreSelected = { storeId -> viewModel.getSoldForStore(storeId) },
    )


}

@Composable
fun RecordScreenContent(
    storesActiveState: List<Pair<Int, String>>,
    uiMessengerState: ScreenUiState,
    soldForStore: List<SoldForStore>,
    onStoreSelected: (Int) -> Unit
) {


    var selectedStoreIndex by remember { mutableStateOf(-1) }

    //Envia al viewModel el id de la tienda seleccionado por el usuario
    LaunchedEffect(selectedStoreIndex) {
        if (selectedStoreIndex != -1) {
            onStoreSelected(storesActiveState[selectedStoreIndex].first)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxWidth()) {
            DropDownMenuStoresView(
                storesActiveState,
                uiMessengerState,
                selectedStoreIndex,
                { index ->
                    selectedStoreIndex = index
                })

            if (selectedStoreIndex != -1) {
                println(soldForStore)
                ListProductRecordView(soldForStore)
            }
        }


    }


}




