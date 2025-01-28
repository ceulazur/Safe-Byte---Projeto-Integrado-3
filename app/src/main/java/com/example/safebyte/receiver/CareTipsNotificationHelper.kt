package com.example.safebyte.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.safebyte.MainActivity
import com.example.safebyte.R
import kotlin.random.Random

object CareTipsNotificationHelper {
    private val careTips = listOf(
        "Lembre-se de verificar sempre os ingredientes antes de consumir um alimento novo",
        "Ao comer fora, informe sempre sobre suas alergias ao garçom ou chef",
        "Mantenha seu autoinjetor de epinefrina sempre atualizado e à mão",
        "Experimente novos restaurantes em horários menos movimentados para melhor atendimento",
        "Faça um diário alimentar para identificar possíveis reações alérgicas"
    )

    fun showCareTipNotification(context: Context) {
        val randomTip = careTips.random()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "care_tips_channel")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Dica de Segurança Alimentar")
            .setContentText(randomTip)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        context.getSystemService(NotificationManager::class.java)
            .notify(Random.nextInt(1000), notification)
    }
}