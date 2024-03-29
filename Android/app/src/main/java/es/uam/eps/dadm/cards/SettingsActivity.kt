package es.uam.eps.dadm.cards

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val MAX_NUMBER_CARDS_KEY = "max_number_cards"
        const val MAX_NUMBER_CARDS_DEFAULT = "20"
        const val AVAILABILITY_BOARD_KEY = "board"
        const val AVAILABILITY_BOARD_DEFAULT = false
        const val SYNC_DATA_KEY = "firebase"
        const val SYNC_DATA_DEFAULT = false
        const val LOGGED_IN_KEY = "logged_in_key"

        fun getMaximumNumberOfCards(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MAX_NUMBER_CARDS_KEY, MAX_NUMBER_CARDS_DEFAULT)
        }

        fun getAvailabilityBoard(context: Context): Boolean {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(AVAILABILITY_BOARD_KEY, AVAILABILITY_BOARD_DEFAULT)
        }

        fun getUploadData(context: Context): Boolean {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(SYNC_DATA_KEY, SYNC_DATA_DEFAULT)
        }

        fun setLoggedIn(context: Context, loggedin: Boolean) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(LOGGED_IN_KEY, loggedin)
            editor.apply()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}