package com.maschdy.nitsu.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.maschdy.nitsu.R
import com.maschdy.nitsu.databinding.FragmentNotificationBinding

private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETED_NOTIFICATIONS"

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding

    private lateinit var notificationManager: NotificationManager
//    private val notificationReceiver = NotificationReceiver()
//    private val dynamicReceiver = DynamicReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)

        setupUiButtonListeners()
        setupUiButtonStates(enableNotify = true, enableUpdate = false, enableCancel = false)
        createNotificationChannel()
//        registerNotificationReceiver()
//        registerDynamicReceiver(dynamicReceiver)
    }

    private fun setupUiButtonListeners() = with(binding) {
        notify.setOnClickListener { sendNotification() }
        update.setOnClickListener { updateNotification() }
        cancel.setOnClickListener { cancelNotification() }
    }

    private fun setupUiButtonStates(
        enableNotify: Boolean,
        enableUpdate: Boolean,
        enableCancel: Boolean
    ) = with(binding) {
        notify.isEnabled = enableNotify
        update.isEnabled = enableUpdate
        cancel.isEnabled = enableCancel
    }

    private fun createNotificationChannel() {
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from Mascot"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            // TODO: para api 26 ou inferior
        }
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setupUiButtonStates(enableNotify = true, enableUpdate = false, enableCancel = false)
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.ic_favorite)
        val notification = getNotificationBuilder()
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Update notification!")
            )
        notificationManager.notify(NOTIFICATION_ID, notification.build())
        setupUiButtonStates(enableNotify = false, enableUpdate = false, enableCancel = true)
    }

    private fun sendNotification() {
        val builder = getNotificationBuilder()

        createNotificationAction(builder, NOTIFICATION_ID, ACTION_UPDATE, "Update")
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_CANCEL, "Remove")

        val deleteAllAction = Intent(ACTION_DELETE_ALL)
        val deletedAction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_ID,
                deleteAllAction,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_ID,
                deleteAllAction,
                PendingIntent.FLAG_ONE_SHOT
            )
        }
        builder.setDeleteIntent(deletedAction)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
        setupUiButtonStates(enableNotify = false, enableUpdate = true, enableCancel = true)
    }

    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter = Intent(actionId)

        val updateAction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_ID,
                updateActionFilter,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_ID,
                updateActionFilter,
                PendingIntent.FLAG_ONE_SHOT
            )
        }
        builder.addAction(
            R.drawable.ic_android,
            actionTitle,
            updateAction
        )
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), NotificationFragment::class.java)

        val notificationPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("You receive a notification!")
            .setContentText("Content of notification!")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)
    }

//    private fun registerNotificationReceiver() {
//        val notificationActionFilters = IntentFilter()
//        notificationActionFilters.addAction(ACTION_UPDATE)
//        notificationActionFilters.addAction(ACTION_DELETE_ALL)
//        notificationActionFilters.addAction(ACTION_CANCEL)
//        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)
//    }

    // for broadcast receiver
//    override fun onDestroy() {
//        requireActivity().unregisterReceiver(notificationReceiver)
//        super.onDestroy()
//    }
//
//    inner class NotificationReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            // Update the notification
//            when (intent.action) {
//                ACTION_UPDATE -> updateNotification()
//                ACTION_CANCEL -> {
//                    notificationManager.cancel(NOTIFICATION_ID)
//                    setupUiButtonStates(
//                        enableNotify = true,
//                        enableUpdate = false,
//                        enableCancel = false
//                    )
//                }
//                ACTION_DELETE_ALL -> setupUiButtonStates(
//                    enableNotify = true,
//                    enableUpdate = false,
//                    enableCancel = false
//                )
//            }
//        }
//    }
//
//    // API level 26 a maioria dos broadcastreceiver sao declarados dinamicamente
//    private fun registerDynamicReceiver(dynamicReceiver: BroadcastReceiver) {
//        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
//            requireActivity().registerReceiver(dynamicReceiver, it)
//        }
//    }
//
//    private fun unregisterDynamicReceiver() {
//        requireActivity().unregisterReceiver(dynamicReceiver)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterDynamicReceiver()
//    }
}
