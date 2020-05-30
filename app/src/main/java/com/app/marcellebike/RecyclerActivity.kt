package com.app.marcellebike
//Student Name: Marcelle Louise de Souza
//Student ID: 19534

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_recycler_detail_view.*
import kotlinx.android.synthetic.main.station_row.view.*
import java.net.HttpURLConnection
import java.net.URL


class RecyclerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_detail_view)

        // Test and run with a coloured background
        recyclerView_Main.setBackgroundColor(Color.WHITE)

        //Add a layout
        recyclerView_Main.layoutManager = LinearLayoutManager(this)

        var url = getString(R.string.DUBLIN_BIKE_API_URL) + getString(R.string.DUBLIN_BIKE_API_KEY)

        //get stations asynchronoursly
        AsyncTaskForJson().execute(url)

    }

    //this class will get the bike stations asynchronously
    inner class AsyncTaskForJson : AsyncTask<String, String, String>() {

        //get the stations in background
        override fun doInBackground(vararg url: String?): String {
            //open a http connection to get stations
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return text
        }

        //call the handleJson function when the stations have been obtain, this function will process, parse the result
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }

        private fun handleJson(jsonString: String?) {
            val gson = GsonBuilder().create()
            var listOfBikeStations = gson.fromJson(jsonString, Array<BikeStation>::class.java).toList()

            var intent = intent;
            val fav = intent.getBooleanExtra("favorites", false)


            //Use the favorite flag set in the intent to figure out if All Stations or just Favorite stations should be listed
            if( fav == false) {
                //Include all stations
                recyclerView_Main.adapter = MainAdapter(listOfBikeStations)
            }else{
                //Delete entries that are not favorites

                //Get list of favorite station Ids from Shared Preferences
                Log.i(getString(R.string.MAPLOGGING), "Station Favorites are loaded")
                var prefs =
                    getSharedPreferences("com.app.marcellebike", Context.MODE_PRIVATE)
                var favStations = prefs.getStringSet("favs", setOf())?.toMutableSet()

                favStations?.forEach{m ->  Log.i(getString(R.string.MAPLOGGING), "Favourite Station: ${m}")}

                //Loop through all Stations. If not in the favorites list, drop it.
                var i = 0
                for(station in listOfBikeStations){

                        if (favStations != null) {
                          if (favStations.contains(station.number)){
                          }else{
                              listOfBikeStations.drop(i)      //Not dropping. I blame Kotlin..... :(
                          }
                        }
                    i = i + 1
                    }
                recyclerView_Main.adapter = MainAdapter(listOfBikeStations)
                }
            }
        }
    }


class MainAdapter(val stations: List<BikeStation>) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.station_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return stations.size;
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.itemView.textName.text = stations[position].name
        holder.itemView.textAvailable.text = stations[position].available_bikes

        holder?.station = stations[position];
    }
}

class CustomViewHolder(view: View, var station: BikeStation? = null) : RecyclerView.ViewHolder(view) {

    companion object {
        val DETAIL_TITLE_KEY  = "ActionBarTitle"
    }

    init {
        view.setOnClickListener {
            Log.i("JSON", "Click on Recycle Item")

            val intent = Intent(view.context, StationActivity::class.java)
            //Include required properties in intent
            intent.putExtra("name1", station?.name)
            intent.putExtra("id", station?.number)
            intent.putExtra("avail", station?.available_bikes)

            view.context.startActivity(intent)
        }
    }
}

