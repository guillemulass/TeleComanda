package com.example.telecomanda.screens.workScreens

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.telecomanda.NotificationActivity
import com.example.telecomanda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val CHANNEL_ID = "channelID"

class EmployeeWorkViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications: StateFlow<List<String>> = _notifications

    private var notificationListenerRegistration: ListenerRegistration? = null

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "First channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Description for the test channel"
            }

            val notificationManager = getApplication<Application>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title: String, text: String, tableNumber: String) {
        val context = getApplication<Application>().applicationContext

        val intent = Intent(context, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_text", text)
            putExtra("notification_title", tableNumber)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // ActivityCompat#requestPermissions should be called from an Activity, not a ViewModel.
                // So, consider requesting permissions from the Activity/Fragment that owns this ViewModel.
                return
            }
            notify((0..10000).random(), builder.build())
        }
    }



    fun getRestaurantName(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val email = auth.currentUser?.email ?: return@launch
                val document = db.collection("employeesEmails").document(email).get().await()
                val restaurantName = document.getString("restaurantName") ?: ""
                onSuccess(restaurantName)
            } catch (exception: Exception) {
                onFailure(exception)
            }
        }
    }

    fun listenToNotifications(restaurantName: String) {
        val notificationsCollection = db.collection("restaurants").document(restaurantName).collection("notificationText")

        notificationListenerRegistration = notificationsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val notifications = snapshot.documents.mapNotNull { it.getString("command") }
                _notifications.value = notifications

                snapshot.documents.forEach { document ->
                    val command = document.getString("command")
                    val tableNumber = document.getString("tableNumber") ?: "Desconocido"
                    if (command != null) {
                        sendNotification("Se ha pedido una comanda en la mesa $tableNumber", command, tableNumber)
                        document.reference.delete()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        notificationListenerRegistration?.remove()
    }
}
