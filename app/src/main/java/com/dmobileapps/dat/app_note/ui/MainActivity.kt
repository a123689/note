package com.dmobileapps.dat.app_note.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dmobileapps.dat.app_note.R
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }
}