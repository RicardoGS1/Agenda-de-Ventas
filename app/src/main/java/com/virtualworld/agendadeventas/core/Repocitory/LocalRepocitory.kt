package com.virtualworld.agendadeventas.core.Repocitory

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ResumeSoldForStoreCore
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.Model.SoldForStore
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

    suspend fun insertListSales(listSoldRoom: List<SoldRoom>){
        vendidoLocalDataSource.addProductoVendido(listSoldRoom)
    }

    fun getAllSold(): Flow<NetworkResponseState<List<SoldRoom>>> {



        return flow {

            emit(NetworkResponseState.Loading)

            try {


                vendidoLocalDataSource.getAllSoldFromTo(null, null).collect { listSoldRoom ->


                    emit(NetworkResponseState.Success(listSoldRoom))
                }


            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }


        }


    }


    fun getAllSoldForStore(idStore: Int): Flow<NetworkResponseState<List<SoldForStore>>> {

        println(idStore.toString() + "localrepocitry")

        return flow {

            emit(NetworkResponseState.Loading)

            try {


                vendidoLocalDataSource.getAllSoldFromTo(null, null).collect { listSoldRoom ->

                    println(listSoldRoom + "localrepocitry")

                   val listSoldForStore = listSoldRoom.filter { it.tienda == idStore }.map {

                        SoldForStore(
                            idbd = it.idbd,
                            idprod = it.idprod,
                            nombre = it.nombre,
                            compra = it.compra,
                            valor = it.valor,
                            unidades = it.unidades,
                            fecha = it.fecha,
                            )
                    }
                    emit(NetworkResponseState.Success(listSoldForStore))
                }


            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }


        }


    }

    //obtener los datos sumados de venta de todas las tiendas activas
    fun getResumeSoldStoreActive(
        dateStart: Long?, dateEnd: Long?
    ): Flow<NetworkResponseState<List<ResumeSoldForStoreCore>>> {

        return flow {
            emit(NetworkResponseState.Loading)
            try {
                vendidoLocalDataSource.getAllSoldFromTo(dateStart, dateEnd)
                    .collect { listVendidos ->

                        tiendasLocalDataSource.getAllStores().collect { listTiendas ->

                            val resumen = listTiendas.filter { it.activa }.map { tienda ->


                                val ventasTienda =
                                    listVendidos.filter { it.tienda.toLong() == tienda.id }


                                val compra = ventasTienda.sumOf { it.compra }

                                val valor = ventasTienda.sumOf { it.valor }

                                val unidades = ventasTienda.sumOf { it.unidades }


                                ResumeSoldForStoreCore(
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

    fun GetTiendasActivas(): Flow<NetworkResponseState<List<StoresActiveCore>>> {


        return flow {
            emit(NetworkResponseState.Loading)
            try {
                tiendasLocalDataSource.getAllStores().collect { listStore ->

                    val listStoresActive = listStore.filter { it.activa }.map { store ->

                        StoresActiveCore(store.id.toInt(), store.nombre)
                    }
                    emit(NetworkResponseState.Success(listStoresActive))

                }


            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }

        }
    }


    fun getAllProductsRoom(): Flow<NetworkResponseState<List<ProductRoom>>> {

        return flow {
            emit(NetworkResponseState.Loading)

            try {

                productoLocalDataSource.getAllProducts().collect { listProductAll ->


                    emit(NetworkResponseState.Success(listProductAll))

                }

            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }

        }
    }



    fun getAllProductsStore(idStore: Int): Flow<NetworkResponseState<List<ProductStoreCore>>> {

        return flow {
            emit(NetworkResponseState.Loading)

            try {

                productoLocalDataSource.getAllProducts().collect { listProductAll ->

                    val listProductStore = listProductAll.map {

                        when (idStore) {
                            1 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta1)
                            2 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta2)
                            3 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta3)
                            4 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta4)
                            5 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta5)
                            else -> {
                                ProductStoreCore(-1, "", 0, 0)
                            }
                        }

                    }
                    emit(NetworkResponseState.Success(listProductStore))

                }

            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }

        }
    }

    suspend fun deleteAllSales(){
        vendidoLocalDataSource.cleanSales()
    }

    suspend fun deleteAllProducts() {
        productoLocalDataSource.deletedAllProduct()
    }

    suspend fun insertListProducts(result: List<ProductRoom>) {

        productoLocalDataSource.insertListProduct(result)

    }

}