package com.virtualword3d.salesregister.Screen.Venta

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.ui.Screen.sell.ListProductSellView
import com.virtualworld.agendadeventas.ui.Screen.sell.SellViewModel
import com.virtualworld.agendadeventas.ui.Screen.common.DropDownMenuStoresView


@Composable
fun SellScreen(viewModel: SellViewModel = hiltViewModel()) {

    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val productForStore by viewModel.productForStore.collectAsState()
    val listChangerProductsSell by viewModel.listChangerProductsSell.collectAsState()
    val uiMessengerState by viewModel.messengerState.collectAsState()

    SellScreenContent(
        storesActiveState,
        productForStore,
        listChangerProductsSell,
        uiMessengerState,
        onChangerMessenger = { stateUi -> viewModel.changerMessenger(stateUi) },
        onStoreSelected = { storeId -> viewModel.getProductForStore(storeId) },
        onUnitSellChanged = { changer -> viewModel.changerUnitSell(changer) },
        onSaveSellClicked = { storeId -> viewModel.SalveSell(storeId) }
    )
}

@Composable
fun SellScreenContent(
    storesActiveState: List<Pair<Int, String>>,
    productForStore: List<ProductStoreCore>,
    listChangerProductsSell: List<Pair<Int, Int>>,
    uiMessengerState: Mensajes,
    onChangerMessenger: (Mensajes) -> Unit,
    onStoreSelected: (Int) -> Unit,
    onUnitSellChanged: (Pair<Int, Int>) -> Unit,
    onSaveSellClicked: (Int) -> Unit
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
                ListProductSellView(productForStore, onUnitSellChanged, listChangerProductsSell)
            }
        }

        SaveButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = { onSaveSellClicked(storesActiveState[selectedStoreIndex].first) }
        )

        MySnackBar(
            uiMessengerState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerMessenger=onChangerMessenger
        )
    }
}

@Composable
fun SaveButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)),
        onClick = onClick
    ) {
        Text(text = stringResource(id = R.string.boton_guardar_vender))
    }
}


@Composable
fun MySnackBar(uiMessengerState: Mensajes, modifier: Modifier, onChangerMessenger: (Mensajes) -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }

    val messageBien = stringResource(id = R.string.mensaje_vendido_vender)
    val messageError = stringResource(id = R.string.mensaje_error)

    LaunchedEffect(uiMessengerState) {

        if (uiMessengerState == Mensajes.BIEN || uiMessengerState == Mensajes.ERROR) {
            val message = when (uiMessengerState) {
                Mensajes.BIEN -> messageBien
                Mensajes.ERROR -> messageError
                else -> ""
            }
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            onChangerMessenger(Mensajes.NEUTRO) // Restablecer el estado del mensaje
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    )

}



