package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.compose.foundation.layout.Box


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState


@Composable
fun ScreenEdit() {

    val viewModel: EditViewModel = hiltViewModel()

    val screenUiState by viewModel.screenUiState.collectAsState()
    val productsState by viewModel.productsState.collectAsState()
    val productSelectState by viewModel.productSelectState.collectAsState()

    val windowEditView = rememberSaveable { (mutableStateOf(false)) }

    val changeScreenUiState = { stateUi: ScreenUiState ->
        viewModel.changerUiState(stateUi)
    }
    val onEditProduct = { product: ProductWithStoresActive ->
        viewModel.changeProductSelect(product)
    }
    val changeWindowEditView = {
        windowEditView.value = !windowEditView.value
    }
    val onRemoveProduct = { product: ProductWithStoresActive ->
        viewModel.deleteProduct(product)
    }
    val setProduct = { product: ProductWithStoresActive ->
        viewModel.setProduct()
    }

    val updateProductName = { name: String -> viewModel.updateProductName(name) }
    val updateProductCost = { cost: String -> viewModel.updateProductCost(cost) }
    val updateStoreValue = { storeId: String, value: String ->
        viewModel.updateStoreValue(storeId, value)
    }


    Box() {


            if (screenUiState != ScreenUiState.LOADING) {
                ListProductsSwipeToDismiss(
                    productsState,
                    changeWindowEditView,
                    onEditProduct,
                    onRemoveProduct,
                    Modifier
                )
            }


            if (screenUiState != ScreenUiState.LOADING) {
                WindowsEditView(
                    changeWindowEditView,
                    productSelectState,
                    updateProductName,
                    updateProductCost,
                    updateStoreValue,
                    setProduct,
                    windowEditView.value
                )
            }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            changeScreenUiState
        )
    }

}


