package com.example.kalendarzyk

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class MyNotificationReceiver : BroadcastReceiver() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(context: Context, intent: Intent) {
        coroutineScope.launch {
            val user = FirebaseAuth.getInstance().currentUser?.email ?: return@launch
            val nextPeriod = calculateNextPeriod(user)
            val fertileDays = calculateFertileDays(user)

            val channelId = "my_channel_id"
            val channelName = "My Channel"
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
            val notificationIntent = Intent(context, DashBoardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Zbliżające się wydarzenie")
                .setContentText("Dotknij aby rozwinąć")
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Najbliższy okres: $nextPeriod\nNajbliższe dni płodne: $fertileDays"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@launch
            }
            NotificationManagerCompat.from(context).notify(1, notification)
        }
    }

    private suspend fun calculateNextPeriod(user: String): String {
        val dayCounter = DayCounter(user)
        val nextPeriod = dayCounter.calculateNextPeriod()?.toString() ?: "Brak danych"
        return withContext(Dispatchers.IO) {
            nextPeriod
        }
    }

    private suspend fun calculateFertileDays(user: String): String {
        val dayCounter = DayCounter(user)
        val fertileDays = dayCounter.calculateFertileDays()
        return withContext(Dispatchers.IO) {
            fertileDays
        }
    }
}
