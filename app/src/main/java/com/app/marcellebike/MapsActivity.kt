package com.app.marcellebike
//Student Name: Marcelle Louise de Souza
//Student ID: 19534

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var listOfBikeStations: List<BikeStation>
    lateinit var mkhelper: MarkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        retrieveFavourites()
    }

    override fun onResume() {
        super.onResume()
        Log.i(getString(R.string.MAPLOGGING), "onResume")

//        retrieveFavourites()
    }
    //When Map is ready, set Markers and start Listener
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getBikeStationJsonData()
        setMarkerListener()
    }

    //Get station data from Dublin Bikes API
    fun getBikeStationJsonData() {

        var url = getString(R.string.DUBLIN_BIKE_API_URL) + getString(R.string.DUBLIN_BIKE_API_KEY)

        //  a request object
        val request = Request.Builder().url(url).build()

        // Create a client
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(getString(R.string.MAPLOGGING), "http fail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i(getString(R.string.MAPLOGGING), "http success")

                // Get the response body
                val body = response?.body?.string()

                // Create a json builder object
                val gson = GsonBuilder().create()

                listOfBikeStations = gson.fromJson(body, Array<BikeStation>::class.java).toList()

                renderListOfBikeStationMarkers()

            }
        })
    }

    //Add all Markers to the Map
    fun renderListOfBikeStationMarkers() {

        runOnUiThread {

            listOfBikeStations.forEach {
                val position = LatLng(it.position.lat, it.position.lng)
                var marker1 =
                    mMap.addMarker(
                        MarkerOptions().position(position).title("${it.name}")
                    )

                //Used MarkHelper to store many properties to the marker.tag. (Needed for Intent)
                marker1.setTag(MarkHelper(it.name, it.number,it.available_bikes))
                Log.i(getString(R.string.MAPLOGGING), it.name)
            }

            val centreLocation = LatLng(53.349562, -6.278198)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLocation, 14.0f))

        }
    }


    fun setMarkerListener() {

        mMap.setOnMarkerClickListener { marker ->
            Log.i(getString(R.string.MAPLOGGING), getString(R.string.MAKERCLICKED))

            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }

            // Pass in data and add a layout
            val intent = Intent(this, StationActivity::class.java)

            //Use MarkHelper from tag to add needed values to Intent
            mkhelper = marker.getTag() as MarkHelper

            intent.putExtra("id", mkhelper.number)
            intent.putExtra("name1",mkhelper.name)
            intent.putExtra("avail", mkhelper.available_bikes)

            startActivity(intent)

            true
        }

    }


//    fun retrieveFavourites() {
//        Log.i(getString(R.string.MAPLOGGING), "Marker Preferences are loaded")
//        var prefs =
//            getSharedPreferences("com.app.gmarcellebike", Context.MODE_PRIVATE)
//        var markers = prefs.getStringSet("stationmarkers", setOf())?.toMutableSet()
//
//       markers?.forEach{m ->  Log.i(getString(R.string.MAPLOGGING), "Favourite Marker: ${m}")}
//
//    }
}





