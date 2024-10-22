package com.github.jvsena42.floresta.domain.floresta

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FlorestaService : Service() {
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val florestaDaemon: FlorestaDaemon by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        try {
            ioScope.launch {
                Log.d(TAG, "onStartCommand: ")
                florestaDaemon.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "onStartCommand error: ", e)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.launch {
            Log.d(TAG, "onDestroy: ")
            florestaDaemon.stop()
        }
        ioScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "FlorestaService"
    }
}