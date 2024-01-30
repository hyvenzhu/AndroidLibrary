package library.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

/**
 * MQTT 服务
 *
 * @author hyvenzhu
 * @version 2024/1/20
 */
class MqttService {

    companion object {
        const val TAG = "MqttService"

        @Volatile
        private var INSTANCE: MqttService? = null

        fun get(): MqttService {
            return INSTANCE ?: synchronized(this) {
                return MqttService().also { INSTANCE = it }
            }
        }
    }

    private val mqttConnectOptions by lazy {
        MqttConnectOptions().apply {
            isCleanSession = true
            connectionTimeout = MqttConfig.get().configParams.connectTimeout
            keepAliveInterval = MqttConfig.get().configParams.keepAliveInterval
            userName = MqttConfig.get().configParams.userName
            password = MqttConfig.get().configParams.password.toCharArray()
        }
    }

    private val connecting: AtomicBoolean = AtomicBoolean(false)

    private var retryCount = 0

    private lateinit var context: Context

    private lateinit var clientId: String

    private var autoReconnect = false

    private var maxConnectRetryCount = 10

    private var eventCallbackRef: WeakReference<MqttCallback>? = null

    private val mqttAndroidClient: MqttAndroidClient by lazy {
        MqttAndroidClient(context, MqttConfig.get().configParams.serviceURI, clientId).apply {
            setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, message: MqttMessage) {
                    Log.i(TAG, "收到消息：topic=$topic, qos=${message.qos}, isRetained=${message.isRetained}")
                    eventCallbackRef?.get()?.messageArrived(topic, message)
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.e(TAG, "连接断开", cause)
                    if (!reconnect()) {
                        eventCallbackRef?.get()?.connectionLost(cause)
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    Log.i(TAG, "发送数据成功：${token.topics.joinToString(",")}")
                    eventCallbackRef?.get()?.deliveryComplete(token)
                }
            })
        }
    }

    fun getStatus(): MqttStatusEnum {
        return when {
            connecting.get() -> MqttStatusEnum.CONNECTING
            mqttAndroidClient.isConnected -> MqttStatusEnum.CONNECTED
            else -> MqttStatusEnum.DISCONNECTED
        }
    }

    fun connect(
        context: Context, clientId: String,
        autoReconnect: Boolean = true, maxConnectRetryCount: Int = 10,
        actionCallback: IMqttActionListener? = null, eventCallback: MqttCallback? = null
    ) {
        this.context = context
        this.clientId = clientId
        this.autoReconnect = autoReconnect
        this.maxConnectRetryCount = maxConnectRetryCount
        this.eventCallbackRef = WeakReference(eventCallback)
        retryCount = 0
        if (!mqttAndroidClient.isConnected) {
            Log.i(TAG, "正在连接 ${MqttConfig.get().configParams.serviceURI}...")
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i(TAG, "连接成功")
                    retryCount = 0
                    connecting.compareAndSet(true, false)
                    actionCallback?.onSuccess(asyncActionToken)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e(TAG, "连接失败", exception)
                    if (!reconnect()) {
                        actionCallback?.onFailure(asyncActionToken, exception)
                    }
                }
            })
        } else {
            Log.i(TAG, "当前已连接，无需连接")
            connecting.compareAndSet(true, false)
            actionCallback?.onSuccess(null)
        }
    }

    fun subscribe(topic: String, qos: Int, callback: IMqttActionListener? = null) {
        if (!mqttAndroidClient.isConnected) {
            Log.e(TAG, "连接已断开")
            return
        }

        mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.i(TAG, "topic 订阅成功：${asyncActionToken.topics?.joinToString(",")}")
                callback?.onSuccess(asyncActionToken)
            }

            override fun onFailure(
                asyncActionToken: IMqttToken,
                exception: Throwable
            ) {
                Log.e(TAG, "topic 订阅失败：${asyncActionToken.topics?.joinToString(",")}", exception)
                callback?.onFailure(asyncActionToken, exception)
            }
        })
    }

    fun publish(topic: String, payload: ByteArray, qos: Int, retained: Boolean, callback: IMqttActionListener? = null) {
        if (!mqttAndroidClient.isConnected) {
            Log.e(TAG, "连接已断开")
            return
        }

        mqttAndroidClient.publish(topic, payload, qos, retained, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.i(TAG, "发送数据成功")
                callback?.onSuccess(asyncActionToken)
            }

            override fun onFailure(
                asyncActionToken: IMqttToken,
                exception: Throwable?
            ) {
                Log.e(TAG, "数据发送失败", exception)
                callback?.onFailure(asyncActionToken, exception)
            }
        })
    }

    fun disconnect(quiesceTimeout: Long = 0L) {
        autoReconnect = false
        if (!mqttAndroidClient.isConnected) {
            Log.e(TAG, "连接已断开")
            return
        }

        mqttAndroidClient.disconnect(quiesceTimeout)
    }

    fun unsubscribe(topic: String) {
        if (!mqttAndroidClient.isConnected) {
            Log.e(TAG, "连接已断开")
            return
        }

        mqttAndroidClient.unsubscribe(topic)
    }

    /**
     * 尝试重连，等待时间和失败次数转换公式：sleepTime = retryCount * 6000
     */
    private fun reconnect(): Boolean {
        return if (autoReconnect && retryCount < maxConnectRetryCount) {
            Log.e(TAG, "正在尝试重连...")
            if (connecting.compareAndSet(false, true)) {
                retryCount++
                try {
                    Log.i(TAG, "等待${retryCount * 6}秒后尝试重连")
                    Thread.sleep(retryCount * 6000L)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "reconnect interrupted", e)
                }
                connect(context, clientId, autoReconnect = autoReconnect, maxConnectRetryCount = maxConnectRetryCount)
                true
            } else {
                Log.e(TAG, "")
                false
            }
        } else {
            connecting.set(false)
            Log.e(TAG, "未尝试重连, 自动重连: $autoReconnect, 重连次数: $retryCount, 最大重连次数: $maxConnectRetryCount")
            false
        }
    }
}