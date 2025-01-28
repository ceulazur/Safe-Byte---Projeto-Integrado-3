package com.example.safebyte.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.safebyte.R
import com.example.safebyte.ui.viewmodel.SettingsViewModel
import java.util.Date

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("NotificationReceiver", "onReceive called")
        Log.d("NotificationReceiver", "Intent action: ${intent?.action}")
        Log.d("NotificationReceiver", "Current time: ${Date()}")

        try {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Verificar se as notificações estão habilitadas
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!notificationManager.areNotificationsEnabled()) {
                    Log.e("NotificationReceiver", "Notifications are disabled for the app")
                    return
                }
            }

            val hasNewTips = checkForNewTips(context)
            Log.d("NotificationReceiver", "Has new tips: $hasNewTips")

            val title = if (hasNewTips) "Nova Dica de Segurança!" else "Bom Dia!"
            val message =
                if (hasNewTips) getLatestTip(context) else "Lembre-se de verificar locais seguros hoje!"

            showNotification(context, title, message)

            // Reagendar para o próximo dia
            val viewModel = SettingsViewModel()
            viewModel.scheduleNotification(context)

        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error showing notification", e)
        }
    }

    private fun checkForNewTips(context: Context): Boolean {
        // Implemente a lógica real de verificação do banco de dados aqui
        val prefs = context.getSharedPreferences("tips", Context.MODE_PRIVATE)
        val lastCheck = prefs.getLong("last_tip_check", 0)
        val currentTime = System.currentTimeMillis()
        val hasNew = currentTime - lastCheck > 24 * 60 * 60 * 1000 // Simulação de nova dica diária
        if (hasNew) prefs.edit().putLong("last_tip_check", currentTime).apply()
        return hasNew
    }

    private fun getLatestTip(context: Context): String {
        // Implemente a busca real no banco de dados aqui
        return "Sempre verifique os ingredientes com o estabelecimento antes de consumir."
    }

    private fun showNotification(context: Context, title: String, message: String) {
        try {
            Log.d("NotificationReceiver", "Showing notification: $title - $message")

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "daily_tips_channel"

            // Criar ou atualizar o canal de notificação
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = notificationManager.getNotificationChannel(channelId)
                if (channel == null) {
                    Log.d("NotificationReceiver", "Creating notification channel")
                    val newChannel = NotificationChannel(
                        channelId,
                        "Dicas Diárias",
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = "Notificações com dicas de segurança para alérgicos"
                        enableLights(true)
                        lightColor = Color.RED
                        enableVibration(true)
                    }
                    notificationManager.createNotificationChannel(newChannel)
                }
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build()

            notificationManager.notify(NotificationID.get(), notification)
            Log.d("NotificationReceiver", "Notification shown successfully")

        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error in showNotification", e)
        }
    }
}

object NotificationID {
    private var id = 0
    fun get() = id++
}