package com.example.mobiletechnologiesadvanced2022

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobiletechno.Feedback
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentReadFeedbackBinding
import com.google.android.gms.maps.model.Marker
import com.google.gson.GsonBuilder
import org.json.JSONObject

class ReadFeedbackFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentReadFeedbackBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadFeedbackBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getFeedback()

        binding.buttonRefreshfeedback.setOnClickListener{
            getFeedback()
        }

        binding.buttonSendFeedbackFragment.setOnClickListener {

            val action = ReadFeedbackFragmentDirections.actionReadFeedbackFragmentToSendFeedbackFragment()
            findNavController().navigate(action)
        }

        return root
    }

    var feedbacks : List<Feedback> = emptyList();

    fun getFeedback() {
        // Note: 10.0.2.2 is for the Android -emulator, it redirects to your computers localhost!
        val JSON_URL = "http://10.0.2.2:80/apigility/public/feedback"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->
           // Log.d("MYLOG", response)
                val gson = GsonBuilder().setPrettyPrinting().create()

                val jObject = JSONObject(response)
                val embedObject = jObject.getJSONObject("_embedded")

                // in this case feedback is the name of our service
                val jArray = embedObject.getJSONArray("feedback")

                feedbacks = gson.fromJson(jArray.toString() , Array<Feedback>::class.java).toList()

                for(f in feedbacks){
                    var text = f.name + ", " + f.value
                    Log.d("MYLOG", text)
                }

                val adapter = ArrayAdapter(context as MainActivity, R.layout.simple_list_item_1, feedbacks)
                binding.listViewFeedback.adapter = adapter
                binding.listViewFeedback.setAdapter(adapter)
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("MYERROR", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise Apigility will block our queries!
                // define we are after JSON data!
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }


        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}