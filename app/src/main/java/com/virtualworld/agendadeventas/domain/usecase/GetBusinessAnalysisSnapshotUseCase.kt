package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.model.BusinessAnalysisSnapshot
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import java.util.Calendar
import javax.inject.Inject

class GetBusinessAnalysisSnapshotUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke(): BusinessAnalysisSnapshot {
        val (start, end) = last30DaysRange()
        return localRepository.getBusinessAnalysisSnapshot(start, end)
    }

    companion object {
        fun last30DaysRange(): Pair<Long, Long> {
            val end = System.currentTimeMillis()
            val start = Calendar.getInstance().apply {
                timeInMillis = end
                add(Calendar.DAY_OF_YEAR, -30)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            return start to end
        }
    }
}
