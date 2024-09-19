package com.virtualworld.agendadeventas.domain.UseCase

import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.DataSellCore
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepocitory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetResumenStore @Inject constructor(private val repoLocal: LocalRepocitory) {


    data class DatosVestaUI(
        val ganancia: Long = 0,
        val unidades: Int = 0,
        val tienda: String = "",
    )

    //obtener los datos de venta de todas las tiendas activas(ganancia, unidades, nombre de tienda)
    fun getResumeSellStoreActive(
        dateStart: Long?,
        dateEnd: Long?
    ): Flow<NetworkResponseState<List<DatosVestaUI>>> {

        return repoLocal.getResumeSellStoreActive(dateStart, dateEnd).map { response ->

            when (response) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success -> {


                    val datosVestaUI = response.result.map {

                        DatosVestaUI(
                            ganancia = (it.valor - it.compra),
                            unidades = it.unidades,
                            tienda = it.tienda
                        )
                    }

                    NetworkResponseState.Success(datosVestaUI)

                }
            }


        }.flowOn(Dispatchers.IO)


    }
}

