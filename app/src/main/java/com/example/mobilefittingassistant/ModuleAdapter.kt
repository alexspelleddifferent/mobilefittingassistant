package com.example.mobilefittingassistant

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

class ModuleAdapter(private val viewModel: ActiveShipViewModel) : RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> (){

    private var modules: List<Module> = listOf()

    fun setModules(modules: List<Module>) {
        this.modules = modules
        notifyDataSetChanged()
    }

    class ModuleViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        private val moduleName: TextView = itemView.findViewById(R.id.module_name)
        private val stat1: TextView = itemView.findViewById(R.id.stat_1)
        private val stat2: TextView = itemView.findViewById(R.id.stat_2)
        private val activateMod: CheckBox = itemView.findViewById(R.id.activate)

        fun bind(module: Module, viewModel: ActiveShipViewModel) {
            moduleName.text = module.module_name
            // i want the stats line to be dependant on how many effects a module has, this was solution i came up with
            // make a list of lists out of the map of effects
            var effectsList = module.effects.map { (key, value) -> listOf(key, value) }
            // transform the stats field into a list
            var statsList = listOf(stat1, stat2)
            // iterate over list of effects to fill out the relevant view ids
            // currently there are equal or less effects than there are stats fields, but this will need
            // to be changed if the app were made to be bigger
            for (i in effectsList.indices) {
                statsList[i].text = "${effectsList[i][0]}: ${effectsList[i][1]}"
            }

            activateMod.isChecked = false

            // resetting just like in lab
            activateMod.setOnCheckedChangeListener(null)
            activateMod.setOnCheckedChangeListener { checkbox, isChecked ->
                if (isChecked) {
                    viewModel.applyModule(module)
                } else {
                    viewModel.removeModule(module)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.module, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        holder.bind(modules[position], viewModel)
    }

    override fun getItemCount() = modules.size
}