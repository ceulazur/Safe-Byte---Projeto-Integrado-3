package com.example.safebyte.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.safebyte.R
import com.example.safebyte.data.repository.TipRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("NotificationReceiver", "onReceive called")

        if (intent?.action == "DAILY_NOTIFICATION") {
            val tipRepository = TipRepository()

            // Usando coroutine para chamada assíncrona
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val lastTip = tipRepository.getLastTip()

                    Log.d("NotificationDebug", "Result: $lastTip") // Log do resultado

                    lastTip.onSuccess { tip ->
                        val message = tip?.message ?: "Mensagem padrão"
                        Log.d("NotificationDebug", "Mensagem obtida: $message")
                        showNotification(context, "Dica!", message)
                    }.onFailure { e ->
                        Log.e("NotificationError", "Falha ao buscar dica", e)
                        showNotification(context, "Erro", "Falha ao carregar dica")
                    }

                } catch (e: Exception) {
                    Log.e("NotificationReceiver", "Error fetching tip", e)
                }
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        try {
            Log.d("NotificationReceiver", "Showing notification: $title - $message")

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "daily_tips_channel"

            // Criar ou atualizar o canal de notificação
            val channel = NotificationChannel(
                channelId,
                "Dicas Diárias",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações com dicas de segurança para alérgicos"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)

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