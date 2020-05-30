package com.app.marcellebike
//Student Name: Marcelle Louise de Souza
//Student ID: 19534

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
    }

    //Launch the Maps Activity
    fun switchToMaps(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    //Launch RecyclerView (Favorites Flag set to False)
    fun switchToRecyclerActivity(view: View) {
        val intent = Intent(this, RecyclerActivity::class.java)
        intent.putExtra("favorites", false )
        startActivity(intent)
    }

    ////Launch RecyclerView (Favorites Flag set to True)
    fun switchToFavActivity(view: View) {
        val intent = Intent(this, RecyclerActivity::class.java)
        intent.putExtra("favorites", true )
        startActivity(intent)
    }

}
