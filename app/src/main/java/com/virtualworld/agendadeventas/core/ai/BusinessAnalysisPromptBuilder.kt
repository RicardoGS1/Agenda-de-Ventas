package com.virtualworld.agendadeventas.core.ai

import com.virtualworld.agendadeventas.core.locale.AppLanguage
import com.virtualworld.agendadeventas.core.model.BusinessAnalysisSnapshot
import com.virtualworld.agendadeventas.domain.model.AiAnalysisType

object BusinessAnalysisPromptBuilder {

    private const val MAX_RESPONSE_CHARS = 4000

    fun systemInstruction(language: AppLanguage): String {
        val respondOnlyIn = respondOnlyInLanguage(language)
        return when (language) {
            AppLanguage.SPANISH -> """
                $respondOnlyIn
                Eres un asesor de negocios que habla con el dueño de una tienda, sin tecnicismos.
                Responde en español sencillo, breve y directo, como en una conversación.
                REGLAS OBLIGATORIAS:
                - Los datos son SOLO de los últimos 30 días; dilo en una frase al inicio.
                - Máximo $MAX_RESPONSE_CHARS caracteres en total. Sé muy conciso.
                - NO uses tablas, columnas alineadas, markdown complejo ni listas largas.
                - Usa como mucho 3-4 párrafos cortos o 3-4 viñetas simples.
                - Explica cifras en lenguaje cotidiano (ej.: "vendiste bien", "te falta stock").
                - Da 2-3 conclusiones claras y 1-2 acciones prácticas, no más.
                - No inventes datos. Si falta información, dilo en una frase.
            """.trimIndent()

            AppLanguage.ENGLISH -> """
                $respondOnlyIn
                You advise a small shop owner in plain, everyday language — no jargon.
                REPLY RULES:
                - Data covers ONLY the last 30 days; say so in one opening sentence.
                - Maximum $MAX_RESPONSE_CHARS characters total. Be very concise.
                - NO tables, aligned columns, complex markdown, or long lists.
                - At most 3-4 short paragraphs or 3-4 simple bullet points.
                - Explain numbers in everyday terms (e.g. "sales are strong", "stock is low").
                - Give 2-3 clear takeaways and 1-2 practical actions, no more.
                - Do not invent data. If information is missing, say so briefly.
            """.trimIndent()

            AppLanguage.CHINESE -> """
                $respondOnlyIn
                你像和普通店主聊天一样给建议，用简单中文，不要术语。
                规则：
                - 数据仅最近30天，开头一句话说明。
                - 总回复不超过 $MAX_RESPONSE_CHARS 字，尽量简短。
                - 禁止表格、对齐列和复杂格式，最多3-4个短段或3-4条要点。
                - 用日常语言解释数字；给出2-3个结论和1-2条可执行建议。
                - 不要编造数据；信息不足时简要说明。
            """.trimIndent()

            AppLanguage.HINDI -> """
                $respondOnlyIn
                आप एक दुकान मालिक से साधारण भाषा में बात करें, तकनीकी शब्द नहीं।
                नियम:
                - केवल पिछले 30 दिनों का डेटा, शुरुआत में एक वाक्य में बताएँ।
                - कुल $MAX_RESPONSE_CHARS अक्षर से कम, बहुत संक्षिप्त रहें।
                - कोई तालिका या जटिल फ़ॉर्मेट नहीं; अधिकतम 3-4 छोटे पैराग्राफ या 3-4 बिंदु।
                - आंकड़े सरल भाषा में समझाएँ; 2-3 निष्कर्ष और 1-2 व्यावहारिक सुझाव दें।
                - डेटा न गढ़ें; जानकारी कम हो तो संक्षेप में बताएँ।
            """.trimIndent()

            AppLanguage.ARABIC -> """
                $respondOnlyIn
                تتحدث مع صاحب متجر بلغة بسيطة ويومية، بلا مصطلحات تقنية.
                القواعد:
                - البيانات لآخر 30 يومًا فقط، اذكر ذلك في جملة في البداية.
                - الحد الأقصى $MAX_RESPONSE_CHARS حرفًا، كن مختصرًا جدًا.
                - لا جداول ولا تنسيق معقد؛ 3-4 فقرات قصيرة أو 3-4 نقاط كحد أقصى.
                - اشرح الأرقام بلغة يومية؛ 2-3 خلاصات و1-2 إجراءات عملية.
                - لا تختلق بيانات؛ إذا نقصت المعلومات، وضّح ذلك باختصار.
            """.trimIndent()
        }
    }

    fun userPrompt(
        type: AiAnalysisType,
        snapshot: BusinessAnalysisSnapshot,
        language: AppLanguage,
    ): String {
        val dataContext = snapshot.toPromptContext(language)
        val task = taskForType(type, language)
        return """
            $task
            ${responseFormatHint(language)}

            ${dataSectionHeader(language)}
            $dataContext
        """.trimIndent()
    }

    private fun respondOnlyInLanguage(language: AppLanguage): String = when (language) {
        AppLanguage.SPANISH -> "IMPORTANTE: Debes responder COMPLETAMENTE en español. No uses otro idioma."
        AppLanguage.ENGLISH -> "IMPORTANT: You MUST respond ENTIRELY in English. Do not use any other language."
        AppLanguage.CHINESE -> "重要：你必须完全用中文回复，不要使用其他语言。"
        AppLanguage.HINDI -> "महत्वपूर्ण: आपको पूरी तरह हिंदी में जवाब देना है, कोई अन्य भाषा नहीं।"
        AppLanguage.ARABIC -> "مهم: يجب أن ترد بالكامل بالعربية، ولا تستخدم أي لغة أخرى."
    }

    private fun responseFormatHint(language: AppLanguage): String = when (language) {
        AppLanguage.SPANISH ->
            "Responde en texto corrido o viñetas cortas. Sin tablas ni listas largas. Máximo $MAX_RESPONSE_CHARS caracteres."

        AppLanguage.ENGLISH ->
            "Reply in plain text or short bullet points. No tables or long lists. Maximum $MAX_RESPONSE_CHARS characters."

        AppLanguage.CHINESE ->
            "用普通文字或简短要点回复。不要表格或长列表。最多 $MAX_RESPONSE_CHARS 字。"

        AppLanguage.HINDI ->
            "साधारण पाठ या छोटे बिंदुओं में जवाब दें। कोई तालिका या लंबी सूची नहीं। अधिकतम $MAX_RESPONSE_CHARS अक्षर।"

        AppLanguage.ARABIC ->
            "أجب بنص عادي أو نقاط قصيرة. بلا جداول أو قوائم طويلة. الحد الأقصى $MAX_RESPONSE_CHARS حرفًا."
    }

    private fun dataSectionHeader(language: AppLanguage): String = when (language) {
        AppLanguage.SPANISH -> "--- DATOS (últimos 30 días) ---"
        AppLanguage.ENGLISH -> "--- DATA (last 30 days) ---"
        AppLanguage.CHINESE -> "--- 数据（最近30天）---"
        AppLanguage.HINDI -> "--- डेटा (पिछले 30 दिन) ---"
        AppLanguage.ARABIC -> "--- البيانات (آخر 30 يومًا) ---"
    }

    private fun taskForType(type: AiAnalysisType, language: AppLanguage): String = when (language) {
        AppLanguage.SPANISH -> spanishTask(type)
        AppLanguage.ENGLISH -> englishTask(type)
        AppLanguage.CHINESE -> chineseTask(type)
        AppLanguage.HINDI -> hindiTask(type)
        AppLanguage.ARABIC -> arabicTask(type)
    }

    private fun spanishTask(type: AiAnalysisType): String = when (type) {
        AiAnalysisType.GENERAL_OVERVIEW ->
            "Resume en pocas frases cómo le fue al negocio: lo bueno, lo mejorable y qué hacer primero."
        AiAnalysisType.STORE_COMPARISON ->
            "Di qué tienda va mejor, cuál va peor y por qué, en lenguaje sencillo. Sin comparar número por número."
        AiAnalysisType.TOP_PRODUCTS ->
            "Cuenta cuáles productos se venden más, cuáles casi no se mueven y qué harías tú en la tienda."
        AiAnalysisType.PROFITABILITY ->
            "Explica si el negocio está ganando bien o no, y en qué tienda conviene enfocarse, sin tecnicismos."
        AiAnalysisType.INVENTORY ->
            "Advierte si hay poco stock o productos parados y qué conviene reponer o dejar de comprar."
        AiAnalysisType.CASH_FLOW ->
            "Resume si el dinero en caja va bien o hay que cuidar gastos y retiros, de forma clara y breve."
        AiAnalysisType.PURCHASES_VS_SALES ->
            "Di si se está comprando de más o de menos respecto a lo que se vende, y qué ajustar."
        AiAnalysisType.RECOMMENDATIONS ->
            "Da solo 2-3 recomendaciones prácticas y fáciles de aplicar esta semana."
    }

    private fun englishTask(type: AiAnalysisType): String = when (type) {
        AiAnalysisType.GENERAL_OVERVIEW ->
            "Briefly summarize how the business is doing: what's good, what to improve, and what to do first."
        AiAnalysisType.STORE_COMPARISON ->
            "Say which store is doing best, which needs attention, and why — in plain language, no number-by-number comparison."
        AiAnalysisType.TOP_PRODUCTS ->
            "Name top sellers, slow movers, and one simple action for each."
        AiAnalysisType.PROFITABILITY ->
            "Explain in simple terms whether the business is earning well and which store to focus on."
        AiAnalysisType.INVENTORY ->
            "Warn about low stock or stuck products and what to restock or stop buying."
        AiAnalysisType.CASH_FLOW ->
            "Briefly say if cash flow looks healthy or if spending/withdrawals need attention."
        AiAnalysisType.PURCHASES_VS_SALES ->
            "Say if you're buying too much or too little vs sales, and what to adjust."
        AiAnalysisType.RECOMMENDATIONS ->
            "Give only 2-3 practical, easy actions for this week."
    }

    private fun chineseTask(type: AiAnalysisType): String = when (type) {
        AiAnalysisType.GENERAL_OVERVIEW ->
            "用几句话总结生意情况：好的方面、需改进之处、优先做什么。"
        AiAnalysisType.STORE_COMPARISON ->
            "用简单语言说明哪家店最好、哪家需关注及原因，不要逐条对比数字。"
        AiAnalysisType.TOP_PRODUCTS ->
            "说明哪些产品卖得好、哪些几乎不动，以及你会在店里怎么做。"
        AiAnalysisType.PROFITABILITY ->
            "用通俗语言说明生意是否赚钱，应重点关注哪家店。"
        AiAnalysisType.INVENTORY ->
            "提醒库存是否偏低或有滞销品，该补什么、少买什么。"
        AiAnalysisType.CASH_FLOW ->
            "简要说明现金状况是否健康，支出和取款是否需注意。"
        AiAnalysisType.PURCHASES_VS_SALES ->
            "说明采购是否过多或过少，以及如何调整。"
        AiAnalysisType.RECOMMENDATIONS ->
            "只给出2-3条本周可执行的实用建议。"
    }

    private fun hindiTask(type: AiAnalysisType): String = when (type) {
        AiAnalysisType.GENERAL_OVERVIEW ->
            "कुछ वाक्यों में बताएँ व्यवसाय कैसा चल रहा है: अच्छा क्या है, क्या सुधारें, पहले क्या करें।"
        AiAnalysisType.STORE_COMPARISON ->
            "सरल भाषा में बताएँ कौन सा स्टोर सबसे अच्छा है, किस पर ध्यान दें और क्यों; संख्या दर संख्या तुलना न करें।"
        AiAnalysisType.TOP_PRODUCTS ->
            "बताएँ कौन से उत्पाद ज़्यादा बिकते हैं, कौन से नहीं, और दुकान में क्या करेंगे।"
        AiAnalysisType.PROFITABILITY ->
            "सरल भाषा में बताएँ क्या व्यवसाय अच्छा मुनाफ़ा दे रहा है और किस स्टोर पर ध्यान दें।"
        AiAnalysisType.INVENTORY ->
            "चेतावनी दें अगर स्टॉक कम है या उत्पाद रुके हैं; क्या भरें, क्या कम खरीदें।"
        AiAnalysisType.CASH_FLOW ->
            "संक्षेप में बताएँ नकदी ठीक है या खर्च/निकासी पर ध्यान देना होगा।"
        AiAnalysisType.PURCHASES_VS_SALES ->
            "बताएँ क्या ज़्यादा या कम खरीद रहे हैं बिक्री के मुकाबले, और क्या बदलें।"
        AiAnalysisType.RECOMMENDATIONS ->
            "केवल 2-3 व्यावहारिक सुझाव दें जो इस हफ्ते लागू कर सकें।"
    }

    private fun arabicTask(type: AiAnalysisType): String = when (type) {
        AiAnalysisType.GENERAL_OVERVIEW ->
            "لخّص في جمل قليلة أداء العمل: ما هو جيد، ما يحتاج تحسينًا، وماذا تفعل أولًا."
        AiAnalysisType.STORE_COMPARISON ->
            "قل أي متجر أفضل وأيّه يحتاج اهتمامًا ولماذا بلغة بسيطة، دون مقارنة رقم برقم."
        AiAnalysisType.TOP_PRODUCTS ->
            "اذكر أكثر المنتجات مبيعًا والأقل حركة وماذا ستفعل في المتجر."
        AiAnalysisType.PROFITABILITY ->
            "اشرح بلغة يومية هل العمل يربح جيدًا وأي متجر يستحق التركيز."
        AiAnalysisType.INVENTORY ->
            "نبّه إن كان المخزون منخفضًا أو منتجات راكدة وماذا تعيد شراءه أو تتوقف عنه."
        AiAnalysisType.CASH_FLOW ->
            "لخّص هل الوضع النقدي جيد أو يجب الانتباه للمصروفات والسحوبات."
        AiAnalysisType.PURCHASES_VS_SALES ->
            "قل إن كنت تشتري أكثر أو أقل مما تبيع وكيف تعدّل ذلك."
        AiAnalysisType.RECOMMENDATIONS ->
            "قدّم 2-3 توصيات عملية فقط يمكن تطبيقها هذا الأسبوع."
    }

    private fun BusinessAnalysisSnapshot.toPromptContext(language: AppLanguage): String {
        val labels = dataLabels(language)
        return buildString {
            appendLine("${labels.period}: $periodLabel")
            appendLine()
            appendLine("${labels.globalTotals}:")
            appendLine("- ${labels.unitsSold}: ${totals.totalUnitsSold}")
            appendLine("- ${labels.salesRevenue}: ${formatMoney(totals.totalRevenue)}")
            appendLine("- ${labels.estimatedProfit}: ${formatMoney(totals.totalProfit)}")
            appendLine("- ${labels.purchaseUnits}: ${totals.totalPurchasesUnits}")
            appendLine("- ${labels.purchaseCost}: ${formatMoney(totals.totalPurchasesCost)}")
            appendLine("- ${labels.cashDeposits}: ${formatMoney(totals.totalCashDeposits)}")
            appendLine("- ${labels.cashWithdrawals}: ${formatMoney(totals.totalCashWithdrawals)}")
            appendLine()
            appendLine("${labels.byStore}:")
            stores.forEach { store ->
                appendLine("- ${store.storeName}:")
                appendLine("  ${labels.unitsSold}: ${store.unitsSold}")
                appendLine("  ${labels.salesRevenue}: ${formatMoney(store.revenue)}")
                appendLine("  ${labels.soldCost}: ${formatMoney(store.costOfGoodsSold)}")
                appendLine("  ${labels.estimatedProfit}: ${formatMoney(store.profit)}")
                appendLine("  ${labels.purchases}: ${store.purchasesUnits} / ${formatMoney(store.purchasesCost)}")
                appendLine("  ${labels.depositsAndWithdrawals}: ${formatMoney(store.cashDeposits)} / ${formatMoney(store.cashWithdrawals)}")
            }
            appendLine()
            appendLine("${labels.topProducts}:")
            topProducts.take(5).forEach { product ->
                appendLine("- ${product.productName}: ${product.unitsSold}, ${labels.profit} ${formatMoney(product.profit)}")
            }
            appendLine()
            appendLine("${labels.lowRotation}:")
            if (lowRotationProducts.isEmpty()) {
                appendLine("- ${labels.none}")
            } else {
                lowRotationProducts.take(5).forEach { product ->
                    appendLine("- ${product.productName}: ${product.unitsSold}")
                }
            }
            appendLine()
            appendLine("${labels.currentInventory}:")
            inventoryByStore.forEach { inventory ->
                appendLine("- ${inventory.storeName}: ${inventory.totalUnits} ${labels.inStock}")
                inventory.lowStockProducts.take(3).forEach { item ->
                    appendLine("  · ${item.name}: ${labels.remaining} ${item.stock}")
                }
            }
        }
    }

    private data class DataLabels(
        val period: String,
        val globalTotals: String,
        val unitsSold: String,
        val salesRevenue: String,
        val estimatedProfit: String,
        val purchaseUnits: String,
        val purchaseCost: String,
        val cashDeposits: String,
        val cashWithdrawals: String,
        val byStore: String,
        val soldCost: String,
        val purchases: String,
        val depositsAndWithdrawals: String,
        val topProducts: String,
        val profit: String,
        val lowRotation: String,
        val none: String,
        val currentInventory: String,
        val inStock: String,
        val remaining: String,
    )

    private fun dataLabels(language: AppLanguage): DataLabels = when (language) {
        AppLanguage.SPANISH -> DataLabels(
            period = "Periodo",
            globalTotals = "TOTALES GLOBALES",
            unitsSold = "Unidades vendidas",
            salesRevenue = "Ingresos por ventas",
            estimatedProfit = "Ganancia estimada",
            purchaseUnits = "Compras (unidades)",
            purchaseCost = "Coste de compras",
            cashDeposits = "Depósitos en caja",
            cashWithdrawals = "Retiros de caja",
            byStore = "POR TIENDA",
            soldCost = "Coste vendido",
            purchases = "Compras",
            depositsAndWithdrawals = "Depósitos / Retiros",
            topProducts = "TOP PRODUCTOS",
            profit = "ganancia",
            lowRotation = "BAJA ROTACIÓN",
            none = "Ninguno",
            currentInventory = "INVENTARIO ACTUAL (referencia)",
            inStock = "uds en stock",
            remaining = "quedan",
        )

        AppLanguage.ENGLISH -> DataLabels(
            period = "Period",
            globalTotals = "GLOBAL TOTALS",
            unitsSold = "Units sold",
            salesRevenue = "Sales revenue",
            estimatedProfit = "Estimated profit",
            purchaseUnits = "Purchases (units)",
            purchaseCost = "Purchase cost",
            cashDeposits = "Cash deposits",
            cashWithdrawals = "Cash withdrawals",
            byStore = "BY STORE",
            soldCost = "Cost of goods sold",
            purchases = "Purchases",
            depositsAndWithdrawals = "Deposits / Withdrawals",
            topProducts = "TOP PRODUCTS",
            profit = "profit",
            lowRotation = "LOW ROTATION",
            none = "None",
            currentInventory = "CURRENT INVENTORY (reference)",
            inStock = "units in stock",
            remaining = "remaining",
        )

        AppLanguage.CHINESE -> DataLabels(
            period = "期间",
            globalTotals = "总计",
            unitsSold = "售出数量",
            salesRevenue = "销售收入",
            estimatedProfit = "估计利润",
            purchaseUnits = "采购（数量）",
            purchaseCost = "采购成本",
            cashDeposits = "现金存入",
            cashWithdrawals = "现金取出",
            byStore = "按店铺",
            soldCost = "售出成本",
            purchases = "采购",
            depositsAndWithdrawals = "存入 / 取出",
            topProducts = "热销产品",
            profit = "利润",
            lowRotation = "低周转",
            none = "无",
            currentInventory = "当前库存（参考）",
            inStock = "库存单位",
            remaining = "剩余",
        )

        AppLanguage.HINDI -> DataLabels(
            period = "अवधि",
            globalTotals = "कुल योग",
            unitsSold = "बेची गई इकाइयाँ",
            salesRevenue = "बिक्री राजस्व",
            estimatedProfit = "अनुमानित लाभ",
            purchaseUnits = "खरीद (इकाइयाँ)",
            purchaseCost = "खरीद लागत",
            cashDeposits = "नकद जमा",
            cashWithdrawals = "नकद निकासी",
            byStore = "स्टोर के अनुसार",
            soldCost = "बेची गई लागत",
            purchases = "खरीद",
            depositsAndWithdrawals = "जमा / निकासी",
            topProducts = "शीर्ष उत्पाद",
            profit = "लाभ",
            lowRotation = "कम बिक्री",
            none = "कोई नहीं",
            currentInventory = "वर्तमान इन्वेंटरी (संदर्भ)",
            inStock = "स्टॉक में इकाइयाँ",
            remaining = "शेष",
        )

        AppLanguage.ARABIC -> DataLabels(
            period = "الفترة",
            globalTotals = "الإجماليات",
            unitsSold = "الوحدات المباعة",
            salesRevenue = "إيرادات المبيعات",
            estimatedProfit = "الربح التقديري",
            purchaseUnits = "المشتريات (وحدات)",
            purchaseCost = "تكلفة المشتريات",
            cashDeposits = "الإيداعات النقدية",
            cashWithdrawals = "السحوبات النقدية",
            byStore = "حسب المتجر",
            soldCost = "تكلفة البضاعة المباعة",
            purchases = "المشتريات",
            depositsAndWithdrawals = "إيداعات / سحوبات",
            topProducts = "أكثر المنتجات مبيعًا",
            profit = "ربح",
            lowRotation = "دوران منخفض",
            none = "لا يوجد",
            currentInventory = "المخزون الحالي (مرجع)",
            inStock = "وحدات في المخزون",
            remaining = "متبقي",
        )
    }

    private fun formatMoney(value: Float): String = String.format("%.2f", value)
}
