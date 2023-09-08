package com.hubx.spikenetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        var netInfoText = findViewById<TextView>(R.id.netText)
//
//        connectivityObserver = NetworkConnectivityObserver(applicationContext)
//        connectivityObserver.observe().onEach {
//            println(">>>>>>>> Status is $it")
//            netInfoText.text = "Network status: $it"
//        }

//        val status by connectivityObserver.observe().collectAsState(
//            initial = ConnectivityObserver.Status.Unavailable
//        )

//        networkMonitor.isConnected.collectAsState(true)
//
//        val connectivityManager = getSystemService(ConnectivityManager::class.java)
//        netInfoText.text = connectivityManager.activeNetwork.toString()

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val textCount = findViewById<TextView>(R.id.tvCount)
        val buttonCount = findViewById<Button>(R.id.btnResetCount)

//        textCount.text = viewModel.count.toString()
        viewModel.count.observe(this, Observer {
            textCount.text = it.toString()
        })

        buttonCount.setOnClickListener {
//            count++
//            textCount.text = count.toString()
            viewModel.resetsCount()
//            textCount.text = viewModel.count.toString()
        }

//      Network Status by StateFlow
        val textNetworkStatus = findViewById<TextView>(R.id.tvNetworkStatus)
        var buttonStartNetworkMonitoring = findViewById<Button>(R.id.btnStartNetworkMoniro)
        var buttonStopNetworkMonitoring = findViewById<Button>(R.id.btnStopNetworkMonitor)

        // default start monitoring
        networkConnectionManager.startListenNetworkState()

        buttonStartNetworkMonitoring.setOnClickListener {
            networkConnectionManager.startListenNetworkState()
        }

        buttonStopNetworkMonitoring.setOnClickListener {
            networkConnectionManager.stopListenNetworkState()
        }

        networkConnectionManager.isNetworkConnectedFlow.onEach {
            if (it) {
                textNetworkStatus.text = "Network Status: Connected"
            } else {
                textNetworkStatus.text = "Network Status: Disconnected"
                Log.i("NET", ">>> disconnected event")
                viewModel.updateCount()
            }
        }.launchIn(lifecycleScope)
    }
}