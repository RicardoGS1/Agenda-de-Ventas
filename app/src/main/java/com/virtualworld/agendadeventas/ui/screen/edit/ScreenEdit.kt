package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R


import com.virtualworld.agendadeventas.domain.Model.ProductWithStoresActive
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState


@Composable
fun ScreenEdit() {

    val viewModel: EditViewModel = hiltViewModel()

    val screenUiState by viewModel.screenUiState.collectAsState()
    val productsState by viewModel.productsState.collectAsState()

    val changeScreenUiState = { stateUi: ScreenUiState -> viewModel.changerUiState(stateUi) }

    val productSelectState by viewModel.productSelectState.collectAsState()//puede ser local

    val changeProductSelect = { product: ProductWithStoresActive ->
        viewModel.changeProductSelect(product)
    }

    val onRemoveProduct = { product: ProductWithStoresActive ->
        viewModel.deleteProduct(product)
    }

    val setProduct = { product: ProductWithStoresActive ->
        viewModel.setProduct()
    }


    val windowEditView = rememberSaveable { (mutableStateOf(false)) }

    val changeWindowEditView = {
        windowEditView.value = !windowEditView.value
    }

    val updateProductName = { name: String -> viewModel.updateProductName(name) }
    val updateProductCost = { cost: String -> viewModel.updateProductCost(cost) }
    val updateStoreValue = { storeId: String, value: String ->
        viewModel.updateStoreValue(
            storeId,
            value
        )
    }

    Box() {


        if (!windowEditView.value) {

            if (screenUiState != ScreenUiState.LOADING) {
                ListProducts(
                    changeWindowEditView,
                    productsState,
                    changeProductSelect,
                    onRemoveProduct
                )
            }
        } else {
            if (screenUiState != ScreenUiState.LOADING) {
                WindowsEditView(
                    changeWindowEditView,
                    productSelectState,
                    updateProductName,
                    updateProductCost,
                    updateStoreValue,
                    setProduct
                )
            }
        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            changeScreenUiState
        )
    }

}


