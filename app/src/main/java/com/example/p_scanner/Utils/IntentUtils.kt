package com.example.p_scanner.Utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


class IntentUtils(val context: Context) {
    fun intentToSettings()
    {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package",  context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}