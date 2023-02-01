package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeckEditFirebaseViewModel : ViewModel()  {
    private var _decks = MutableLiveData<List<Deck>>()
    val decks: LiveData<List<Deck>>
        get() = _decks
    private var reference = FirebaseDatabase.getInstance().getReference("decks")

    init {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfDecks = mutableListOf<Deck>()
                for (child in snapshot.children) {
                    child.getValue(Deck::class.java)?.let {
                        listOfDecks.add(it)
                    }
                }
                _decks.value = listOfDecks
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}