package es.uam.eps.dadm.cards

import android.app.Application
import es.uam.eps.dadm.cards.database.CardDatabase
import timber.log.Timber
import java.util.concurrent.Executors

class CardsApplication: Application() {
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate() {
        super.onCreate()
        val cardDatabase = CardDatabase.getInstance(applicationContext)
        executor.execute {
            /*
            cardDatabase.deckDao.addDeck(Deck(1, "English"))
            cardDatabase.deckDao.addDeck(Deck(2, "French"))
            cardDatabase.cardDao.addCard(Card("To wake up", "Despertarse", deckId = 1))
            cardDatabase.cardDao.addCard(Card("To rule out", "Descartar", deckId = 1))
            cardDatabase.cardDao.addCard(Card("To turn down", "Rechazar", deckId = 1))
            cardDatabase.cardDao.addCard(Card("La voiture", "El coche", deckId = 2))
            cardDatabase.cardDao.addCard(Card("J'ai faim", "Tengo hambre", deckId = 2))
            */
        }
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        var cards: MutableList<Card> = mutableListOf()
        var decks: MutableList<Deck> = mutableListOf()
        lateinit var selectedDeckId: String

        fun getCard(id: String): Card? {
            return cards.find { it.id == id }
        }
        fun getDeck(id: String): Deck? {
            return decks.find { it.deckId == id }
        }
        fun addCard(card: Card) {
            cards.add(card)
        }
        fun addDeck(deck: Deck) {
            decks.add(deck)
        }
        fun deleteCard(card: Card) {
            cards.remove(card)
        }
        fun deleteDeck(deck: Deck) {
            decks.remove(deck)
        }
        fun getCardsFromDeck(id: String): List<Card> {
            return cards.filter { card -> card.deckId == id }
        }
    }
}