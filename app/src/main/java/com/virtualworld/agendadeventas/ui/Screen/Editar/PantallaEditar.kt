package com.virtualword3d.salesregister.Screen.Editar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel


import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaEditar() {

    val viewModel: EditarViewModel = hiltViewModel()
    viewModel.getProducto()
    viewModel.getTiendas()
    ListaProductosEditar(viewModel)
    VentanaDetalles(viewModel)
    VentanaEliminar(viewModel)
    VentanaEditar(viewModel)

}



//LISTA PRODUCTOS PANTALLA EDITAR+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

@Composable
fun ListaProductosEditar(viewModel: EditarViewModel) {

    val nombreProducto: List<String> by viewModel.nombreProducto.observeAsState(initial = listOf())

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 46.dp)
    ) {

        itemsIndexed(items = nombreProducto) { index, nombres ->
            ItemListaProductosEditar(index, nombres, viewModel)
        }
    }
}


//VENTANAS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun VentanaEditar(viewModel: EditarViewModel) {

    val nombreProductoEditar: String by viewModel.nombreProductoEditar.observeAsState(initial = "")
    val precioCompra: String by viewModel.precioCompra.observeAsState(initial = "")
    val precioTienda1: String by viewModel.precioTienda1.observeAsState(initial = "")
    val precioTienda2: String by viewModel.precioTienda2.observeAsState(initial = "")
    val precioTienda3: String by viewModel.precioTienda3.observeAsState(initial = "")
    val precioTienda4: String by viewModel.precioTienda4.observeAsState(initial = "")
    val precioTienda5: String by viewModel.precioTienda5.observeAsState(initial = "")

    val activaTienda1: Boolean by viewModel.activaTienda1.observeAsState(initial = true)
    val activaTienda2: Boolean by viewModel.activaTienda2.observeAsState(initial = true)
    val activaTienda3: Boolean by viewModel.activaTienda3.observeAsState(initial = true)
    val activaTienda4: Boolean by viewModel.activaTienda4.observeAsState(initial = true)
    val activaTienda5: Boolean by viewModel.activaTienda5.observeAsState(initial = true)

    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")

    val ventana_editar_enable: Boolean by viewModel.ventana_editar_enable.observeAsState(initial = false)

    if (ventana_editar_enable == true) {

        VistaVentanaEditar(viewModel,nombreProductoEditar,precioCompra, precioTienda1, precioTienda2, precioTienda3, precioTienda4, precioTienda5, activaTienda1, activaTienda2, activaTienda3, activaTienda4, activaTienda5, nombreTienda1, nombreTienda2, nombreTienda3, nombreTienda4, nombreTienda5)


    }






}


@Composable
fun VentanaDetalles(viewModel: EditarViewModel) {

    val ventana_detalles_enable: Boolean by viewModel.ventana_detalles_enable.observeAsState(initial = false)
    val activaTienda1: Boolean by viewModel.activaTienda1.observeAsState(initial = true)
    val activaTienda2: Boolean by viewModel.activaTienda2.observeAsState(initial = true)
    val activaTienda3: Boolean by viewModel.activaTienda3.observeAsState(initial = true)
    val activaTienda4: Boolean by viewModel.activaTienda4.observeAsState(initial = true)
    val activaTienda5: Boolean by viewModel.activaTienda5.observeAsState(initial = true)

    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")

    val producto_detalles: ProductRoom by viewModel.producto_detalles.observeAsState(
        initial = ProductRoom(
            0,
            "",
            0,
            0,
            0,
            0,
            0,
            0
        )
    )


    if (ventana_detalles_enable == true) {
        VistaVentanaDetalles(viewModel,activaTienda1,activaTienda2,activaTienda3,activaTienda4,activaTienda5,producto_detalles,nombreTienda1, nombreTienda2, nombreTienda3, nombreTienda4, nombreTienda5)
    }


}


@Composable
fun VentanaEliminar(viewModel: EditarViewModel) {

    val ventana_borrar_enable: Boolean by viewModel.ventana_borrar_enable.observeAsState(initial = false)
    val producto_borrar: ProductRoom by viewModel.producto_borrar.observeAsState(
        initial = ProductRoom(
            0,
            "",
            0,
            0,
            0,
            0,
            0,
            0
        )
    )

    if (ventana_borrar_enable) {

        VistaVentanaBorrar(viewModel,producto_borrar)




    }


}


//ITEMS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun ItemListaProductosEditar(index: Int, nombres: String, viewModel: EditarViewModel) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
        elevation= CardDefaults.cardElevation(8.dp)){
        Column(modifier = Modifier.fillMaxWidth()) {



                Row(
                    modifier = Modifier.align(Alignment.End),

                    ) {


                    IconButton(
                        onClick = { viewModel.ventanaDetalles(index) },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Icon(
                            Icons.Filled.Visibility,
                            contentDescription = "Cambiar color",                            

                            )
                    }

                    IconButton(
                        onClick = { viewModel.ventanaEditar(index) },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Cambiar color",

                            )
                    }


                    IconButton(
                        onClick = { viewModel.ventanaBorrar(index) },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {

                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Cambiar color",

                            )
                    }


                }

            Text(
                modifier= Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 4.dp),
                text = nombres,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }
    }

}


@Composable
fun ItemsVentanaDetalles(categoria: String, producto_detalle: String) {

    Card(
        modifier = Modifier
            .padding(4.dp),
        border = BorderStroke(1.dp, Color.LightGray)

    ) {

        Column() {
            //INFO
            Text(
                text = categoria,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                textAlign = TextAlign.Center
            )

            Text(
                text = producto_detalle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                textAlign = TextAlign.Center
            )

        }
    }

}


//VISTAS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

@Composable
fun VistaVentanaEditar(viewModel: EditarViewModel, nombreProductoEditar: String, precioCompra: String, precioTienda1: String, precioTienda2: String, precioTienda3: String, precioTienda4: String, precioTienda5: String, activaTienda1: Boolean, activaTienda2: Boolean, activaTienda3: Boolean, activaTienda4: Boolean, activaTienda5: Boolean, nombreTienda1: String, nombreTienda2: String, nombreTienda3: String, nombreTienda4: String, nombreTienda5: String) {

    Dialog(onDismissRequest = { viewModel.ventanaEditar(-1) }){

        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
           // backgroundColor = MaterialTheme.colors.surface,
            elevation= CardDefaults.cardElevation(10.dp),
        ) {

    Box(){
            Column() {

                //TITULO EDITAR y BOTON CERRAR
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.titulo_ventana_editar),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(modifier = Modifier.align(Alignment.TopEnd),
                        onClick = { viewModel.ventanaEditar(-1) }
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cambiar color",
                            )
                    }
                }
                LazyColumn(modifier = Modifier.padding(bottom = 50.dp)) {

                    item {
                        TextField(
                            value = nombreProductoEditar,
                            onValueChange = { viewModel.OnChangedNombreProducto(it) },
                            label = {
                                Text(
                                    text = "Nombre del Producto",

                                    )
                            },
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(vertical = 8.dp),
                            singleLine = true
                        )
                    }

                    //PRECIO COMPRA
                    item {
                        TextField(
                            value = precioCompra,
                            onValueChange = { viewModel.OnChangedPrecioCompra(it) },
                            label = { Text(text = "Precio de compra") },
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }

                    //TIENDA1
                    if (activaTienda1) {
                        item {
                            TextField(
                                value = precioTienda1,
                                onValueChange = { viewModel.OnChangedPrecioTienda1(it) },
                                label = { Text(text = "Precio de venta: $nombreTienda1") },
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .padding(vertical = 8.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    if (activaTienda2) {
                        item {
                            TextField(
                                value = precioTienda2,
                                onValueChange = { viewModel.OnChangedPrecioTienda2(it) },
                                label = { Text(text = "Precio de venta: $nombreTienda2") },
                                modifier = Modifier

                                    .padding(horizontal = 32.dp)
                                    .padding(vertical = 8.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    if (activaTienda3) {
                        item {
                            TextField(
                                value = precioTienda3,
                                onValueChange = { viewModel.OnChangedPrecioTienda3(it) },
                                label = { Text(text = "Precio de venta: $nombreTienda3") },
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .padding(vertical = 8.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    if (activaTienda4) {
                        item {
                            TextField(
                                value = precioTienda4,
                                onValueChange = { viewModel.OnChangedPrecioTienda4(it) },
                                label = { Text(text = "Precio de venta: $nombreTienda4") },
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .padding(vertical = 8.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    if (activaTienda5) {
                        item {


                            TextField(
                                value = precioTienda5,
                                onValueChange = { viewModel.OnChangedPrecioTienda5(it) },
                                label = {
                                    Text(
                                        text = "Precio de venta: $nombreTienda5",

                                        )
                                },
                                modifier = Modifier

                                    .padding(horizontal = 32.dp)
                                    .padding(vertical = 8.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),


                                )
                        }
                    }


                }
            }

    Button(modifier = Modifier.align(Alignment.BottomCenter),
        onClick = {
            viewModel.updateProducto()
            viewModel.ventanaEditar(-1)
        },
           colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500))) {
        Text(text = stringResource(R.string.boton_guardar_editar))
    }
        }
        }



}
}


@Composable
fun VistaVentanaDetalles(viewModel: EditarViewModel, activaTienda1: Boolean, activaTienda2: Boolean, activaTienda3: Boolean, activaTienda4: Boolean, activaTienda5: Boolean, producto_detalles: ProductRoom, nombreTienda1: String, nombreTienda2: String, nombreTienda3: String, nombreTienda4: String, nombreTienda5: String) {


    Dialog(onDismissRequest = { viewModel.ventanaDetalles(-1) }) {



            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(8.dp),
               // backgroundColor = MaterialTheme.colors.surface,
                elevation= CardDefaults.cardElevation(10.dp),
                ) {

                Column() {

                    //TITULO EDITAR y BOTON CERRAR
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.titulo_ventana_detalles),
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(modifier = Modifier.align(Alignment.CenterEnd),


                            onClick = { viewModel.ventanaDetalles(-1) }
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Cambiar color",

                                )
                        }


                    }


                    //LISTA DE TIENDAS Y PRECIO
                    LazyColumn() {


                        item {
                            ItemsVentanaDetalles(
                                stringResource(R.string.nombre_producto_detalles),
                                producto_detalles.nombre.toString()
                            )
                        }

                        item {
                            ItemsVentanaDetalles(
                                stringResource(R.string.precio_compra_detalles),
                                producto_detalles.compra.toString()
                            )
                        }


                        if (activaTienda1) {
                            item {
                                ItemsVentanaDetalles(
                                    stringResource(R.string.precio_venta_detalles) + nombreTienda1,
                                    producto_detalles.venta1.toString()
                                )
                            }}

                        if (activaTienda2) {
                            item {
                                ItemsVentanaDetalles(
                                    stringResource(R.string.precio_venta_detalles) + nombreTienda2,
                                    producto_detalles.venta2.toString()
                                )
                            }}

                        if (activaTienda3) {
                            item {
                                ItemsVentanaDetalles(
                                    stringResource(R.string.precio_venta_detalles) + nombreTienda3,
                                    producto_detalles.venta3.toString()
                                )
                            }}

                        if (activaTienda4) {
                            item {
                                ItemsVentanaDetalles(
                                    stringResource(R.string.precio_venta_detalles) + nombreTienda4,
                                    producto_detalles.venta4.toString()
                                )
                            }}

                        if (activaTienda5) {
                            item {
                                ItemsVentanaDetalles(
                                    stringResource(R.string.precio_venta_detalles) + nombreTienda5,
                                    producto_detalles.venta5.toString()
                                )
                            }}


                    }
                }
            }



    }

}


@Composable
fun VistaVentanaBorrar(viewModel: EditarViewModel, producto_borrar: ProductRoom) {


    AlertDialog(
        onDismissRequest = {
            viewModel.ventanaBorrar(-1) },
        confirmButton = {
            TextButton(onClick = {
                viewModel.borrarProducto(producto_borrar)
                viewModel.ventanaBorrar(-1)
            }
            ) {
                Text(text = stringResource(id = R.string.boton_confirmar_borrar), color =  MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.ventanaBorrar(-1)
            }
            ) {
                Text(text = stringResource(id = R.string.boton_confirmar_no_borrar), color =  MaterialTheme.colorScheme.primary)
            }
        },
        title = { Text(text = stringResource(id = R.string.ventana_borrar)) },
        text = { Text(text = producto_borrar.nombre) }
    )

}