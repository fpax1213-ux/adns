package com.eyalm.adns.services

import android.content.BroadcastReceiver
import android.util.Log
import com.eyalm.adns.data.DnsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ToggleReceiver : BroadcastReceiver() {
    override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
        Log.d("ToggleReceiver", "Received broadcast")

        if (intent?.action == "TOGGLE_DNS") {
            Log.d("ToggleReceiver", "click broadcast")

            val pendingResult = goAsync()
            val repository = DnsRepository(context!!)
            val newState = !repository.isAdBlockingActive()

            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                try {
                    repository.setAdBlockingState(newState).join()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}