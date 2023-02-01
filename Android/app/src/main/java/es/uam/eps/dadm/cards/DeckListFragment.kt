package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding
import java.util.concurrent.Executors

class DeckListFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    private var reference = FirebaseDatabase.getInstance().getReference("decks")
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter
    private val deckListViewModel by lazy {
        ViewModelProvider(this).get(DeckListViewModel::class.java)
    }
    var numDecks: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_deck_list,
            container,
            false)
        adapter = DeckAdapter()
        adapter.data = emptyList()
        binding.deckListRecyclerView.adapter = adapter

        deckListViewModel.numDecks.observe(viewLifecycleOwner) {
            numDecks = it
        }

        binding.newDeckFab.setOnClickListener {
            auth = Firebase.auth
            val deck = Deck("", userId = auth.currentUser!!.uid)
            val cardDatabase = CardDatabase.getInstance(requireContext())
            executor.execute {
                cardDatabase.deckDao.addDeck(deck)
            }
            if (SettingsActivity.getUploadData(requireContext())) {
                reference.child(deck.deckId).setValue(deck)
            }
            deck.createdBefore = false
            it.findNavController().navigate(DeckListFragmentDirections.actionDeckListFragmentToDeckEditFragment(deck.deckId))
        }
        deckListViewModel.decks.observe(viewLifecycleOwner) {
            auth = Firebase.auth
            adapter.data = it.filter { it.userId == auth.currentUser!!.uid }
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_deck_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth = Firebase.auth
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
            R.id.log_out -> {
                auth.signOut()
                view?.findNavController()
                    ?.navigate(DeckListFragmentDirections.actionDeckListFragmentToLoginFragment())
            }
            R.id.upload_data -> {
                val decksUpload = adapter.data.filter { it.userId == auth.currentUser?.uid }
                for (deck in decksUpload) {
                    reference.child(deck.deckId).removeValue()
                }
                for (deck in decksUpload) {
                    reference.child(deck.deckId).setValue(deck)
                }
                Snackbar.make(requireView(), R.string.firebase_uploading, Snackbar.LENGTH_SHORT).show()
            }
            R.id.download_data -> {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.deckDao.deleteDecks()
                }
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val listOfDecks = mutableListOf<Deck>()
                        for (child in snapshot.children) {
                            child.getValue(Deck::class.java)?.let {
                                listOfDecks.add(it)
                            }
                        }
                        for (deck in listOfDecks) {
                            executor.execute {
                                try {
                                    cardDatabase.deckDao.addDeck(deck)
                                } catch (e: Error) {

                                }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
                Snackbar.make(requireView(), R.string.firebase_downloading, Snackbar.LENGTH_SHORT).show()
            }
        }
        return true
    }
}