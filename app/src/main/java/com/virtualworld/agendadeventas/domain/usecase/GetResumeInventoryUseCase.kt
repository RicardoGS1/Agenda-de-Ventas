package com.virtualworld.agendadeventas.domain.usecase

import android.annotation.SuppressLint
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.ui.screen.resume.InventoryResumeDataUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import javax.inject.Inject

class GetResumeInventoryUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    @SuppressLint("DefaultLocale")
    fun getResumeInventoryStoreActive(): Flow<NetworkResponseState<List<InventoryResumeDataUI>>> {
        val decimalFormat = DecimalFormat("#.##")

        return localRepository.getResumeInventoryStoreActive().map { response ->
            when (response) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success -> {
                    val inventoryData = response.result.map { storeResume ->
                        InventoryResumeDataUI(
                            store = storeResume.tienda,
                            totalUnits = storeResume.unidades,
                            totalInvestment = decimalFormat.format(storeResume.inversion),
                            totalProfit = decimalFormat.format(storeResume.ganancia),
                        )
                    }
                    NetworkResponseState.Success(inventoryData)
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}
