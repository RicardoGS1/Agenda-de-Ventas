package com.virtualworld.agendadeventas.domain.model

import androidx.annotation.StringRes
import com.virtualworld.agendadeventas.R

enum class AiAnalysisType(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
) {
    GENERAL_OVERVIEW(
        titleRes = R.string.ai_analysis_overview_title,
        descriptionRes = R.string.ai_analysis_overview_desc,
    ),
    STORE_COMPARISON(
        titleRes = R.string.ai_analysis_stores_title,
        descriptionRes = R.string.ai_analysis_stores_desc,
    ),
    TOP_PRODUCTS(
        titleRes = R.string.ai_analysis_products_title,
        descriptionRes = R.string.ai_analysis_products_desc,
    ),
    PROFITABILITY(
        titleRes = R.string.ai_analysis_profitability_title,
        descriptionRes = R.string.ai_analysis_profitability_desc,
    ),
    INVENTORY(
        titleRes = R.string.ai_analysis_inventory_title,
        descriptionRes = R.string.ai_analysis_inventory_desc,
    ),
    CASH_FLOW(
        titleRes = R.string.ai_analysis_cash_title,
        descriptionRes = R.string.ai_analysis_cash_desc,
    ),
    PURCHASES_VS_SALES(
        titleRes = R.string.ai_analysis_purchases_title,
        descriptionRes = R.string.ai_analysis_purchases_desc,
    ),
    RECOMMENDATIONS(
        titleRes = R.string.ai_analysis_recommendations_title,
        descriptionRes = R.string.ai_analysis_recommendations_desc,
    ),
}
