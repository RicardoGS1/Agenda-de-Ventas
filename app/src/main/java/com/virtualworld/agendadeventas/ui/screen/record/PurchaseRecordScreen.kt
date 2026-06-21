package com.virtualworld.agendadeventas.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.model.PurchaseForStoreCore
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.StoreSelectView

@Composable
fun PurchaseRecordScreen(viewModel: PurchaseRecordViewModel = hiltViewModel()) {
    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val uiMessengerState by viewModel.messengerState.collectAsState()
    val purchasesForStore by viewModel.purchasesForStore.collectAsState()

    PurchaseRecordScreenContent(
        storesActiveState = storesActiveState,
        uiMessengerState = uiMessengerState,
        purchasesForStore = purchasesForStore,
        onStoreSelected = { storeId -> viewModel.getPurchasesForStore(storeId) },
    )
}

@Composable
fun PurchaseRecordScreenContent(
    storesActiveState: List<Pair<Int, String>>,
    uiMessengerState: ScreenUiState,
    purchasesForStore: List<PurchaseForStoreCore>,
    onStoreSelected: (Int) -> Unit,
) {
    var selectedStoreIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(selectedStoreIndex) {
        if (selectedStoreIndex != -1) {
            onStoreSelected(storesActiveState[selectedStoreIndex].first)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                StoreSelectView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    label = stringResource(R.string.purchase_record_store_label),
                    stores = storesActiveState,
                    selectedIndex = selectedStoreIndex,
                    isLoading = uiMessengerState == ScreenUiState.LOADING,
                    placeholder = stringResource(R.string.purchase_record_pick_store),
                    onStoreSelected = { index -> selectedStoreIndex = index },
                )
            }

            if (selectedStoreIndex != -1) {
                ListProductPurchaseRecordView(
                    purchases = purchasesForStore,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
