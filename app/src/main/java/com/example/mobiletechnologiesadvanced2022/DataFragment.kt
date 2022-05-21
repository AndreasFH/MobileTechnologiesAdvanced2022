package com.example.mobiletechnologiesadvanced2022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentDataBinding
import com.google.gson.GsonBuilder




class DataFragment : Fragment() {
    private var _binding: FragmentDataBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    //for recyclerview
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //init recyclerview
        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = linearLayoutManager

        // navigate to another fragment, pass some parameter too
        binding.getDataButton.setOnClickListener {



            // this is the url where we want to get our data from
            val JSON_URL = "https://jsonplaceholder.typicode.com/todos"

            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.GET, JSON_URL,
                Response.Listener { response ->

                    // print the response as a whole
                    // we can use GSON to modify this response into something more usable
                    Log.d("ADVTECH", response)

                    val gson = GsonBuilder().setPrettyPrinting().create()

                    var rows : List<ToDo> = gson.fromJson(response, Array<ToDo>::class.java).toList()



                    adapter = RecyclerAdapter(rows)
                    binding.recyclerView.adapter = adapter



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

        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}