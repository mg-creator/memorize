package com.jedi.memorize.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getColor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.jedi.memorize.R

class MapFragment : Fragment(), LocationListener, OnMapReadyCallback {

    private lateinit var content: View
    private lateinit var locationManager: LocationManager
    private lateinit var map: GoogleMap
    private val circleOptions = CircleOptions()
    private lateinit var zoomIn: Button
    private lateinit var zoomOut: Button
    private lateinit var biggerCircle: Button
    private lateinit var smallerCircle: Button
    private var position = LatLng(41.3851, 2.1734)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        content = activity!!.findViewById(android.R.id.content)
        zoomIn = view.findViewById(R.id.addZoomButton)
        zoomOut = view.findViewById(R.id.subZoomButton)
        biggerCircle = view.findViewById(R.id.addRadius)
        smallerCircle = view.findViewById(R.id.subRadius)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        circleOptions.center(position)
        circleOptions.radius(500.0)
        circleOptions.fillColor(getColor(this.context as Activity, R.color.dorado_borde_carta_semi))
        circleOptions.strokeColor(getColor(this.context as Activity, R.color.dorado_borde_carta))
        circleOptions.strokeWidth(4f)

        if (ActivityCompat.checkSelfPermission(
                this.activity as Context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this.activity as Context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 123)
        } else {
            getLocation()
        }

        zoomIn.setOnClickListener { map.moveCamera(CameraUpdateFactory.zoomIn()) }
        zoomOut.setOnClickListener { map.moveCamera(CameraUpdateFactory.zoomOut()) }

        biggerCircle.setOnClickListener {
            map.clear()
            map.addMarker(MarkerOptions().position(position).title("Your position"))
            circleOptions.radius(circleOptions.radius + 500)
            map.addCircle(circleOptions)
        }
        smallerCircle.setOnClickListener {
            map.clear()
            map.addMarker(MarkerOptions().position(position).title("Your position"))
            circleOptions.radius(circleOptions.radius - 500)
            map.addCircle(circleOptions)
        }

        return view
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10L,
                1000F,
                this
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123 && grantResults.isNotEmpty()) {
            getLocation()
        } else {
            Snackbar.make(content,"Ha habido un problema al connectar con el servidor.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onLocationChanged(location: Location) {
        position = LatLng(location.latitude, location.longitude)
        map.addMarker(MarkerOptions().position(position).title("Your position"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f))

        circleOptions.center(position)
        map.addCircle(circleOptions)
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 5.0f))
    }
}