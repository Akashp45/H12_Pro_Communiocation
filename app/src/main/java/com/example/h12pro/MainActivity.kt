package com.example.h12pro

import MavlinkConnectionManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.divpundir.mavlink.definitions.common.CommandLong

import com.example.h12pro.repository.DroneRepository
import com.example.h12pro.viewModel.DroneViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    private lateinit var logTextView: TextView
    private lateinit var armButton: Button

    private val viewModel by viewModels<DroneViewModel> {
        val conn = MavlinkConnectionManager(14550)
        val repo = DroneRepository(conn)
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return DroneViewModel(repo) as T
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        logTextView = findViewById(R.id.logTextView)
        armButton = findViewById(R.id.btn_arm)

        val job = lifecycleScope.launch {
            viewModel.heartbeat.collectLatest { msg ->
                Log.d("HeartBeat: ","heartBeat: ${msg}")
                appendLog(msg)
            }
        }

        armButton.setOnClickListener {
            viewModel.armDrone()
        }

        lifecycleScope.launch {
            viewModel.mavOdidArmStatus.collectLatest {status->
                Log.d("H12Pro","ArmStatus: ${status}")
                Toast.makeText(applicationContext,"Arm Status: ${status}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun appendLog(msg: String){
        logTextView.append("$msg\n")
        // auto-scroll to bottom
        val scrollView = logTextView.parent as ScrollView
        scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
    }

}