package com.virtualworld.agendadeventas.ui.screen.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.virtualworld.agendadeventas.R

fun openRateApp(context: Context) {
    val packageName = context.packageName
    val marketUri = Uri.parse("market://details?id=$packageName")
    val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (_: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}

fun openShareApp(context: Context) {
    val packageName = context.packageName
    val playStoreUrl = "https://play.google.com/store/apps/details?id=$packageName"
    val shareText = context.getString(R.string.settings_share_message, playStoreUrl)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(
        Intent.createChooser(intent, context.getString(R.string.settings_share_app))
    )
}
