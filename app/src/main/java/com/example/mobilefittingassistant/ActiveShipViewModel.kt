package com.example.mobilefittingassistant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActiveShipViewModel : ViewModel() {

    private val repository = ModulesRepository()

    val lowSlotModules: LiveData<List<Module>> = repository.lowSlotModules
    val medSlotModules: LiveData<List<Module>> = repository.medSlotModules
    val highSlotModules: LiveData<List<Module>> = repository.highSlotModules

    // solution for how to add the ship that is in the intent into the viewmodel
    // _ is the ship data tha is malleable and is hidden within the viewmodel
    private val _currentShip = MutableLiveData<Ship>()
    // currentship is the one that is accessible outside of the viewmodel
    val currentShip: LiveData<Ship> get() = _currentShip

    fun setShip(ship: Ship) {
        // initializes ship
        ship.resetCurrentStats()
        fetchAModulesForShip(ship)
        _currentShip.value = ship
    }

    private fun fetchAModulesForShip(ship: Ship) {
        // fetches all the modules using the repository
        repository.fetchLowSlotModules(ship.lowSlots)
        repository.fetchMedSlotModules(ship.medSlots)
        repository.fetchHighSlotModules(ship.highSlots)

    }

    fun removeModule(module: Module) {
        // removes the effect of a module to ships stats
        val ship = _currentShip.value ?: return
        ship.removeModuleFromShipStats(module)
        _currentShip.postValue(ship)
    }

    fun applyModule(module: Module) {
        // applies the effect of a module to ship stats
        val ship = _currentShip.value ?: return
        ship.applyModuleToShipStats(module)
        _currentShip.postValue(ship)
    }

}