package com.example.mobilefittingassistant

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShipRepository {

    // first i connect to the database
    private val db = Firebase.firestore

    fun fetchShipByName (shipName: String, completion: (Ship?) -> Unit){
        db.collection("ships")
            .whereEqualTo("shipName", shipName)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    val ship = result.documents[0].toObject(Ship::class.java)
                    completion(ship)
                    Log.d("ship repository", "fetched intended ship")
                } else {
                    completion(null)
                    Log.d("ship repository", "fetched an empty ship")
                }
            }
            .addOnFailureListener { error ->
                Log.e("ship repository", "error fetching ship $shipName", error)
                completion(null)
            }
    }
}