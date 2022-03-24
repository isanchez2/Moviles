package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class MainViewModel : ViewModel() {
    var card: Card? = null
    private var cards: MutableList<Card> = CardsApplication.cards
    private val _nDueCards = MutableLiveData<Int>()
    val nDueCards: LiveData<Int>
        get() = _nDueCards

    init {
        card = randomCard()
        _nDueCards.value = dueCards().size
    }

    private fun dueCards() = cards.filter { card -> card.isDue(LocalDateTime.now()) }

    private fun randomCard() = try {
        dueCards().random()
    } catch (e: NoSuchElementException) {
        null
    }

    fun update(quality: Int) {
        card?.quality = quality
        card?.update(LocalDateTime.now())
        card = randomCard()
        _nDueCards.value = nDueCards.value?.minus(1)
    }
}