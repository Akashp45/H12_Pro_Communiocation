import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.DatagramPacket
import java.net.DatagramSocket


import com.divpundir.mavlink.connection.udp.UdpServerMavConnection
import com.divpundir.mavlink.adapters.coroutines.asCoroutine
import com.divpundir.mavlink.api.MavEnumValue
import com.divpundir.mavlink.api.MavFrame
import com.divpundir.mavlink.api.MavMessage
import com.divpundir.mavlink.definitions.common.CommandLong
import com.divpundir.mavlink.definitions.common.CommonDialect
import kotlinx.coroutines.CoroutineScope
import com.divpundir.mavlink.definitions.common.MavCmd


class MavlinkConnectionManager (private val udpPort: Int = 14550) {

    val connection = UdpServerMavConnection(udpPort, CommonDialect).asCoroutine()

    suspend fun listen(): Flow<MavFrame<out MavMessage<*>>> {
//        val connection = UdpServerMavConnection(udpPort, CommonDialect).asCoroutine()
        connection.connect(CoroutineScope( Dispatchers.IO))
        Log.d("Terrain", "MAVLink connection started on port $udpPort")

        return connection.mavFrame
    }
}