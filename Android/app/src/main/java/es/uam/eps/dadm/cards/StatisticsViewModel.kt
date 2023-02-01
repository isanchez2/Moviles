package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.uam.eps.dadm.cards.database.CardDatabase

class StatisticsViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private var _cards = CardDatabase.getInstance(context).cardDao.getCards()
    val cards: LiveData<List<Card>>
        get() = _cards
    /* private var cards: MutableList<Card> = CardsApplication.cards */
    private var _numCards = CardDatabase.getInstance(context).cardDao.getNumCards()
    val numCards: LiveData<Int>
        get() = _numCards

    private var _decks = CardDatabase.getInstance(context).deckDao.getDecks()
    val decks: LiveData<List<Deck>>
        get() = _decks
    /* private var decks: MutableList<Deck> = CardsApplication.decks */
    private var _numDecks = CardDatabase.getInstance(context).deckDao.getNumDecks()
    val numDecks: LiveData<Int>
        get() = _numDecks

    private val _numMedEasiness = MutableLiveData<Double>()
    val numMedEasiness: LiveData<Double>
        get() = _numMedEasiness

    private val _numMedInterval = MutableLiveData<Long>()
    val numMedInterval: LiveData<Long>
        get() = _numMedInterval

    private val _numReviews = MutableLiveData<Int>()
    val numReviews: LiveData<Int>
        get() = _numReviews

    private val _numDifferentCardsAnswered = MutableLiveData<Int>()
    val numDifferentCardsAnswered: LiveData<Int>
        get() = _numDifferentCardsAnswered

    private val _numDifficultCards = MutableLiveData<Int>()
    val numDifficultCards: LiveData<Int>
        get() = _numDifficultCards

    private val _numDoubtCards = MutableLiveData<Int>()
    val numDoubtCards: LiveData<Int>
        get() = _numDoubtCards

    private val _numEasyCards = MutableLiveData<Int>()
    val numEasyCards: LiveData<Int>
        get() = _numEasyCards

    init {
        /* _numCards.value = cards.value?.size
        _numDecks.value = decks.value?.size
        _numMedEasiness.value = easinessMed()
        _numMedInterval.value = intervalMed()
        _numReviews.value = totalReviews()
        _numDifferentCardsAnswered.value = totalDifferentAnsweredCards()
        _numDifficultCards.value = difficultCards()
        _numDoubtCards.value = doubtCards()
        _numEasyCards.value = easyCards() */
    }
/*
    private fun easinessMed(): Double {
        var totalEasiness = 0.0
        for (card in cards.value!!) {
            totalEasiness += card.easiness
        }
        return totalEasiness / _numCards.value!!
    }

    private fun intervalMed(): Long {
        var totalInterval: Long = 0
        for (card in cards.value!!) {
            totalInterval += card.interval
        }
        return totalInterval / _numCards.value!!
    }

    private fun totalReviews(): Int {
        var answers = 0
        for (card in cards.value!!) {
            answers += card.numAnswers
        }
        return answers
    }

    private fun totalDifferentAnsweredCards(): Int {
        var answeredCards = 0
        for (card in cards.value!!) {
            if (card.answered) {
                answeredCards += 1
            }
        }
        return answeredCards
    }

    private fun difficultCards(): Int {
        var numDifficultCards = 0
        for (card in cards.value!!) {
            if (card.quality == 0) numDifficultCards++
        }
        return numDifficultCards
    }

    private fun doubtCards(): Int {
        var numDoubtCards = 0
        for (card in cards.value!!) {
            if (card.quality == 3) numDoubtCards++
        }
        return numDoubtCards
    }

    private fun easyCards(): Int {
        var numEasyCards = 0
        for (card in cards.value!!) {
            if (card.quality == 5) numEasyCards++
        }
        return numEasyCards
    }*/

}