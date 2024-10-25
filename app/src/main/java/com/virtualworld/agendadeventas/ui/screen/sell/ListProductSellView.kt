package com.virtualworld.agendadeventas.ui.screen.sell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore


@Composable
fun ListProductSellView(
    stateScreenProductStore: List<ProductStoreCore>,
    changeUnitSell: (Pair<Int, Int>) -> Unit,
    listChangerProductsSell: List<Pair<Int, Int>>
) {


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 46.dp)
    ) {

        itemsIndexed(items = stateScreenProductStore) { index, nombres ->

            val a = listChangerProductsSell.filter {

                it.first == nombres.idProduct
            }.firstOrNull()

            TtemProducto( nombres, changeUnitSell, a)
        }
    }
}


@Composable
fun TtemProducto(
    nombres: ProductStoreCore,
    changeUnitSell: (Pair<Int, Int>) -> Unit,
    listChangerProductsSell: Pair<Int, Int>?,

    ) {


    Card(modifier = Modifier.padding(4.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                Text(
                    text = nombres.nombre,
                    Modifier.padding(horizontal = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold

                )

                Row {


                    Text(
                        text = stringResource(id = R.string.sell_item_detall_cost),

                        fontSize = 16.sp,
                        fontWeight = FontWeight.Thin

                    )

                    Text(
                        text = nombres.compra.toString(),

                        fontSize = 16.sp,
                       // fontWeight = FontWeight.Bold

                    )

                    Spacer(Modifier.size(10.dp))

                    Text(
                        text = stringResource(id = R.string.sell_item_detall_price),

                        fontSize = 16.sp,
                        fontWeight = FontWeight.Thin

                    )

                    Text(
                        text = nombres.venta.toString(),

                        fontSize = 16.sp,
                     //   fontWeight = FontWeight.Bold

                    )


                }
            }


            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = {
                        changeUnitSell(Pair(nombres.idProduct, -1))
                        // viewModel.resUnidades(index)
                        // viewModel.actualisarya()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(0.dp)
                ) {
                    Text(text = "-", fontSize = 32.sp, modifier = Modifier.padding(horizontal = 4.dp))
                }

                Text(

                    text = listChangerProductsSell?.let { it.second.toString() } ?: "null",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                )

                Button(
                    onClick = {
                        changeUnitSell(Pair(nombres.idProduct, 1))
                        // viewModel.addUnidades(index)
                        // viewModel.actualisarya()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(0.dp),
                ) {
                    Text(text = "+", fontSize = 32.sp)

                }
            }
        }
    }
}
