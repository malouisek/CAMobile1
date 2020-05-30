package com.app.marcellebike
//Student Name: Marcelle Louise de Souza
//Student ID: 19534

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RecyclerDetailView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_detail_view)

        // NOTICE USING THE CONSTANT FROM COMPANION OBJECT
        supportActionBar?.title = intent.getStringExtra(CustomViewHolder.DETAIL_TITLE_KEY)

    }
}
