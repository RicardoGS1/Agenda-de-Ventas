package com.virtualworld.agendadeventas.core.model

data class BusinessAnalysisSnapshot(
    val periodStartMillis: Long,
    val periodEndMillis: Long,
    val periodLabel: String,
    val stores: List<StoreAnalysisData>,
    val topProducts: List<ProductSalesData>,
    val lowRotationProducts: List<ProductSalesData>,
    val inventoryByStore: List<InventorySnapshotData>,
    val totals: AnalysisTotals,
)

data class StoreAnalysisData(
    val storeId: Int,
    val storeName: String,
    val unitsSold: Int,
    val costOfGoodsSold: Float,
    val revenue: Float,
    val profit: Float,
    val purchasesUnits: Int,
    val purchasesCost: Float,
    val cashDeposits: Float,
    val cashWithdrawals: Float,
)

data class ProductSalesData(
    val productName: String,
    val unitsSold: Int,
    val revenue: Float,
    val profit: Float,
)

data class InventorySnapshotData(
    val storeId: Int,
    val storeName: String,
    val totalUnits: Int,
    val investment: Float,
    val potentialProfit: Float,
    val lowStockProducts: List<ProductInventoryItem>,
)

data class ProductInventoryItem(
    val name: String,
    val stock: Int,
    val purchasePrice: Float,
    val salePrice: Float,
)

data class AnalysisTotals(
    val totalUnitsSold: Int,
    val totalRevenue: Float,
    val totalProfit: Float,
    val totalPurchasesCost: Float,
    val totalPurchasesUnits: Int,
    val totalCashDeposits: Float,
    val totalCashWithdrawals: Float,
)
