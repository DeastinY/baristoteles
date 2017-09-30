package com.arsartificia.dev.baristoteles

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.number_fragment.view.*

class TemperatureFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.number_fragment, container, false)
        Util.registerCircularReveal(activity, view, arguments)
        return view
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, view, fragmentManager, true, true)

        view.touchables.filterIsInstance<Button>().forEach { TextViewCompat.setAutoSizeTextTypeWithDefaults(it, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM) }

        view.infoTextView.text = getString(R.string.temperature)
        view.buttonNine.setOnClickListener { _ -> changeEditText(view, { it.plus("9") }) }
        view.buttonEight.setOnClickListener { _ -> changeEditText(view, { it.plus("8") }) }
        view.buttonSeven.setOnClickListener { _ -> changeEditText(view, { it.plus("7") }) }
        view.buttonSix.setOnClickListener { _ -> changeEditText(view, { it.plus("6") }) }
        view.buttonFive.setOnClickListener { _ -> changeEditText(view, { it.plus("5") }) }
        view.buttonFour.setOnClickListener { _ -> changeEditText(view, { it.plus("4") }) }
        view.buttonThree.setOnClickListener { _ -> changeEditText(view, { it.plus("3") }) }
        view.buttonTwo.setOnClickListener { _ -> changeEditText(view, { it.plus("2") }) }
        view.buttonOne.setOnClickListener { _ -> changeEditText(view, { it.plus("1") }) }
        view.buttonZero.setOnClickListener { _ -> changeEditText(view, { it.plus("0") }) }
        view.buttonDelete.setOnClickListener { _ -> changeEditText(view, { it.dropLast(1) }) }
        view.buttonDelete.setImageDrawable(resources.getDrawable(R.drawable.ic_backspace_black_24dp))
        view.buttonDelete.scaleType = ImageView.ScaleType.FIT_CENTER
        view.buttonDelete.background = null
        view.buttonDot.setOnClickListener { _ -> changeEditText(view, { txt ->
            if (!txt.contains('.')) {
                txt.plus(".")
            }
            else {
                txt
            }
        }) }
        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                if (view.editText.text.isNotEmpty()) {
                    ma.temperature = view.editText.text.toString()
                }
                Util.nextFragment(this, ma.settings, fragmentManager, view.buttonNext, view)
            } catch (error: NumberFormatException) {
                Snackbar.make(view, "Please enter a proper number", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

    private fun changeEditText(dialogView: View, exec: (String) -> String) {
        val edit_txt = dialogView.editText
        var txt = edit_txt.text.toString()
        if (txt.contains("°C")) {
            txt = txt.dropLast(3)
        }
        txt = exec(txt)
        if (txt.length > 6) {
            txt = txt.dropLast(1)
        }
        if (txt.isNotEmpty()) {
            txt = txt.plus(" °C")
        }
        edit_txt.text = txt
    }

}