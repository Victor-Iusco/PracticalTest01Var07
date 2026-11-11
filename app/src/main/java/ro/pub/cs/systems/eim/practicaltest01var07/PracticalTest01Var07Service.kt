package ro.pub.cs.systems.eim.practicaltest01var07

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlin.random.Random

class PracticalTest01Var07Service : Service() {
    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Un singur fir de executie, chiar daca serviciul este pornit de mai multe ori.
        if (processingThread == null) {
            processingThread = ProcessingThread(this)
            processingThread?.start()
        }
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        processingThread?.stopThread()
        super.onDestroy()
    }
}

// Trimite la fiecare 10 secunde un mesaj cu difuzare ce contine patru valori intregi aleatoare.
class ProcessingThread(private val context: Context) : Thread() {

    @Volatile
    private var isRunning = true

    override fun run() {
        while (isRunning) {
            val intent = Intent()
            intent.action = Constants.ACTION_RANDOM_NUMBERS
            intent.putExtra(Constants.EXTRA_NUMBERS, IntArray(4) { Random.nextInt(0, 11) })
            context.sendBroadcast(intent)

            try {
                sleep(Constants.SLEEP_TIME)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun stopThread() {
        isRunning = false
        interrupt()
    }
}
