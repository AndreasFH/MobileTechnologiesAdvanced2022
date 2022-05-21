package com.example.mobiletechnologiesadvanced2022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobiletechno.Feedback
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentReadFeedbackBinding
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentSendFeedbackBinding
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException

class SendFeedbackFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentSendFeedbackBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendFeedbackBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSubmit.setOnClickListener {
            if(binding.editTextEditLocation.length() > 0 && binding.editTextEditName.length() >0 && binding.editTextEditValue.length() >0) {
                sendFeedback()
                val action = SendFeedbackFragmentDirections.actionSendFeedbackFragmentToReadFeedbackFragment()
                findNavController().navigate(action)
            }
        }
        return root
    }


    fun sendFeedback() {
        val JSON_URL = "http://10.0.2.2/apigility/public/feedback"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, JSON_URL,
            Response.Listener { response ->

            },
            Response.ErrorListener {
                Log.d("MYERROR", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }


            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                var body = ByteArray(0)
                try {
                    var newData = ""

                    var f : Feedback = Feedback();
                    f.location = binding.editTextEditLocation.text.toString();
                    f.name = binding.editTextEditName.text.toString();
                    f.value = binding.editTextEditValue.text.toString()

                    var gson = GsonBuilder().create();
                     newData = gson.toJson(f);

                    body = newData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                }
                return body
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}