package library.mqtt

/**
 * Mqtt 配置项
 *
 * @author hyvenzhu
 * @version 2022/2/9
 */
class MqttConfig private constructor(val configParams: ConfigParams) {

    companion object {

        @Volatile
        private var INSTANCE: MqttConfig? = null

        fun init(configParams: ConfigParams) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MqttConfig(configParams).also { INSTANCE = it }
            }
        }

        fun get(): MqttConfig =
            INSTANCE ?: throw RuntimeException("MqttConfig must call init first!!!")
    }
}

class ConfigParams(
    val serviceURI: String,
    val userName: String,
    val password: String,
    val connectTimeout: Int = 10,
    val keepAliveInterval: Int = 20
)