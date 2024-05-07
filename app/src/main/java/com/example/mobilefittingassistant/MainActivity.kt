package com.example.mobilefittingassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var shipName = "Rifter"

        val welcomeMessage = getString(R.string.welcome_message, shipName)
        findViewById<TextView>(R.id.welcome_message).text = welcomeMessage

        // i want the screen to jump to the fitting activity (handling it as an activity because
        // it will be a core part of the app), while still keeping the main activity, since
        // it will later have other options. so right now it gets a ship from the database, then
        // once it has gotten the ship it launches the fitting activity

        val shipRepo = ShipRepository()
        shipRepo.fetchShipByName(shipName) { ship->
            ship?.let {
                val intent = Intent (this, FittingActivity::class.java).apply {
                    putExtra("CURRENT_SHIP", ship)
                }
                startActivity(intent)
                // end this main activity so that if the user clicks back it doesnt go to this screen
                finish()
            } ?: run {
                Log.e("MainActivity", "Failed to load ship data for $shipName")
            }
        }
    }
}