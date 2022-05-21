package com.example.mobiletechnologiesadvanced2022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentCityWeatherBinding
import com.example.mobiletechnologiesadvanced2022.datatypes.data.cityweather.CityWeather
import com.google.gson.GsonBuilder

class CityWeatherFragment : Fragment() {

    private var _binding: FragmentCityWeatherBinding? = null

    private val binding get() = _binding!!

    val args: CityWeatherFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.d("ADVTECH", "my city" + args.city)

        var API_KEY : String = BuildConfig.OPEN_WEATHER_API_KEY

        var city : String = args.city

        binding.buttonReturn.setOnClickListener{
            val action = CityWeatherFragmentDirections.actionCityWeatherFragmentToMapsFragment()
            findNavController().navigate(action)
        }

        val JSON_URL = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API_KEY"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("ADVTECH", response)

                val gson = GsonBuilder().setPrettyPrinting().create()

                var item : CityWeather = gson.fromJson(response, CityWeather::class.java)
                    Log.d ("ADVTECH","temp today:" + item.main?.temp)

                binding.textViewWeatherHumidity.text = item.main?.humidity.toString() + "%"
                binding.textViewWeatherTemperature.text = item.main?.temp.toString() + "° Celsius"
                binding.textViewWeatherWindspeed.text = item.wind?.speed.toString() + "ms"


            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }


        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}