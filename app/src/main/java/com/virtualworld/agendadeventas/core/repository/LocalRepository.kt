package com.virtualworld.agendadeventas.core.repository

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.entity.CashTransactionRoom
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.core.entity.PurchaseRoom
import com.virtualworld.agendadeventas.core.entity.inventoryForStore
import com.virtualworld.agendadeventas.core.entity.salePriceForStore
import com.virtualworld.agendadeventas.core.model.BusinessAnalysisSnapshot
import com.virtualworld.agendadeventas.core.model.ProductInventoryItem
import com.virtualworld.agendadeventas.core.model.ProductSalesData
import com.virtualworld.agendadeventas.core.model.StoreAnalysisData
import com.virtualworld.agendadeventas.core.model.AnalysisTotals
import com.virtualworld.agendadeventas.core.model.InventorySnapshotData
import com.virtualworld.agendadeventas.core.model.CashOperationCore
import com.virtualworld.agendadeventas.core.model.CashOperationType
import com.virtualworld.agendadeventas.core.model.ProductStoreCore
import com.virtualworld.agendadeventas.core.model.PurchaseForStoreCore
import com.virtualworld.agendadeventas.core.model.ResumeInventoryForStoreCore
import com.virtualworld.agendadeventas.core.model.ResumeSoldForStoreCore
import com.virtualworld.agendadeventas.core.model.SoldForStoreCore
import com.virtualworld.agendadeventas.core.model.StoreCashSummaryCore
import com.virtualworld.agendadeventas.core.model.StoresActiveCore
import com.virtualworld.agendadeventas.core.product.ProductImageStorage
import com.virtualworld.agendadeventas.core.source.local.CashLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.ProductsLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.PurchaseLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.SoldLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.StoresLocalDataSource
import com.virtualworld.agendadeventas.id.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val storesLocalDataSource: StoresLocalDataSource,
    private val soldLocalDataSource: SoldLocalDataSource,
    private val cashLocalDataSource: CashLocalDataSource,
    private val purchaseLocalDataSource: PurchaseLocalDataSource,
    private val productsLocalDataSource: ProductsLocalDataSource,
    private val productImageStorage: ProductImageStorage,
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
                combine(
                    soldLocalDataSource.getAllSoldFromTo(null, null),
                    productsLocalDataSource.getAllProducts(),
                ) { listSoldRoom, listProducts ->
                    val imageByProductId = listProducts.associate { it.id to it.imagePath }
                    listSoldRoom
                        .filter { it.tienda == idStore }
                        .map { sold ->
                            SoldForStoreCore(
                                idbd = sold.idbd,
                                idprod = sold.idprod,
                                nombre = sold.nombre,
                                compra = sold.compra,
                                valor = sold.valor,
                                unidades = sold.unidades,
                                fecha = sold.fecha,
                                imagePath = imageByProductId[sold.idprod],
                            )
                        }
                }.collect { listSoldForStore ->
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
              combine(
                  soldLocalDataSource.getAllSoldFromTo(dateStart, dateEnd),
                  storesLocalDataSource.getAllStores()
              ) { listVendidos, listTiendas ->

                  NetworkResponseState.Success(
                      listTiendas
                          .filter { it.activa }
                          .map {tienda ->
                              calcularResumenVentasTienda(tienda, listVendidos)
                          }
                  )
              }.collect { emit(it) }
          } catch (e:Exception){
              emit(NetworkResponseState.Error(e))
          }

        }.flowOn(ioDispatcher)


    }

    fun getResumeInventoryStoreActive(): Flow<NetworkResponseState<List<ResumeInventoryForStoreCore>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                combine(
                    productsLocalDataSource.getAllProducts(),
                    storesLocalDataSource.getAllStores(),
                ) { listProducts, listStores ->
                    NetworkResponseState.Success(
                        listStores
                            .filter { it.activa }
                            .map { tienda ->
                                calcularResumenInventarioTienda(tienda, listProducts)
                            },
                    )
                }.collect { emit(it) }
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }.flowOn(ioDispatcher)
    }

    private fun calcularResumenVentasTienda(
        tienda: StoreRoom,
        listVendidos: List<SoldRoom>
    ): ResumeSoldForStoreCore {
        val ventasTienda = listVendidos.filter { it.tienda.toLong() == tienda.id }
        val compra = ventasTienda.fold(0f) { acc, sold -> acc + (sold.compra * sold.unidades) }
        val valor = ventasTienda.fold(0f) { acc, sold -> acc + (sold.valor * sold.unidades) }
        val unidades = ventasTienda.sumOf { it.unidades }
        return ResumeSoldForStoreCore(compra, valor, unidades, tienda.id.toInt(), tienda.nombre)
    }

    private fun calcularResumenInventarioTienda(
        tienda: StoreRoom,
        products: List<ProductRoom>,
    ): ResumeInventoryForStoreCore {
        val storeId = tienda.id.toInt()
        var unidades = 0
        var inversion = 0f
        var ganancia = 0f

        products.filter { it.tieneInventario }.forEach { product ->
            val stock = product.inventoryForStore(storeId)
            val venta = product.salePriceForStore(storeId)
            unidades += stock
            inversion += product.compra * stock
            ganancia += (venta - product.compra) * stock
        }

        return ResumeInventoryForStoreCore(
            unidades = unidades,
            inversion = inversion,
            ganancia = ganancia,
            idTienda = storeId,
            tienda = tienda.nombre,
        )
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
                            1 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta1, it.tieneInventario, it.inventario1, it.imagePath)
                            2 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta2, it.tieneInventario, it.inventario2, it.imagePath)
                            3 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta3, it.tieneInventario, it.inventario3, it.imagePath)
                            4 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta4, it.tieneInventario, it.inventario4, it.imagePath)
                            5 -> ProductStoreCore(it.id.toInt(), it.nombre, it.compra, it.venta5, it.tieneInventario, it.inventario5, it.imagePath)
                            else -> {
                                ProductStoreCore(-1, "", 0f, 0f, false, 0, null)
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

    suspend fun deleteAllPurchases() {
        purchaseLocalDataSource.cleanAll()
    }

    suspend fun deleteAllCashTransactions() {
        cashLocalDataSource.cleanAll()
    }

    suspend fun insertListPurchases(purchases: List<PurchaseRoom>) {
        purchaseLocalDataSource.insertAll(purchases)
    }

    suspend fun insertListCashTransactions(transactions: List<CashTransactionRoom>) {
        cashLocalDataSource.insertAll(transactions)
    }

    suspend fun getAllProductsOnce(): List<ProductRoom> {
        return productsLocalDataSource.getAllProducts().first()
    }

    suspend fun insertListProducts(result: List<ProductRoom>) {
        productsLocalDataSource.insertListProduct(result)
    }

    suspend fun addProduct(productRoom: ProductRoom): NetworkResponseState<Unit> {
        return productsLocalDataSource.addProduct(productRoom)
    }

    suspend fun deleteProduct(id: Int) {
        val product = productsLocalDataSource.getProductById(id.toLong())
        productsLocalDataSource.deleteProductById(id)
        productImageStorage.deleteImage(product?.imagePath)
    }

    suspend fun adjustInventory(id: Long, storeId: Int, delta: Int) {
        productsLocalDataSource.adjustInventory(id, storeId, delta)
    }

    suspend fun recordPurchase(
        productId: Long,
        productName: String,
        unitCost: Float,
        storeId: Int,
        units: Int,
        deductedFromCash: Boolean,
    ) {
        if (units <= 0 || storeId !in 1..5) return
        purchaseLocalDataSource.insert(
            PurchaseRoom(
                idprod = productId,
                nombre = productName,
                compra = unitCost,
                tienda = storeId,
                unidades = units,
                fecha = System.currentTimeMillis(),
                deductedFromCash = deductedFromCash,
            ),
        )
    }

    fun getAllPurchasesForStore(idStore: Int): Flow<NetworkResponseState<List<PurchaseForStoreCore>>> =
        flow {
            emit(NetworkResponseState.Loading)
            try {
                combine(
                    purchaseLocalDataSource.getByStore(idStore),
                    productsLocalDataSource.getAllProducts(),
                ) { purchases, listProducts ->
                    val imageByProductId = listProducts.associate { it.id to it.imagePath }
                    purchases.map { purchase ->
                        PurchaseForStoreCore(
                            id = purchase.id,
                            idprod = purchase.idprod,
                            nombre = purchase.nombre,
                            compra = purchase.compra,
                            unidades = purchase.unidades,
                            fecha = purchase.fecha,
                            deductedFromCash = purchase.deductedFromCash,
                            imagePath = imageByProductId[purchase.idprod],
                        )
                    }
                }.collect { listPurchasesForStore ->
                    emit(NetworkResponseState.Success(listPurchasesForStore))
                }
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }.flowOn(ioDispatcher)

    fun getPurchasesForProduct(
        storeId: Int,
        productId: Long,
    ): Flow<NetworkResponseState<List<PurchaseForStoreCore>>> = flow {
        emit(NetworkResponseState.Loading)
        try {
            combine(
                purchaseLocalDataSource.getByStoreAndProduct(storeId, productId),
                productsLocalDataSource.getAllProducts(),
            ) { purchases, listProducts ->
                val imageByProductId = listProducts.associate { it.id to it.imagePath }
                purchases.map { purchase ->
                    PurchaseForStoreCore(
                        id = purchase.id,
                        idprod = purchase.idprod,
                        nombre = purchase.nombre,
                        compra = purchase.compra,
                        unidades = purchase.unidades,
                        fecha = purchase.fecha,
                        deductedFromCash = purchase.deductedFromCash,
                        imagePath = imageByProductId[purchase.idprod],
                    )
                }
            }.collect { mappedPurchases ->
                emit(NetworkResponseState.Success(mappedPurchases))
            }
        } catch (e: Exception) {
            emit(NetworkResponseState.Error(e))
        }
    }.flowOn(ioDispatcher)

    suspend fun getProductById(id: Long): ProductRoom? {
        return productsLocalDataSource.getProductById(id)
    }

    fun getStoreCashSummaries(): Flow<NetworkResponseState<List<StoreCashSummaryCore>>> = flow {
        emit(NetworkResponseState.Loading)
        try {
            combine(
                soldLocalDataSource.getAllSoldFromTo(null, null),
                cashLocalDataSource.getAllTransactions(),
                storesLocalDataSource.getAllStores(),
            ) { sales, cashTransactions, stores ->
                stores
                    .filter { it.activa }
                    .map { store ->
                        val storeId = store.id.toInt()
                        val storeSales = sales.filter { it.tienda == storeId }
                        val storeCash = cashTransactions.filter { it.storeId == storeId }
                        val salesTotal = storeSales.fold(0f) { acc, sold ->
                            acc + (sold.valor * sold.unidades)
                        }
                        val depositsTotal = storeCash
                            .filter { it.type == CashTransactionRoom.TYPE_DEPOSIT }
                            .sumOf { it.amount.toDouble() }
                            .toFloat()
                        val withdrawalsTotal = storeCash
                            .filter { it.type == CashTransactionRoom.TYPE_WITHDRAWAL }
                            .sumOf { it.amount.toDouble() }
                            .toFloat()
                        StoreCashSummaryCore(
                            storeId = storeId,
                            storeName = store.nombre,
                            salesTotal = salesTotal,
                            depositsTotal = depositsTotal,
                            withdrawalsTotal = withdrawalsTotal,
                        )
                    }
            }.collect { summaries ->
                emit(NetworkResponseState.Success(summaries))
            }
        } catch (e: Exception) {
            emit(NetworkResponseState.Error(e))
        }
    }.flowOn(ioDispatcher)

    fun getStoreCashOperations(storeId: Int): Flow<NetworkResponseState<List<CashOperationCore>>> = flow {
        emit(NetworkResponseState.Loading)
        try {
            combine(
                soldLocalDataSource.getAllSoldFromTo(null, null),
                cashLocalDataSource.getTransactionsByStore(storeId),
            ) { sales, cashTransactions ->
                val saleOperations = sales
                    .filter { it.tienda == storeId }
                    .map { sold ->
                        val total = sold.valor * sold.unidades
                        CashOperationCore(
                            id = sold.idbd,
                            type = CashOperationType.SALE,
                            amount = total,
                            signedAmount = total,
                            description = sold.nombre,
                            fecha = sold.fecha,
                            units = sold.unidades,
                        )
                    }
                val cashOperations = cashTransactions.map { transaction ->
                    val isDeposit = transaction.type == CashTransactionRoom.TYPE_DEPOSIT
                    CashOperationCore(
                        id = transaction.id,
                        type = if (isDeposit) CashOperationType.DEPOSIT else CashOperationType.WITHDRAWAL,
                        amount = transaction.amount,
                        signedAmount = if (isDeposit) transaction.amount else -transaction.amount,
                        description = transaction.note.ifBlank {
                            if (isDeposit) "" else ""
                        },
                        fecha = transaction.fecha,
                    )
                }
                (saleOperations + cashOperations).sortedByDescending { it.fecha }
            }.collect { operations ->
                emit(NetworkResponseState.Success(operations))
            }
        } catch (e: Exception) {
            emit(NetworkResponseState.Error(e))
        }
    }.flowOn(ioDispatcher)

    suspend fun addCashDeposit(storeId: Int, amount: Float, note: String = ""): NetworkResponseState<Unit> {
        return try {
            if (amount <= 0f) {
                return NetworkResponseState.Error(IllegalArgumentException("Amount must be positive"))
            }
            cashLocalDataSource.insertTransaction(
                CashTransactionRoom(
                    storeId = storeId,
                    amount = amount,
                    type = CashTransactionRoom.TYPE_DEPOSIT,
                    fecha = System.currentTimeMillis(),
                    note = note.trim(),
                ),
            )
            NetworkResponseState.Success(Unit)
        } catch (e: Exception) {
            NetworkResponseState.Error(e)
        }
    }

    suspend fun withdrawCash(storeId: Int, amount: Float, note: String = ""): NetworkResponseState<Unit> {
        return try {
            if (amount <= 0f) {
                return NetworkResponseState.Error(IllegalArgumentException("Amount must be positive"))
            }
            val balance = getCurrentBalanceForStore(storeId)
            if (amount > balance) {
                return NetworkResponseState.Error(InsufficientCashException())
            }
            cashLocalDataSource.insertTransaction(
                CashTransactionRoom(
                    storeId = storeId,
                    amount = amount,
                    type = CashTransactionRoom.TYPE_WITHDRAWAL,
                    fecha = System.currentTimeMillis(),
                    note = note.trim(),
                ),
            )
            NetworkResponseState.Success(Unit)
        } catch (e: Exception) {
            NetworkResponseState.Error(e)
        }
    }

    suspend fun getBusinessAnalysisSnapshot(
        dateStart: Long,
        dateEnd: Long,
    ): BusinessAnalysisSnapshot = withContext(ioDispatcher) {
        val sales = soldLocalDataSource.getAllSoldFromTo(dateStart, dateEnd).first()
        val purchases = purchaseLocalDataSource.getAll().first()
            .filter { it.fecha in dateStart..dateEnd }
        val cashTransactions = cashLocalDataSource.getAllTransactions().first()
            .filter { it.fecha in dateStart..dateEnd }
        val stores = storesLocalDataSource.getAllStores().first().filter { it.activa }
        val products = productsLocalDataSource.getAllProducts().first()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val periodLabel = "${dateFormat.format(Date(dateStart))} - ${dateFormat.format(Date(dateEnd))}"

        val storeData = stores.map { store ->
            val storeId = store.id.toInt()
            val storeSales = sales.filter { it.tienda == storeId }
            val storePurchases = purchases.filter { it.tienda == storeId }
            val storeCash = cashTransactions.filter { it.storeId == storeId }

            val revenue = storeSales.fold(0f) { acc, sold -> acc + (sold.valor * sold.unidades) }
            val cost = storeSales.fold(0f) { acc, sold -> acc + (sold.compra * sold.unidades) }

            StoreAnalysisData(
                storeId = storeId,
                storeName = store.nombre,
                unitsSold = storeSales.sumOf { it.unidades },
                costOfGoodsSold = cost,
                revenue = revenue,
                profit = revenue - cost,
                purchasesUnits = storePurchases.sumOf { it.unidades },
                purchasesCost = storePurchases.fold(0f) { acc, p -> acc + (p.compra * p.unidades) },
                cashDeposits = storeCash
                    .filter { it.type == CashTransactionRoom.TYPE_DEPOSIT }
                    .sumOf { it.amount.toDouble() }
                    .toFloat(),
                cashWithdrawals = storeCash
                    .filter { it.type == CashTransactionRoom.TYPE_WITHDRAWAL }
                    .sumOf { it.amount.toDouble() }
                    .toFloat(),
            )
        }

        val productSalesMap = sales.groupBy { it.nombre }.mapValues { (_, items) ->
            val revenue = items.fold(0f) { acc, sold -> acc + (sold.valor * sold.unidades) }
            val cost = items.fold(0f) { acc, sold -> acc + (sold.compra * sold.unidades) }
            ProductSalesData(
                productName = items.first().nombre,
                unitsSold = items.sumOf { it.unidades },
                revenue = revenue,
                profit = revenue - cost,
            )
        }

        val topProducts = productSalesMap.values
            .sortedByDescending { it.unitsSold }
            .take(15)

        val soldProductNames = productSalesMap.keys
        val allProductNames = products.map { it.nombre }.toSet()
        val lowRotationProducts = (allProductNames + soldProductNames)
            .distinct()
            .map { name ->
                productSalesMap[name] ?: ProductSalesData(name, 0, 0f, 0f)
            }
            .filter { it.unitsSold <= 2 }
            .sortedBy { it.unitsSold }

        val inventoryByStore = stores.map { store ->
            val storeId = store.id.toInt()
            var totalUnits = 0
            var investment = 0f
            var potentialProfit = 0f
            val lowStock = mutableListOf<ProductInventoryItem>()

            products.filter { it.tieneInventario }.forEach { product ->
                val stock = product.inventoryForStore(storeId)
                val salePrice = product.salePriceForStore(storeId)
                totalUnits += stock
                investment += product.compra * stock
                potentialProfit += (salePrice - product.compra) * stock
                if (stock in 0..5) {
                    lowStock.add(
                        ProductInventoryItem(
                            name = product.nombre,
                            stock = stock,
                            purchasePrice = product.compra,
                            salePrice = salePrice,
                        ),
                    )
                }
            }

            InventorySnapshotData(
                storeId = storeId,
                storeName = store.nombre,
                totalUnits = totalUnits,
                investment = investment,
                potentialProfit = potentialProfit,
                lowStockProducts = lowStock.sortedBy { it.stock },
            )
        }

        val totals = AnalysisTotals(
            totalUnitsSold = storeData.sumOf { it.unitsSold },
            totalRevenue = storeData.fold(0f) { acc, store -> acc + store.revenue },
            totalProfit = storeData.fold(0f) { acc, store -> acc + store.profit },
            totalPurchasesCost = storeData.fold(0f) { acc, store -> acc + store.purchasesCost },
            totalPurchasesUnits = storeData.sumOf { it.purchasesUnits },
            totalCashDeposits = storeData.fold(0f) { acc, store -> acc + store.cashDeposits },
            totalCashWithdrawals = storeData.fold(0f) { acc, store -> acc + store.cashWithdrawals },
        )

        BusinessAnalysisSnapshot(
            periodStartMillis = dateStart,
            periodEndMillis = dateEnd,
            periodLabel = periodLabel,
            stores = storeData,
            topProducts = topProducts,
            lowRotationProducts = lowRotationProducts,
            inventoryByStore = inventoryByStore,
            totals = totals,
        )
    }

    private suspend fun getCurrentBalanceForStore(storeId: Int): Float {
        return combine(
            soldLocalDataSource.getAllSoldFromTo(null, null),
            cashLocalDataSource.getTransactionsByStore(storeId),
        ) { salesList, cashList ->
            val salesTotal = salesList
                .filter { it.tienda == storeId }
                .fold(0f) { acc, sold -> acc + (sold.valor * sold.unidades) }
            val deposits = cashList
                .filter { it.type == CashTransactionRoom.TYPE_DEPOSIT }
                .sumOf { it.amount.toDouble() }
                .toFloat()
            val withdrawals = cashList
                .filter { it.type == CashTransactionRoom.TYPE_WITHDRAWAL }
                .sumOf { it.amount.toDouble() }
                .toFloat()
            salesTotal + deposits - withdrawals
        }.first()
    }
}

class InsufficientCashException : Exception()