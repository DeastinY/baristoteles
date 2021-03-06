package com.arsartificia.dev.baristoteles

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {
    lateinit var ma : MainActivity

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.settings_fragment, container, false)
        Util.registerCircularReveal(activity, view, arguments)
        return view
    }

    override fun onStart() {
        super.onStart()

        ma = activity as MainActivity
        grindSwitch.isChecked = ma.settings.grind
        grindSwitch.setOnClickListener{ ma.settings.grind = grindSwitch.isChecked }
        weightSwitch.isChecked = ma.settings.weight
        weightSwitch.setOnClickListener{ ma.settings.weight = weightSwitch.isChecked }
        temperatureSwitch.isChecked = ma.settings.temperature
        temperatureSwitch.setOnClickListener{ ma.settings.temperature = temperatureSwitch.isChecked }
        timeSwitch.isChecked = ma.settings.time
        timeSwitch.setOnClickListener { ma.settings.time = timeSwitch.isChecked }
        outweightSwitch.isChecked = ma.settings.outWeight
        outweightSwitch.setOnClickListener{ ma.settings.outWeight = grindSwitch.isChecked }
        notesSwitch.isChecked = ma.settings.notes
        notesSwitch.setOnClickListener{ ma.settings.notes = notesSwitch.isChecked }
    }
}