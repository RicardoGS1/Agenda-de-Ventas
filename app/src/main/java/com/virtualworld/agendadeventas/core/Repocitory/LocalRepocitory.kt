package com.virtualworld.agendadeventas.core.Repocitory

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.DataSellCore
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse
import com.virtualworld.agendadeventas.core.Model.StoresActiveCore
import com.virtualworld.agendadeventas.core.source.local.ProductoLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepocitory @Inject constructor(
    private val tiendasLocalDataSource: TiendasLocalDataSource,
    private val vendidoLocalDataSource: VendidoLocalDataSourse,
    private val productoLocalDataSource: ProductoLocalDataSource
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

    fun getAllProductsStore(idStore:Int):Flow<NetworkResponseState<List<ProductStoreCore>>>{

        return  flow {
            emit(NetworkResponseState.Loading)

            try {

               productoLocalDataSource.getAllProducts().collect{listProductAll->

                  val listProductStore = listProductAll.map {

                      when(idStore){
                          1->ProductStoreCore(it.id.toInt(),it.nombre,it.compra,it.venta1)
                          2->ProductStoreCore(it.id.toInt(),it.nombre,it.compra,it.venta2)
                          3->ProductStoreCore(it.id.toInt(),it.nombre,it.compra,it.venta3)
                          4->ProductStoreCore(it.id.toInt(),it.nombre,it.compra,it.venta4)
                          5->ProductStoreCore(it.id.toInt(),it.nombre,it.compra,it.venta5)
                          else -> {ProductStoreCore(-1,"",0,0)}
                      }

                  }
                   emit(NetworkResponseState.Success(listProductStore))

               }

            }catch (e:Exception){
                NetworkResponseState.Error(e)
            }

        }
    }

}