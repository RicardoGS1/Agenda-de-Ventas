package com.virtualworld.agendadeventas.domain.usecase

import android.annotation.SuppressLint
import android.icu.math.BigDecimal
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.ui.screen.resume.ResumeDataUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import javax.inject.Inject

class GetResumeStoresUseCase @Inject constructor(private val localRepository: LocalRepository) {


    //obtener los datos de venta de todas las tiendas activas(ganancia, unidades, nombre de tienda)
    @SuppressLint("DefaultLocale")
    fun getResumeSellStoreActive(
        dateStart: Long?,
        dateEnd: Long?
    ): Flow<NetworkResponseState<List<ResumeDataUI>>> {

        val decimalFormat = DecimalFormat("#.##")

        return localRepository.getResumeSoldStoreActive(dateStart, dateEnd).map { response ->



            when (response) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success -> {

                    val resumeDataUI = response.result.map {

                        ResumeDataUI(
                            profit =  decimalFormat.format(it.valor-it.compra),
                            units = it.unidades,
                            store = it.tienda
                        )
                    }


                    NetworkResponseState.Success(resumeDataUI)

                }
            }

        }.flowOn(Dispatchers.IO)


    }
}

