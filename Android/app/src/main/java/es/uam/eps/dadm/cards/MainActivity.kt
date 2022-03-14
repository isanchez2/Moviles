package es.uam.eps.dadm.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.cards.databinding.ActivityMainBinding
import timber.log.Timber

private const val ANSWERED_KEY = "es.uam.eps.dadm.cards:answered"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var card: Card = Card("Tree", "Árbol")
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = Player(this.lifecycle)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.card = card
        binding.apply {
            answerButton.setOnClickListener {
                card?.answered = true
                invalidateAll()
            }
        }
        Timber.i("onCreate called")
        Timber.i("answered = ${savedInstanceState?.getBoolean(ANSWERED_KEY)}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState called")
        outState.putBoolean(ANSWERED_KEY, card.answered)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState called")
        card.answered = savedInstanceState.getBoolean(ANSWERED_KEY)
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
        player.start()
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
        binding.invalidateAll()
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart called")
    }

}