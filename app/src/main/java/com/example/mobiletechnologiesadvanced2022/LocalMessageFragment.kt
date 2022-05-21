package com.example.mobiletechnologiesadvanced2022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentLocalMessageBinding
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentWeatherMqttBinding
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.util.*


class LocalMessageFragment : Fragment() {

    private var _binding: FragmentLocalMessageBinding? = null
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root


        connectLocalBroker()
        return root
    }

    var MQTT_URL = "10.0.2.2"
    var MQTT_TOPIC ="test/topic"
    private lateinit var client: Mqtt3AsyncClient

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }

    fun connectLocalBroker() {
        client = MqttClient.builder()
            .useMqttVersion3()
            .identifier(UUID.randomUUID().toString())
            .serverHost(MQTT_URL)
            .serverPort(1883)
            .buildAsync();




        client.connect()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Can´t connect")
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

                activity?.runOnUiThread(Runnable {
                    binding.textViewLocalMessageResult.text = result

                })

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

