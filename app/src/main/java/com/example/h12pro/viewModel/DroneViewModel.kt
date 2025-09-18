package com.example.h12pro.viewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divpundir.mavlink.definitions.common.MavOdidArmStatus
import com.example.h12pro.repository.DroneRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception

class DroneViewModel(private val repo: DroneRepository) : ViewModel() {

    private val _heartbeat = MutableStateFlow("Waiting for heartbeat..")
    val heartbeat: StateFlow<String> = _heartbeat

    private val _armStatus = MutableStateFlow("Waiting for arm status..")
    val mavOdidArmStatus: StateFlow<String>  = _armStatus

    init {
//         Collect heartbeat
        viewModelScope.launch {
            try {
                repo.observeHeartbeat().collect { msg ->
                    _heartbeat.value = msg
                }
            } catch (e: Exception) {
//                _heartbeat.value = "Heartbeat timeout or error"
            }
        }

//        viewModelScope.launch {
//            repo.observeMavOdiArmStatus().collect{ msg->
//                _armStatus.value = msg
//            }
//        }
    }

    fun armDrone(){
        viewModelScope.launch {
            val success = repo.armDrone()
            if(success){
                _armStatus.value = "Arm command send successfully.. "
            }else{
                _armStatus.value = "Failed to send arm command.."
            }
        }
    }
}
