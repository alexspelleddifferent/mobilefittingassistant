package com.example.mobilefittingassistant
import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

// this is to make it so that an object of this type can be passed between activities via intents
@Parcelize
class Ship (
    // firebase needs these default values. otherwise i wanted to not have that in there
    // these are the base ship stats that are stored in firebase
    val shipName : String = "",
    val lowSlots: Int = 0,
    val medSlots: Int = 0,
    val highSlots: Int = 0,
    val baseShields: Float = 0.0f,
    val baseArmor: Float = 0.0f,
    val baseDps: Float = 0.0f,
    val baseCapRegen: Float = 0.0f,
    val baseRepair: Float = 0.0f,
    val imageFile: String = "",

    // these are the derived stats
    var currentShields: Float = baseShields,
    var currentArmor: Float = baseArmor,
    var currentDps: Float = baseDps,
    var currentCapRegen: Float = baseCapRegen,
    var currentRepair: Float = baseRepair

) : Parcelable {

    init {
        // so that all the current stats get updated when we populate the base stats
        resetCurrentStats()
    }

    fun resetCurrentStats() {
        currentShields = baseShields
        currentArmor = baseArmor
        currentDps = baseDps
        currentCapRegen = baseCapRegen
        currentRepair = baseRepair
    }

    fun applyModuleToShipStats(module: Module) {
        for (eachEffect in module.effects) {
            when (eachEffect.key) {
                "shields" -> currentShields += eachEffect.value
                "armor" -> currentArmor += eachEffect.value
                "dps" -> currentDps += eachEffect.value
                "capRegen" -> currentCapRegen += eachEffect.value
                "repair" -> currentRepair += eachEffect.value
            }
        }
    }

    fun removeModuleFromShipStats(module: Module) {
        for (eachEffect in module.effects) {
            when (eachEffect.key) {
                "shields" -> currentShields -= eachEffect.value
                "armor" -> currentArmor -= eachEffect.value
                "dps" -> currentDps -= eachEffect.value
                "capRegen" -> currentCapRegen -= eachEffect.value
                "repair" -> currentRepair -= eachEffect.value
            }
        }
    }
}

