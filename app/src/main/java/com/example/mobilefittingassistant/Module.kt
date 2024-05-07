package com.example.mobilefittingassistant

// i want one of the variables in my module class to be only one of 3 values
enum class Slot {NULL, LOW, MED, HIGH }

data class Module (
     val module_name: String = "",
     val slot: Slot = Slot.NULL,
     val effects: Map<String, Float> = emptyMap()
) {
    override fun toString(): String {
        return "Module name: ${module_name}, slot: $slot, effects: $effects"
    }
}