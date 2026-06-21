package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.core.entity.CashTransactionRoom
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.core.entity.PurchaseRoom
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SeedTestDataUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(): ScreenUiState {
        return try {
            localRepository.deleteAllSales()
            localRepository.deleteAllPurchases()
            localRepository.deleteAllCashTransactions()
            localRepository.deleteAllProducts()

            val stores = listOf(
                StoreRoom(1, "Tienda Centro", true),
                StoreRoom(2, "Tienda Norte", true),
                StoreRoom(3, "Tienda Sur", true),
                StoreRoom(4, "Almacén A", false),
                StoreRoom(5, "Almacén B", false),
            )
            localRepository.updateStores(stores)

            val products = listOf(
                ProductRoom(
                    nombre = "Café molido 500g",
                    compra = 8f,
                    venta1 = 15f,
                    venta2 = 14f,
                    venta3 = 16f,
                    venta4 = 13f,
                    venta5 = 13.5f,
                    tieneInventario = true,
                    inventario1 = 25,
                    inventario2 = 30,
                    inventario3 = 20,
                ),
                ProductRoom(
                    nombre = "Azúcar 1 kg",
                    compra = 5f,
                    venta1 = 10f,
                    venta2 = 9.5f,
                    venta3 = 11f,
                    venta4 = 9f,
                    venta5 = 9.5f,
                    tieneInventario = true,
                    inventario1 = 40,
                    inventario2 = 35,
                    inventario3 = 45,
                ),
                ProductRoom(
                    nombre = "Servicio de reparación",
                    compra = 0f,
                    venta1 = 50f,
                    venta2 = 55f,
                    venta3 = 48f,
                    venta4 = 52f,
                    venta5 = 50f,
                    tieneInventario = false,
                ),
                ProductRoom(
                    nombre = "Aceite 900 ml",
                    compra = 12f,
                    venta1 = 22f,
                    venta2 = 21f,
                    venta3 = 23f,
                    venta4 = 20f,
                    venta5 = 21.5f,
                    tieneInventario = true,
                    inventario1 = 15,
                    inventario2 = 18,
                    inventario3 = 12,
                ),
                ProductRoom(
                    nombre = "Galletas surtidas",
                    compra = 3.5f,
                    venta1 = 7f,
                    venta2 = 6.5f,
                    venta3 = 7.5f,
                    venta4 = 6f,
                    venta5 = 6.5f,
                    tieneInventario = true,
                    inventario1 = 60,
                    inventario2 = 55,
                    inventario3 = 50,
                ),
            )
            localRepository.insertListProducts(products)

            val savedProducts = localRepository.getAllProductsOnce()
            val productByName = savedProducts.associateBy { it.nombre }

            val cafe = productByName["Café molido 500g"] ?: return ScreenUiState.ERROR
            val azucar = productByName["Azúcar 1 kg"] ?: return ScreenUiState.ERROR
            val servicio = productByName["Servicio de reparación"] ?: return ScreenUiState.ERROR
            val aceite = productByName["Aceite 900 ml"] ?: return ScreenUiState.ERROR
            val galletas = productByName["Galletas surtidas"] ?: return ScreenUiState.ERROR

            val now = System.currentTimeMillis()
            val daysAgo = { days: Long -> now - TimeUnit.DAYS.toMillis(days) }

            localRepository.insertListSales(
                listOf(
                    SoldRoom(
                        idprod = servicio.id,
                        nombre = servicio.nombre,
                        compra = servicio.compra,
                        valor = servicio.venta1,
                        tienda = 1,
                        unidades = 1,
                        fecha = daysAgo(5),
                    ),
                    SoldRoom(
                        idprod = cafe.id,
                        nombre = cafe.nombre,
                        compra = cafe.compra,
                        valor = cafe.venta1,
                        tienda = 1,
                        unidades = 5,
                        fecha = daysAgo(3),
                    ),
                    SoldRoom(
                        idprod = azucar.id,
                        nombre = azucar.nombre,
                        compra = azucar.compra,
                        valor = azucar.venta1,
                        tienda = 1,
                        unidades = 10,
                        fecha = daysAgo(2),
                    ),
                    SoldRoom(
                        idprod = aceite.id,
                        nombre = aceite.nombre,
                        compra = aceite.compra,
                        valor = aceite.venta2,
                        tienda = 2,
                        unidades = 3,
                        fecha = daysAgo(1),
                    ),
                    SoldRoom(
                        idprod = galletas.id,
                        nombre = galletas.nombre,
                        compra = galletas.compra,
                        valor = galletas.venta3,
                        tienda = 3,
                        unidades = 20,
                        fecha = now,
                    ),
                    SoldRoom(
                        idprod = cafe.id,
                        nombre = cafe.nombre,
                        compra = cafe.compra,
                        valor = cafe.venta2,
                        tienda = 2,
                        unidades = 8,
                        fecha = daysAgo(4),
                    ),
                ),
            )

            localRepository.insertListPurchases(
                listOf(
                    PurchaseRoom(
                        idprod = cafe.id,
                        nombre = cafe.nombre,
                        compra = cafe.compra,
                        tienda = 1,
                        unidades = 10,
                        fecha = daysAgo(7),
                        deductedFromCash = true,
                    ),
                    PurchaseRoom(
                        idprod = azucar.id,
                        nombre = azucar.nombre,
                        compra = azucar.compra,
                        tienda = 2,
                        unidades = 15,
                        fecha = daysAgo(4),
                        deductedFromCash = false,
                    ),
                    PurchaseRoom(
                        idprod = galletas.id,
                        nombre = galletas.nombre,
                        compra = galletas.compra,
                        tienda = 3,
                        unidades = 30,
                        fecha = daysAgo(2),
                        deductedFromCash = true,
                    ),
                    PurchaseRoom(
                        idprod = aceite.id,
                        nombre = aceite.nombre,
                        compra = aceite.compra,
                        tienda = 1,
                        unidades = 5,
                        fecha = daysAgo(6),
                        deductedFromCash = true,
                    ),
                ),
            )

            localRepository.insertListCashTransactions(
                listOf(
                    CashTransactionRoom(
                        storeId = 1,
                        amount = 500f,
                        type = CashTransactionRoom.TYPE_DEPOSIT,
                        fecha = daysAgo(10),
                        note = "Capital inicial",
                    ),
                    CashTransactionRoom(
                        storeId = 1,
                        amount = 100f,
                        type = CashTransactionRoom.TYPE_WITHDRAWAL,
                        fecha = daysAgo(3),
                        note = "Gastos operativos",
                    ),
                    CashTransactionRoom(
                        storeId = 2,
                        amount = 300f,
                        type = CashTransactionRoom.TYPE_DEPOSIT,
                        fecha = daysAgo(6),
                        note = "Aporte socio",
                    ),
                    CashTransactionRoom(
                        storeId = 3,
                        amount = 200f,
                        type = CashTransactionRoom.TYPE_DEPOSIT,
                        fecha = daysAgo(4),
                        note = "Fondo de caja",
                    ),
                    CashTransactionRoom(
                        storeId = 2,
                        amount = 50f,
                        type = CashTransactionRoom.TYPE_WITHDRAWAL,
                        fecha = daysAgo(1),
                        note = "Compra insumos",
                    ),
                ),
            )

            ScreenUiState.OK
        } catch (_: Exception) {
            ScreenUiState.ERROR
        }
    }
}
