package com.virtualworld.agendadeventas.ui.screen.common

import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive


fun checkDecimalNumber(numStore: String): String {

    val filteredChars = numStore.filterIndexed { index, c ->
        c in "0123456789" || (c == '.' && numStore.indexOf('.') == index)
    }

    return filteredChars
}

fun productValidator(product: ProductWithStoresActive): Boolean {
    return product.productName.isNotBlank() && product.productCost.isNotBlank()
}

