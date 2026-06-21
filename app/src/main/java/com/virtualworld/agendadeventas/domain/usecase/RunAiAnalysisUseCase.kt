package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.ai.BusinessAnalysisPromptBuilder
import com.virtualworld.agendadeventas.core.ai.GeminiApiService
import com.virtualworld.agendadeventas.core.locale.AppLanguage
import com.virtualworld.agendadeventas.domain.model.AiAnalysisType
import javax.inject.Inject

class RunAiAnalysisUseCase @Inject constructor(
    private val getBusinessAnalysisSnapshotUseCase: GetBusinessAnalysisSnapshotUseCase,
    private val geminiApiService: GeminiApiService,
) {
    suspend operator fun invoke(
        type: AiAnalysisType,
        language: AppLanguage,
    ): Result<String> {
        val snapshot = getBusinessAnalysisSnapshotUseCase()
        val systemInstruction = BusinessAnalysisPromptBuilder.systemInstruction(language)
        val userPrompt = BusinessAnalysisPromptBuilder.userPrompt(type, snapshot, language)
        return geminiApiService.generateAnalysis(systemInstruction, userPrompt)
    }
}
