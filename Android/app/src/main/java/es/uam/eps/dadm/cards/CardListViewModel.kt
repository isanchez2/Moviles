package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.uam.eps.dadm.cards.database.CardDatabase

class CardListViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val deckId = MutableLiveData<String>()

    val cards: LiveData<List<Card>> = CardDatabase.getInstance(context).cardDao.getCards()

    fun loadCardId(id: String) {
        deckId.value = id
    }
}