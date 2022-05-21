package com.example.mobiletechnologiesadvanced2022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentReadFeedbackBinding
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentWeatherMqttBinding
import com.example.mobiletechnologiesadvanced2022.datatypes.data.weatherstation.WeatherStation
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck


class WeatherMqttFragment : Fragment() {
    private var _binding: FragmentWeatherMqttBinding? = null
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherMqttBinding.inflate(inflater, container, false)
        val root: View = binding.root

        connectWeatherStation()



        return root
    }
    var MQTT_URL = "i5u4t3.messaging.internetofthings.ibmcloud.com"
    var MQTT_TOPIC = "iot-2/type/ws-10/id/3004/evt/data/fmt/json"
    var MQTT_CLIENT_ID = "a:i5u4t3:dfgdsfgdfghdgjaaeg"
    var MQTT_USERNAME = "a-i5u4t3-kg2otp2blv"
    var MQTT_PASSWORD ="iJt_MQqHikrls*)Cpc"

    private lateinit var client: Mqtt3AsyncClient

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }

    fun connectWeatherStation(){
        client = MqttClient.builder()
            .useMqttVersion3()
            .identifier(MQTT_CLIENT_ID)
            .serverHost(MQTT_URL)
            .serverPort(1883)
            .buildAsync()



        client.connectWith()
            .simpleAuth()
            .username(MQTT_USERNAME)
            .password(MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Connection failure.")
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
            }
    }

    fun subscribeToTopic()
    {
        client.subscribeWith()
            .topicFilter(MQTT_TOPIC)
            .callback { publish ->

                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                try {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    var ws: WeatherStation = gson.fromJson(result, WeatherStation::class.java)

                    var temperature = ws.d.get1().v.toString() + "°C"


                    activity?.runOnUiThread(Runnable {
                        binding.textViewWeatherstationTemperature.text = temperature

                    })
                }
                catch(e: Exception) {
                    Log.d("ADVTECH", e.message.toString())
                }
            }
            .send()
            .whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe
                    Log.d("ADVTECH", "Subscribe failed.")
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    Log.d("ADVTECH", "Subscribed!")
                }
            }
    }

}