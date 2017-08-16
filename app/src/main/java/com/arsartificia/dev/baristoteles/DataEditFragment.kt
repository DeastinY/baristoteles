package com.arsartificia.dev.baristoteles

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dataedit_fragment.*

class DataEditFragment() : Fragment() {

    lateinit var entry: Entry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        entry = arguments.getSerializable("extra") as Entry
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.dataedit_fragment, container, false)
        Util.registerCircularReveal(activity, view, arguments)
        return view
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, view, fragmentManager, false, true)

        nameEditText.setText(entry.name)
        ratingBar.rating = entry.rating
        weightTextView.setText(entry.weight)
        timeTextView.setText(entry.time)
        grindTextView.setText(entry.grind)
        noteEditText.setText(entry.note)
        buttonSave.setOnClickListener {
                val ma : MainActivity = activity as MainActivity
                ma.replaceData(entry, Entry(
                        nameEditText.text.toString(),
                        grindTextView.text.toString(),
                        timeTextView.text.toString(),
                        weightTextView.text.toString(),
                        noteEditText.text.toString(),
                        ratingBar.rating
                ))
                Util.hideKeyboard(activity, view)
                fragmentManager.popBackStack("DataFragment", 0)
            }
    }
}