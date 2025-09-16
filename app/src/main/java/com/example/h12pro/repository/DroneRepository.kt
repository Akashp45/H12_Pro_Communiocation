package com.example.h12pro.repository

import MavlinkConnectionManager
import com.divpundir.mavlink.definitions.common.CommandLong
import com.divpundir.mavlink.definitions.common.MavCmd
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

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

                "msg-> $msg\n\n\n"
            }
    }


//    suspend fun sendArmCommand(arm: Boolean) {
//        val command = CommandLong.builder()
//            .targetSystem(1u) // system ID (uint8)
//            .targetComponent(1u) // usually autopilot component = 1
//            .command(MavEnumValue.of(MavCmd.MAV_CMD_COMPONENT_ARM_DISARM))
//            .confirmation(0u)
//            .param1(if (arm) 1f else 0f) // 1 = arm, 0 = disarm
//            .param2(0f)
//            .param3(0f)
//            .param4(0f)
//            .param5(0f)
//            .param6(0f)
//            .param7(0f)
//            .build()
//
//        connection.send(command)
//    }
}