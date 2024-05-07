package com.example.mobilefittingassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FittingActivity : AppCompatActivity() {

    private lateinit var calculatedShields: TextView
    private lateinit var calculatedArmor: TextView
    private lateinit var calculatedDps: TextView
    private lateinit var calculatedRepairs: TextView
    private lateinit var calculatedStability: TextView
    private lateinit var shipName: TextView

    private lateinit var viewModel: ActiveShipViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fitting_screen)

        viewModel = ViewModelProvider(this).get(ActiveShipViewModel::class.java)

        val ship = intent.getParcelableExtra<Ship>("CURRENT_SHIP") ?: return

        shipName = findViewById(R.id.ship_name)
        calculatedShields = findViewById(R.id.shields)
        calculatedArmor = findViewById(R.id.armor)
        calculatedDps = findViewById(R.id.dps)
        calculatedRepairs = findViewById(R.id.repair_rate)
        calculatedStability = findViewById(R.id.cap_stability)

        // stuff that happens as soon as we have a ship - get its name, put it into the view model
        // and set up an observer for any changes to the stats of the ship
        shipName.text = "${ship?.shipName}"
        viewModel.setShip(ship)
        viewModel.currentShip.observe(this) { updatedShip ->
            updateShipDetails(updatedShip)
        }

        setupRecyclerView(R.id.slots_1, viewModel.highSlotModules)
        setupRecyclerView(R.id.slots_2, viewModel.medSlotModules)
        setupRecyclerView(R.id.slots_3, viewModel.lowSlotModules)
    }

    private fun updateShipDetails(ship: Ship) {
        // update Ui elements for the current ship stats
            calculatedShields.text = "Current shields: ${ship.currentShields}"
            calculatedArmor.text = "Current armor: ${ship.currentArmor}"
            calculatedDps.text = "Current dps: ${ship.currentDps}"
            calculatedRepairs.text = "Current tank: ${ship.currentRepair}"
            calculatedStability.text = "Cap stability: ${ship.currentCapRegen}"
    }

    // since i have 3 recycler views at the same time i am going to make a new function that handles
    // their initializations
    private fun setupRecyclerView(recyclerViewId: Int, modulesLiveData: LiveData<List<Module>>) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        val adapter = ModuleAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        modulesLiveData.observe(this, Observer { modules ->
            adapter.setModules(modules)
        })
    }
}