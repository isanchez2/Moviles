package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val executor = Executors.newSingleThreadExecutor()
    private var reference = FirebaseDatabase.getInstance().getReference("cards")
    private var auth: FirebaseAuth = Firebase.auth

    var card: Card? = null
    var cards: LiveData<List<Card>> = CardDatabase.getInstance(context).cardDao.getCards()
    var dueCard: LiveData<Card?> = Transformations.map(cards) {
        try {
            it.filter {
                it.isDue(LocalDateTime.now()) && it.userId == auth.currentUser!!.uid
            }.random()
        } catch (e: Exception) {
            null
        }
    }
    var nDueCards: LiveData<Int> = Transformations.map(cards) {
        it.filter {
            it.isDue(LocalDateTime.now()) && it.userId == auth.currentUser!!.uid
        }.size
    }
    var board: Boolean = SettingsActivity.getAvailabilityBoard(context)

    /*
    init {
        card = randomCard()
        _nDueCards.value = dueCards().size
    } */

    /* private fun dueCards() = cards.filter { card -> card.isDue(LocalDateTime.now()) }

    private fun randomCard() = try {
        dueCards().random()
    } catch (e: NoSuchElementException) {
        null
    } */

    fun update(quality: Int) {
        card?.quality = quality
        card?.numAnswers = card?.numAnswers?.plus(1)!!
        card?.update(LocalDateTime.now())
        /* card = randomCard() */
        executor.execute {
            CardDatabase.getInstance(context).cardDao.update(card!!)
        }
        if (SettingsActivity.getUploadData(context)) {
            reference.child(card!!.id).setValue(card)
        }
        /* _nDueCards.value = nDueCards.value?.minus(1) */
    }
}