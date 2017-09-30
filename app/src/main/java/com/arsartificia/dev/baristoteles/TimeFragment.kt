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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.number_fragment.view.*


class TimeFragment : Fragment() {

    var timerThread : Thread? = null
    lateinit var buttonTimer : ImageButton

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

        view.infoTextView.text = getString(R.string.time)
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
        view.linearLayoutLastRow.removeView(view.buttonDot)
        // Add Timer Button, Remove Dot Button
        buttonTimer = ImageButton(activity)
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)
        params.weight = 1f
        buttonTimer.layoutParams = params
        buttonTimer.setImageDrawable(resources.getDrawable(R.drawable.ic_timer_black_24dp))
        buttonTimer.scaleType = ImageView.ScaleType.FIT_CENTER
        buttonTimer.background = null
        buttonTimer.setOnClickListener {
            if (timerThread != null) {
                stopThread()
            } else {
                buttonTimer.setImageDrawable(resources.getDrawable(R.drawable.ic_timer_off_black_24dp))
                timerThread = object : Thread() {

                    override fun run() {
                        try {
                            while (!isInterrupted) {
                                Thread.sleep(1000)
                                activity.runOnUiThread({
                                    timerIncrementText(view)
                                })
                            }
                        } catch (e: InterruptedException) {
                        }

                    }
                }
                timerThread!!.start()
            }
        }
        view.linearLayoutLastRow.addView(buttonTimer, 0)
        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                if (view.editText.text.isNotEmpty()) {
                    val time = view.editText.text.toString()
                    ma.time = time
                }
                stopThread()
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

    override fun onStop() {
        super.onStop()
        stopThread()
    }

    private fun stopThread() {
        buttonTimer.setImageDrawable(null)
        buttonTimer.setImageDrawable(resources.getDrawable(R.drawable.ic_timer_black_24dp))
        if (timerThread != null) {
            timerThread!!.interrupt()
            timerThread = null
        }
    }

    private fun clearText(text: String) : String {
        return text.replace("s","").replace("m","").replace(":","").replace(" ", "")
    }

    private fun extendText(text: String) : String {
        var ntext = text
        when (text.length) {
            1, 2 -> ntext = text.plus(" s")
            3 -> ntext = text[0].plus(" m ").plus(text.substring(1, 3)).plus(" s")
            4 -> ntext = text.substring(0,2).plus(" m : ").plus(text.substring(2, 4)).plus(" s")
        }
        return ntext
    }

    private fun changeEditText(dialogView: View, exec: (String) -> String) {
        stopThread()
        var text = clearText(dialogView.editText.text.toString())
        text = clearText(text)
        text = exec(text)
        if (text.length > 4) {
            text = text.dropLast(1)
        }
        dialogView.editText.text = extendText(text)
    }

    private fun timerIncrementText(dialogView: View) {
        var text = clearText(dialogView.editText.text.toString())
        text = clearText(text)
        if (text.isEmpty()) { text = "0" }
        text = text.toInt().inc().toString()
        if (text.length > 4) {
            text = text.dropLast(1)
        }
        dialogView.editText.text = extendText(text)
    }

}
