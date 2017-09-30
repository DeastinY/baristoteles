package com.arsartificia.dev.baristoteles

import AnimationUtils
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.text_fragment.view.*
import java.io.Serializable

object Util {
    fun registerCircularReveal(context: Context, view: View, arguments: Bundle) {
        AnimationUtils.registerCreateShareLinkCircularRevealAnimation(
                context,
                view,
                arguments.getInt("centerX"),
                arguments.getInt("centerY"),
                arguments.getInt("width"),
                arguments.getInt("height"))
    }

    fun  initializeFragment(activity: Activity, view: View, fragmentManager: FragmentManager, backIcon: Boolean, hideInput: Boolean) {
        try {
            val imageView: ImageView = view.findViewById(R.id.closeDialogImg)
            imageView.setOnClickListener({
                fragmentManager.popBackStack()
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            })
            if (backIcon) {
                imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp)
            } else {
                imageView.setImageResource(R.drawable.ic_close_black_24dp)
            }
        } catch ( e: IllegalStateException )
        {
            // If the layout does not provide a closeDialogImg we don't care
        }

        if (hideInput) {
            hideKeyboard(activity, view)
        } else {
            showKeyboard(500, view, activity)
        }
    }

    fun showKeyboard(delay: Long, view:View, context: Context) {
        val handler = Handler()
        handler.postDelayed({
            view.mainEditText.requestFocus()
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view.mainEditText, InputMethodManager.SHOW_IMPLICIT)
        }, delay)
    }

    fun hideKeyboard(activity: Activity, view:View ) {
        //Hide Keyboard
        view.postDelayed({
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }, 50)
    }

    fun nextFragment(current: Fragment, settings: Settings, fragmentManager: FragmentManager, animationCenter: View, view: View) {
        if (current is NameFragment) {
            if (settings.grind) {
                transitionFragment(fragmentManager, GrindFragment(), "Grind", animationCenter, view)
            } else if (settings.weight) {
                transitionFragment(fragmentManager, WeightFragment(), "Weight", animationCenter, view)
            } else if (settings.temperature) {
                transitionFragment(fragmentManager, TemperatureFragment(), "Temperature", animationCenter, view)
            } else if (settings.time) {
                transitionFragment(fragmentManager, TimeFragment(), "Time", animationCenter, view)
            } else if (settings.outWeight) {
                transitionFragment(fragmentManager, WeightOutFragment(), "WeightOut", animationCenter, view)
            } else if (settings.notes) {
                transitionFragment(fragmentManager, NoteFragment(), "Notes", animationCenter, view)
            } else {
                transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
            }
        } else if (current is GrindFragment) {
            if (settings.weight) {
                transitionFragment(fragmentManager, WeightFragment(), "Weight", animationCenter, view)
            } else if (settings.temperature) {
                transitionFragment(fragmentManager, TemperatureFragment(), "Temperature", animationCenter, view)
            } else if (settings.time) {
                transitionFragment(fragmentManager, TimeFragment(), "Time", animationCenter, view)
            } else if (settings.outWeight) {
                transitionFragment(fragmentManager, WeightOutFragment(), "WeightOut", animationCenter, view)
            } else if (settings.notes) {
                transitionFragment(fragmentManager, NoteFragment(), "Notes", animationCenter, view)
            } else {
                transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
            }
        } else if (current is WeightFragment) {
            if (settings.temperature) {
                transitionFragment(fragmentManager, TemperatureFragment(), "Temperature", animationCenter, view)
            } else if (settings.time) {
                transitionFragment(fragmentManager, TimeFragment(), "Time", animationCenter, view)
            } else if (settings.outWeight) {
                transitionFragment(fragmentManager, WeightOutFragment(), "WeightOut", animationCenter, view)
            } else if (settings.notes) {
                transitionFragment(fragmentManager, NoteFragment(), "Notes", animationCenter, view)
            } else {
                transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
            }
        } else if (current is TemperatureFragment) {
            if (settings.time) {
                transitionFragment(fragmentManager, TimeFragment(), "Time", animationCenter, view)
            } else if (settings.outWeight) {
                transitionFragment(fragmentManager, WeightOutFragment(), "WeightOut", animationCenter, view)
            } else if (settings.notes) {
                transitionFragment(fragmentManager, NoteFragment(), "Notes", animationCenter, view)
            } else {
                transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
            }
        } else if (current is TimeFragment) {
            if (settings.outWeight) {
                transitionFragment(fragmentManager, WeightOutFragment(), "WeightOut", animationCenter, view)
            } else if (settings.notes) {
                transitionFragment(fragmentManager, NoteFragment(), "Notes", animationCenter, view)
            } else {
                transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
            }
        } else if (current is WeightOutFragment) {
            if (settings.notes) {
                transitionFragment(fragmentManager, NoteFragment(), "Notes", animationCenter, view)
            } else {
                transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
            }
        } else {
            transitionFragment(fragmentManager, RatingFragment(), "Rating", animationCenter, view)
        }
    }

    fun transitionFragment(fragmentManager: FragmentManager, newFragment: Fragment, name: String, animationCenter: View, view: View, bundleArg : Serializable = "No Additional Arguments") {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val args = Bundle()
        args.putInt("centerX", (animationCenter.x+animationCenter.width/2).toInt())
        args.putInt("centerY", (animationCenter.y+animationCenter.height/2).toInt())
        args.putInt("width", view.width)
        args.putInt("height", view.height)
        args.putSerializable("extra", bundleArg)
        newFragment.arguments = args
        fragmentTransaction.add(R.id.fragment_container, newFragment)
        fragmentTransaction.addToBackStack(name)
        fragmentTransaction.commit()
    }
}