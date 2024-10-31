package com.virtualworld.agendadeventas.ui.screen.stores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.virtualword3d.salesregister.Data.Entity.StoreRoom

import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ScreenStores() {

    val viewModel: ViewModelTienda = hiltViewModel()

    val screenUiState: ScreenUiState by viewModel.screenUiState.collectAsState()
    val storesAllState by viewModel.storesAllState.collectAsState()

    val onChangerScreenUiState = { stateUi: ScreenUiState -> viewModel.changerUiState(stateUi) }

    val onChangeNameStore = { name: String, id: Int -> viewModel.onChangeNameStore(name, id) }
    val onChangeSwitchStore =
        { active: Boolean, id: Int -> viewModel.onChangeActiveStore(active, id) }
    val updateStore = {viewModel.updateStore()}

    ContentStores(screenUiState, storesAllState, onChangerScreenUiState, onChangeNameStore,onChangeSwitchStore,updateStore )




    }


@Composable
fun ContentStores(
    screenUiState: ScreenUiState,
    storesAllState: List<StoreRoom>,
    onChangerScreenUiState: (ScreenUiState) -> Unit,
    onChangeNameStore: (String, Int) -> Unit,
    onChangeSwitchStore: (Boolean, Int) -> Unit,
    updateStore: () -> Unit, ) {


    //VISTA PRINCIPAL
    Box() {

        Column {
            //TITULO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {

                Column {
                    Text(
                        text = stringResource(id = R.string.titulo_Tiendas),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary

                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {

                        Text(
                            text = stringResource(id = R.string.info_colunas_nombre),
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = stringResource(id = R.string.info_colunas_activas),
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterEnd)
                                .padding(end = 12.dp)

                        )
                    }

                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                storesAllState.forEach {
                    item {
                        FildTienda(
                            it.nombre,
                            it.activa,
                            onChangeNameStore,
                            onChangeSwitchStore,
                            it.id.toInt()
                        )
                    }
                }
            }
        }


        Button(
            onClick =  updateStore,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(id = R.string.boton_guardar_Tiendas))

        }

        ManagerScreenStateView(uiMessengerState = screenUiState, modifier = Modifier.align(Alignment.BottomCenter),onChangerScreenUiState)

    }
}


@Composable
fun FildTienda(
    nombreTienda: String,
    shwitTienda: Boolean,
    onChangeNameStore: (String, Int) -> Unit,
    onChangeSwitchStore: (Boolean, Int) -> Unit,
    s: Int,


    ) {


    Row() {


        TextField(
            value = nombreTienda,
            onValueChange = { onChangeNameStore(it, s) },
            label = {
                Text(
                    text = stringResource(id = R.string.label_nombre_de_tiendas) + s,

                    )
            },
            modifier = Modifier

                .padding(horizontal = 32.dp)
                .padding(vertical = 8.dp),
            singleLine = true


        )


        Switch(
            checked = shwitTienda, onCheckedChange = { onChangeSwitchStore(it, s) },
            modifier = Modifier
                .align(alignment = CenterVertically)
                .padding(end = 32.dp),


            )

    }
}
