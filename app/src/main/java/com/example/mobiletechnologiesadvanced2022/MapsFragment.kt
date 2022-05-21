package com.example.mobiletechnologiesadvanced2022

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.fragment.findNavController
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentDataBinding
import com.example.mobiletechnologiesadvanced2022.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_data.*

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {


    private var _binding: FragmentMapsBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        gMap = googleMap

        googleMap.setOnMarkerClickListener(this)

        val sydney = LatLng(-34.0, 151.0)

       val marker1: Marker? = googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
       marker1?.tag="Sydney"
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val lustenau = LatLng(47.42135692148401, 9.659376204378459)
        val marker2: Marker? = googleMap.addMarker(MarkerOptions().position(lustenau).title("Lustenau"))
        marker2?.tag="Lustenau"
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lustenau))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.checkBoxZoomControls.setOnCheckedChangeListener {getDataButton, isChecked ->
            Log.d("MYLOG", isChecked.toString())

            gMap.uiSettings.isZoomControlsEnabled = isChecked
        }



        binding.radioGroupMapType.setOnCheckedChangeListener{radioGroup, id ->
            when (id) {
                binding.radioHybrid.id ->  gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                binding.radioNormal.id -> gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                binding.radioTerrain.id -> gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }

        }

        return root
        //return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMarkerClick(p0: Marker): Boolean {
       Log.d("MYLOG", p0.tag.toString())

        val action = MapsFragmentDirections.actionMapsFragmentToCityWeatherFragment(p0.tag.toString())
        findNavController().navigate(action)

        return false
    }

}