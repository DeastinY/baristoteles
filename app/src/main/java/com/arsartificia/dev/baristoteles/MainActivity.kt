package com.arsartificia.dev.baristoteles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream




class MainActivity : AppCompatActivity() {
    var name: String = ""
    var grind: String = ""
    var time: String = ""
    var weight: String = ""
    var weightOut: String = ""
    var note: String = ""
    var temperature: String = ""
    var rating: Float = 0.0f
    val filename = "baristoteles.data"
    var data = ArrayList<Entry>()
    lateinit var dataFragment: DataFragment
    lateinit var settings: Settings
    val PREFS_NAME = "BaristotelesPreferences"
    val PREFS_INVERT = "upgrade_invert"
    val PREFS_STEP_TIME = "step_time"
    val PREFS_STEP_WEIGHT = "step_weight"
    val PREFS_STEP_GRIND = "step_grind"
    val PREFS_STEP_NOTES = "step_notes"
    val PREFS_STEP_OUTWEIGHT = "step_weight_out"
    val PREFS_STEP_TEMPERATURE = "step_temperature"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val dataFragment = DataFragment()
        fragmentTransaction.add(R.id.fragment_container, dataFragment)
        fragmentTransaction.addToBackStack("DataFragment")
        fragmentTransaction.commit()

        loadData()
        loadSettings()
    }

    fun checkCreateSettings() {
        val s = getSharedPreferences(PREFS_NAME, 0)
        if (s.getBoolean(PREFS_INVERT, true)) {
            //the app is being launched for first time, do something
            data.reverse()
            Snackbar.make(root_layout, resources.getText(R.string.upgrade_invert), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            // record the fact that the app has been started at least once
            s.edit().putBoolean(PREFS_INVERT, false).apply()
        }
        if (s.getBoolean(PREFS_STEP_TIME, true)) {
            s.edit().putBoolean(PREFS_STEP_TIME, true).apply()
        }
        if (s.getBoolean(PREFS_STEP_WEIGHT, true)) {
            s.edit().putBoolean(PREFS_STEP_WEIGHT, true).apply()
        }
        if (s.getBoolean(PREFS_STEP_OUTWEIGHT, true)) {
            s.edit().putBoolean(PREFS_STEP_OUTWEIGHT, true).apply()
        }
        if (s.getBoolean(PREFS_STEP_GRIND, true)) {
            s.edit().putBoolean(PREFS_STEP_GRIND, true).apply()
        }
        if (s.getBoolean(PREFS_STEP_TEMPERATURE, true)) {
            s.edit().putBoolean(PREFS_STEP_TEMPERATURE, true).apply()
        }
        if (s.getBoolean(PREFS_STEP_NOTES, true)) {
            s.edit().putBoolean(PREFS_STEP_NOTES, true).apply()
        }
    }

    fun loadSettings() {
        val s = getSharedPreferences(PREFS_NAME, 0)
        settings = Settings(
                s.getBoolean(PREFS_STEP_TIME, true),
                s.getBoolean(PREFS_STEP_WEIGHT, true),
                s.getBoolean(PREFS_STEP_GRIND, true),
                s.getBoolean(PREFS_STEP_NOTES, true),
                s.getBoolean(PREFS_STEP_TEMPERATURE, true),
                s.getBoolean(PREFS_STEP_OUTWEIGHT, true))
    }

    override fun onStop() {
        super.onStop()
        writeData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                // TODO put your code here to respond to the button tap
                Util.transitionFragment(fragmentManager, SettingsFragment(), "SettingsFragment", root_layout, root_layout)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun addData() {
        data.add(0, Entry(name, grind, time, weight, weightOut, note, rating, temperature))
        resetTempData()
        dataFragment.adapter.notifyItemInserted(0)
        dataFragment.recyclerView.scrollToPosition(0)
    }

    fun replaceData(old: Entry, new: Entry) {
        val idx = data.indexOf(old)
        data[idx] = new
        dataFragment.adapter.notifyItemChanged(idx)
    }

    fun resetTempData() {
        name = ""
        grind = ""
        time = ""
        weight = ""
        note = ""
        temperature = ""
        weightOut = ""
        rating = 0.0f
    }

    fun loadData() {
        try {
            val fis = openFileInput(filename)
            val ois = ObjectInputStream(fis)
            @Suppress("UNCHECKED_CAST")
            data = ois.readObject() as ArrayList<Entry>
            ois.close()
        } catch (error: FileNotFoundException) {
            // This is okay. Happens on the first run
        } catch (error: Exception) {
            Snackbar.make(findViewById(R.id.root_layout), error.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun writeData() {
        val s = getSharedPreferences(PREFS_NAME, 0)
        s.edit().putBoolean(PREFS_STEP_TIME, settings.time)
                .putBoolean(PREFS_STEP_WEIGHT, settings.weight)
                .putBoolean(PREFS_STEP_GRIND, settings.grind)
                .putBoolean(PREFS_STEP_NOTES, settings.notes).apply()
        try {
            val fos = openFileOutput(filename, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(data)
            oos.close()
        } catch (error: Exception) {
            Snackbar.make(findViewById(R.id.root_layout), error.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onBackPressed() {
        val count = fragmentManager.backStackEntryCount
        if (count == 1) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            fragmentManager.popBackStack()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }
}
