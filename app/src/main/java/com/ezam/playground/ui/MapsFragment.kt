package com.ezam.playground.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezam.playground.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass.
 * Use the [MapsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var gmap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val view = super.onCreateView(inflater, container, savedInstanceState)
        getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap

        val cacahoatan = LatLng( 14.990223, -92.167980 )
        val zoom = 17f

        gmap.addMarker( MarkerOptions().position(cacahoatan))
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(cacahoatan, zoom))
    }

}