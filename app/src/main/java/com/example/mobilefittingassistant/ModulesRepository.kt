package com.example.mobilefittingassistant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModulesRepository {

    // first i connect to the database
    private val db = Firebase.firestore

    // then i make lists for each of the slots possible
    val lowSlotModules = MutableLiveData<List<Module>>()
    val medSlotModules = MutableLiveData<List<Module>>()
    val highSlotModules = MutableLiveData<List<Module>>()

    // making 3 different functions for the different slots. plus also making it so that it will
    // fetch as many modules as as there are slots of a certain type on the ship that we are fitting
    fun fetchLowSlotModules(maxCount: Int) {
        fetchModulesBySlotType(Slot.LOW, maxCount, lowSlotModules)
    }

    fun fetchMedSlotModules(maxCount: Int) {
        fetchModulesBySlotType(Slot.MED, maxCount, medSlotModules)
    }

    fun fetchHighSlotModules(maxCount: Int) {
        fetchModulesBySlotType(Slot.HIGH, maxCount, highSlotModules)
    }

    // general function so we dont have to rewrite the code over and over. connects to the database,
    // finds modules that fit a certain slot, then for each slot count that we have, we put it into
    // the list of currently selected modules for the ship. also adding an error check
    private fun fetchModulesBySlotType(slotType: Slot, maxCount: Int, liveData: MutableLiveData<List<Module>>) {
        db.collection("modules")
            .whereEqualTo("slot", slotType.name)
            .get()
            .addOnSuccessListener { result ->
                // get a list of potential modules from the database
                val potentialModules = mutableListOf<Module>()
                for (document in result) {
                    val module = documentToModule(document)
                    if (module != null) {
                        potentialModules.add(module)
                    }
                }
                // now make a list of actual modules on the ship (to fill slots)
                val shipModules = mutableListOf<Module>()
                if (potentialModules.isNotEmpty()) {
                    repeat(maxCount) {
                        val randomModule = potentialModules.random()
                        shipModules.add(randomModule)
                        Log.d("module repository", "adding $randomModule")
                    }
                }
                liveData.postValue(shipModules)
            }
            .addOnFailureListener { e ->
                Log.e("ModuleRepository", "Error fetching ${slotType.name} slot modules", e)
            }
    }

    // have to define my own mapping function because my class needs some versatility, more than firebase has
    private fun documentToModule(document: QueryDocumentSnapshot): Module {
        // gets initial data. if there is nothing from the firebase query then it sets it as empty module
        val moduleName = document.getString("module_name") ?: ""
        val slot = Slot.valueOf(document.getString("slot") ?: "NULL")
        val effects = mutableMapOf<String, Float>()

        // so now we can parse through the different effects in the different modules and save them in the map
        for ((key, value) in document.data) {
            // ignore the name and slot pieces of data
            if (key!= "module_name" && key!= "slot" && value is Number) {
                effects[key] = value.toFloat()
            }
        }
        return Module(moduleName, slot, effects)
    }
}
