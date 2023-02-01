package es.uam.eps.dadm.cards.database

import androidx.lifecycle.LiveData
import androidx.room.*
import es.uam.eps.dadm.cards.Card
import es.uam.eps.dadm.cards.Deck

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks_table")
    fun getDecks(): LiveData<List<Deck>>

    @Query("SELECT * FROM decks_table WHERE deckId = :id")
    fun getDeck(id: String): LiveData<Deck?>

    @Query("SELECT * FROM cards_table WHERE deckId = :id")
    fun getDeckWithCards(id: String): LiveData<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeck(deck: Deck)

    @Update
    fun update(deck: Deck)

    @Delete
    fun delete(deck: Deck)

    @Query("DELETE FROM decks_table")
    fun deleteDecks()

    @Query("DELETE FROM cards_table WHERE deckId = :id")
    fun deleteDeckWithCards(id: String)

    @Query("SELECT count(*) FROM decks_table")
    fun getNumDecks(): LiveData<Int>
}