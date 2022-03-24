package es.uam.eps.dadm.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.cards.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var listener = View.OnClickListener { v ->
        // Asigna a quality el valor 0, 3 o 5,
        // dependiendo del botón que se haya pulsado
        val quality = when (v?.id) {
            R.id.difficult_button -> 0
            R.id.doubt_button -> 3
            R.id.easy_button -> 5
            else -> -1
        }
        // Llama al método update de viewModel
        viewModel.update(quality)
        Timber.i("next fecha: ${viewModel.card?.nextPracticeDate}")

        // Si la propiedad card de viewModel es null
        // informa al usuario mediante un Toast de que
        // no quedan tarjetas que repasar
        if (viewModel.card == null) {
            Toast.makeText(this, R.string.empty_cards, Toast.LENGTH_LONG).show()
        }
        binding.invalidateAll()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        //binding.card = viewModel.card
        Timber.i("onCreate called")

        binding.answerButton.setOnClickListener {
            viewModel.card?.answered = true
            binding.invalidateAll()
        }

        // Ajusta el escuchador listener a los botones de dificultad
        binding.easyButton.setOnClickListener(this.listener)
        binding.doubtButton.setOnClickListener(this.listener)
        binding.difficultButton.setOnClickListener(this.listener)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
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