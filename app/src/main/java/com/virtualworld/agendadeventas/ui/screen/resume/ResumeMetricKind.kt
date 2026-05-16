package com.virtualworld.agendadeventas.ui.screen.resume

import java.text.DecimalFormat

enum class ResumeMetricKind {
    Units,
    Amount,
    Investment,
    Profit,
}

private val resumeDecimalFormat = DecimalFormat("#.##")

fun computeResumeTotal(
    metricKind: ResumeMetricKind,
    values: List<String>,
): String {
    return when (metricKind) {
        ResumeMetricKind.Units -> values.sumOf { it.toIntOrNull() ?: 0 }.toString()
        ResumeMetricKind.Amount,
        ResumeMetricKind.Investment,
        ResumeMetricKind.Profit,
        -> resumeDecimalFormat.format(values.sumOf(::parseResumeDecimal))
    }
}

private fun parseResumeDecimal(value: String): Double {
    return value.trim()
        .replace(',', '.')
        .toDoubleOrNull() ?: 0.0
}
