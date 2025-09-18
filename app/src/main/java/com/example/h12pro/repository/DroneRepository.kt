package com.example.h12pro.repository

import MavlinkConnectionManager
import com.divpundir.mavlink.api.MavEnumValue
import com.divpundir.mavlink.definitions.common.CommandLong
import com.divpundir.mavlink.definitions.common.MavCmd
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.lang.Exception

class DroneRepository(private val connection: MavlinkConnectionManager) {
    suspend fun observeHeartbeat(): Flow<String> {
        return connection.listen()
            .map { it.message }
            .mapNotNull { msg ->
//                when (msg) {
//                    is Heartbeat -> "Heartbeat: autopilot=${msg.autopilot.entry?.name}\n\n\n"
//                    is GlobalPositionInt -> "GPS: lat=${msg.lat/1e7}, lon=${msg.lon/1e7}, alt=${msg.alt/1000.0}m\n\n\n"
//                    is Attitude -> "Attitude: roll=${msg.roll}, pitch=${msg.pitch}, yaw=${msg.yaw}\n\n\n"
//                    is VfrHud -> "Speed: ${msg.groundspeed} m/s, Climb: ${msg.climb} m/s\n\n\n"
//                    is SysStatus -> "Battery: ${msg.batteryRemaining}%\n\n\n"
//                    else -> null
//                }
                if (msg.toString().contains("MavOdiArm", ignoreCase = true)) {
                    "MavOdiArm Status: $msg\n\n\n"
                } else {
                    "msg-> $msg\n\n\n"
                }
            }
    }

    suspend fun armDrone(): Boolean{
        return try {
            val armCmd = CommandLong(
                targetSystem = 1u,
                targetComponent = 1u,
                command = MavEnumValue.of(MavCmd.COMPONENT_ARM_DISARM),
                confirmation = 0u,
                param1 = 1f,
                param2 = 0f,
                param3 = 0f,
                param4 = 0f,
                param5 = 0.0f,
                param6 = 0.0f,
                param7 = 0f
            )
            connection.connection.sendUnsignedV2(
                systemId = 255u,
                componentId = 0u,
                armCmd
            )
            return true
        }catch (e: Exception){
            false
        }
    }

    suspend fun observeMavOdiArmStatus(): Flow<String> {
        return connection.listen()
            .map { it.message }
            .mapNotNull { msg ->
                // Filter for MavOdiArm status messages
                if (msg.toString().contains("MavOdiArm", ignoreCase = true)) {
                    "MavOdiArm Status: $msg\n\n\n"
                } else {
                    null
                }
            }
    }

}