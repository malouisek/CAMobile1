package com.app.marcellebike
//Student Name: Marcelle Louise de Souza
//Student ID: 19534

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_station.*

class StationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)

        // All needed info included in the intent.
        var intent = intent;

        var stationLatitude = intent.getStringExtra("lat")
        var stationLongitude = intent.getStringExtra("lng")
        var stationId = intent.getStringExtra("id")
        var stationName = intent.getStringExtra("name1")
        var stationsAvail = intent.getStringExtra("avail")

        textViewLatitude.text = stationLatitude
        textViewLongitude.text = stationLongitude
        textViewAvail.text = stationsAvail
        textViewName.text = stationName

        //Set current station as a favorite
        buttonSavePreferences.setOnClickListener {
            saveAsFavourite("${stationId}")
            fun Context.toast(message: CharSequence) =
                Toast.makeText(this, "Saved as Favorite", Toast.LENGTH_SHORT).show()
        }
    }

    //Modify Shared Preferences to save list of favorited station numbers
    fun saveAsFavourite(station: String) {
        var prefs =
            getSharedPreferences("com.app.marcellebike", Context.MODE_PRIVATE)
        var favs = prefs.getStringSet("favs", setOf())?.toMutableSet()
        favs?.add(station)
        prefs.edit().putStringSet("favs", favs).apply()
        Log.i(getString(R.string.MAPLOGGING), "Favorite Preferences are saved as ${station}")

       finish()
    }
}
