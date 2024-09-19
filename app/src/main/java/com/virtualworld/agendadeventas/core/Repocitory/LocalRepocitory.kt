package com.virtualworld.agendadeventas.core.Repocitory

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.DataSellCore
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse
import com.virtualworld.agendadeventas.core.Model.StoresActiveCore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepocitory @Inject constructor(
    private val tiendasLocalDataSource: TiendasLocalDataSource,
    private val vendidoLocalDataSource: VendidoLocalDataSourse
) {

    //obtener los datos de venta de todas las tiendas activas
    fun getResumeSellStoreActive(
        dateStart:Long?,dateEnd:Long?
    ): Flow<NetworkResponseState<List<DataSellCore>>> {

        return flow {
            emit(NetworkResponseState.Loading)
            try {
                vendidoLocalDataSource.getDatosVentaEntreFechas(dateStart, dateEnd)
                    .collect { listVendidos ->

                        tiendasLocalDataSource.getAllStores().collect { listTiendas ->

                            val resumen = listTiendas.filter { it.activa }.map { tienda ->


                                val ventasTienda =
                                    listVendidos.filter { it.tienda.toLong() == tienda.id - 1 }


                                val compra = ventasTienda.sumOf { it.compra }

                                val valor = ventasTienda.sumOf { it.valor }

                                val unidades = ventasTienda.sumOf { it.unidades }



                                DataSellCore(
                                    compra,
                                    valor,
                                    unidades,
                                    tienda.id.toInt(),
                                    tienda.nombre
                                )


                            }

                            emit(NetworkResponseState.Success(resumen))

                        }

                    }
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }
    }

    fun GetTiendasActivas():Flow<NetworkResponseState<List<StoresActiveCore>>> {


      return flow {
          emit(NetworkResponseState.Loading)
          try{
          tiendasLocalDataSource.getAllStores().collect {listStore->

             val listStoresActive = listStore.filter { it.activa }.map { store->

                 StoresActiveCore(store.id.toInt(),store.nombre)
             }
              emit(NetworkResponseState.Success(listStoresActive))

          }


          }catch (e:Exception){
            NetworkResponseState.Error(e)
          }

      }
    }

}