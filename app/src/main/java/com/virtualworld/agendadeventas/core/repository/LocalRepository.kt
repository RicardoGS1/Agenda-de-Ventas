package com.virtualworld.agendadeventas.core.repository

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ResumeSoldForStoreCore
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.Model.SoldForStoreCore
import com.virtualworld.agendadeventas.core.source.local.StoresLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.SoldLocalDataSource
import com.virtualworld.agendadeventas.core.Model.StoresActiveCore
import com.virtualworld.agendadeventas.core.source.local.ProductsLocalDataSource
import com.virtualworld.agendadeventas.id.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val storesLocalDataSource: StoresLocalDataSource,
    private val soldLocalDataSource: SoldLocalDataSource,
    private val productsLocalDataSource: ProductsLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {


    //obtiene todos los productos de la base de datos
    fun getAllProducts(): Flow<NetworkResponseState<List<ProductRoom>>> =
        flow {
            emit(NetworkResponseState.Loading)
            try {
                productsLocalDataSource.getAllProducts().collect { listProductAll ->
                    emit(NetworkResponseState.Success(listProductAll))
                }
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }.flowOn(ioDispatcher)

    //obtiene todas las ventas realizadas de la base de datos
    fun getAllSold(): Flow<NetworkResponseState<List<SoldRoom>>> =
        flow {
            emit(NetworkResponseState.Loading)
            try {
                soldLocalDataSource.getAllSoldFromTo(null, null).collect { listSoldRoom ->
                    emit(NetworkResponseState.Success(listSoldRoom))
                }
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }.flowOn(ioDispatcher)

    //obtiene las ventas de una tienda especificada por su id
    fun getAllSoldForStore(idStore: Int): Flow<NetworkResponseState<List<SoldForStoreCore>>> =
        flow {
            emit(NetworkResponseState.Loading)
            try {
                soldLocalDataSource.getAllSoldFromTo(null, null).collect { listSoldRoom ->
                    val listSoldForStore = listSoldRoom.filter { it.tienda == idStore }.map {

                        SoldForStoreCore(
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
        }.flowOn(ioDispatcher)


    //obtener los datos sumados de venta de todas las tiendas activas
    fun getResumeSoldStoreActive(
        dateStart: Long?, dateEnd: Long?
    ): Flow<NetworkResponseState<List<ResumeSoldForStoreCore>>> {

        return flow {
            emit(NetworkResponseState.Loading)
            try {
                soldLocalDataSource.getAllSoldFromTo(dateStart, dateEnd)
                    .collect { listVendidos ->

                        storesLocalDataSource.getAllStores().collect { listTiendas ->

                            val resume = listTiendas.filter { it.activa }.map { tienda ->


                                val ventasTienda =
                                    listVendidos.filter { it.tienda.toLong() == tienda.id }


                                val compra = ventasTienda.sumOf { it.compra * it.unidades }

                                val valor = ventasTienda.sumOf { it.valor * it.unidades }

                                val unidades = ventasTienda.sumOf { it.unidades }


                                ResumeSoldForStoreCore(
                                    compra,
                                    valor,
                                    unidades,
                                    tienda.id.toInt(),
                                    tienda.nombre
                                )


                            }

                            emit(NetworkResponseState.Success(resume))

                        }

                    }
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }.flowOn(ioDispatcher)
    }

    //ogtiene las tiendas activas
    fun getStoresActive(): Flow<NetworkResponseState<List<StoresActiveCore>>> =
        flow {
            emit(NetworkResponseState.Loading)
            try {
                storesLocalDataSource.getAllStores().collect { listStore ->

                    val listStoresActive = listStore.filter { it.activa }.map { store ->

                        StoresActiveCore(store.id.toInt(), store.nombre)
                    }
                    emit(NetworkResponseState.Success(listStoresActive))
                }
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }

        }.flowOn(ioDispatcher)

    //ogtiene las tiendas
    fun getAllStores(): Flow<NetworkResponseState<List<StoreRoom>>> =
        flow {
            emit(NetworkResponseState.Loading)
            try {
                storesLocalDataSource.getAllStores().collect { listStore ->
                    emit(NetworkResponseState.Success(listStore))
                }
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }

        }.flowOn(ioDispatcher)

    suspend fun updateStores(storeRoom: List<StoreRoom>) {
        storesLocalDataSource.updateStores(storeRoom)
    }



    //obtiene los productos por tienda especificada por id de tienda
    fun getAllProductsStore(idStore: Int): Flow<NetworkResponseState<List<ProductStoreCore>>> {
        return flow {
            emit(NetworkResponseState.Loading)

            try {

                productsLocalDataSource.getAllProducts().collect { listProductAll ->

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

        }.flowOn(ioDispatcher)
    }

    suspend fun insertListSales(listSoldRoom: List<SoldRoom>) {
        soldLocalDataSource.addProductsSold(listSoldRoom)
    }

    suspend fun deleteAllSales() {
        soldLocalDataSource.cleanSales()
    }

    suspend fun deleteAllProducts() {
        productsLocalDataSource.deletedAllProduct()
    }

    suspend fun insertListProducts(result: List<ProductRoom>) {
        productsLocalDataSource.insertListProduct(result)
    }

    suspend fun addProduct(productRoom: ProductRoom): NetworkResponseState<Unit> {
        return productsLocalDataSource.addProduct(productRoom)
    }

    suspend fun deleteProduct(id:Int){
        productsLocalDataSource.deleteProductById(id)
    }



}