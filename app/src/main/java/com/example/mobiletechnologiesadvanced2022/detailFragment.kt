package com.example.mobiletechnologiesadvanced2022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentDetailBinding
import com.google.gson.GsonBuilder


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    // get fragment parameters from previous fragment
    val args: DetailFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
// print out the given parameter into logs
        Log.d("ADVTECH", "" + args.id.toString())

        val JSON_URL = "https://jsonplaceholder.typicode.com/todos/" +args.id.toString()

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("ADVTECH", response)

                val gson = GsonBuilder().setPrettyPrinting().create()

                var toDoItem : ToDo = gson.fromJson(response, ToDo::class.java)

                binding.textViewDetailId.text = toDoItem.id.toString()
                binding.textViewDetailUserId.text = toDoItem.id.toString()
                binding.textViewDetailTitle.text = toDoItem.title
                binding.textViewDetailCompleted.text = toDoItem.completed.toString()






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

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)


        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

