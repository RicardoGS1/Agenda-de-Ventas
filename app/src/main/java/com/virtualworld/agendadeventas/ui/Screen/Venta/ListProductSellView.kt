package com.virtualworld.agendadeventas.ui.Screen.Venta

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore


@Composable
fun ListProductSellView(stateScreenProductStore: List<ProductStoreCore>) {




    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 46.dp)
    ) {

        itemsIndexed(items = stateScreenProductStore) { index, nombres ->
            TtemProducto(index, nombres )
        }
    }
}


@Composable
fun TtemProducto(index:Int, nombres: ProductStoreCore){




    Card(modifier = Modifier.padding(4.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                text = nombres.nombre,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold

            )

            Text(
                text = nombres.venta.toString(),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold

            )


            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = {
                       // viewModel.resUnidades(index)
                       // viewModel.actualisarya()
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)) ,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(0.dp)
                ) {
                    Text(text = "-", fontSize = 32.sp)
                }

                Text(

                    text = "99",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                )

                Button(
                    onClick = {
                       // viewModel.addUnidades(index)
                       // viewModel.actualisarya()
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)) ,
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
