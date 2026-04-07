package org.example.btvnkotlin

import android.app.Application
import android.util.Log
import com.cloudinary.android.MediaManager
import com.google.firebase.FirebaseApp

class BTVNKotlinApp : Application() {
    
    companion object {
        private const val TAG = "BTVNKotlinApp"
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application onCreate started")
        
        try {
            // Init Firebase
            FirebaseApp.initializeApp(this)
            Log.d(TAG, "Firebase initialized successfully")

            // Init Cloudinary
            val config = mapOf(
                "cloud_name" to "dwzsc2t3i",
                "api_key" to "933989674644269",
                "api_secret" to "DtJWlM3ZVRSxuop7M1wkD7x48iU"
            )
            MediaManager.init(this, config)
            Log.d(TAG, "Cloudinary initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing: ${e.message}", e)
        }
    }
}
