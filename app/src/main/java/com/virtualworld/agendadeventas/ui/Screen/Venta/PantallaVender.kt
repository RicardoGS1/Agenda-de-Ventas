package com.virtualword3d.salesregister.Screen.Venta

import android.util.Log
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.Screen.Venta.ListProductSellView
import com.virtualworld.agendadeventas.ui.Screen.Venta.ViewModelVender
import com.virtualworld.agendadeventas.ui.Screen.common.DropDownMenuStoresView
import kotlinx.coroutines.launch


@Composable
fun PantallaVender(){

    val viewModel: ViewModelVender = hiltViewModel()

    val stateScreenStoresActive by viewModel.storesActiveState.collectAsState()
    val stateScreenProductStore by viewModel.productForStore.collectAsState()

    var selectedIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(key1 = true) {
        viewModel.getStoresActive()
        viewModel.getProducto()

    }

    LaunchedEffect(selectedIndex ) {
        println(selectedIndex.toString())
        if(selectedIndex!=-1)
        viewModel.getProductForStore(stateScreenStoresActive[selectedIndex].first)
    }

   // MensajesAgregar(viewModel)

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxWidth())
        {


            DropDownMenuStoresView(
                items = stateScreenStoresActive,
                selectedIndex = selectedIndex,
                onItemSelected = { index, _ -> selectedIndex = index },
            )

            if(selectedIndex!=-1)
                ListProductSellView(stateScreenProductStore)
        }

        Button( modifier = Modifier.align(alignment = Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)) ,
                onClick = { viewModel.GuardarVenta() } ) {
                Text(text = stringResource(id = R.string.boton_guardar_vender))
        }

    }

}





@Composable
fun MensajesAgregar(viewModel: ViewModelVender) {
    Log.d("efecto","MensajesAgregar")

    val respuestaError: Mensajes by viewModel.respuestaError.observeAsState(Mensajes.NEUTRO)
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SnackbarHost(hostState = snackState)

    fun iniciarSnakbar(texto: String) {
        scope.launch {
            snackState.showSnackbar(
                message = texto,
                duration = SnackbarDuration.Short
            )
        }
    }

    if (respuestaError == Mensajes.BIEN) {
        iniciarSnakbar(stringResource(id = R.string.mensaje_vendido_vender))
        viewModel.controlMensaje(Mensajes.NEUTRO)
    }

}


