package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentCardListBinding
import java.util.concurrent.Executors

class CardListFragment: Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    private var reference = FirebaseDatabase.getInstance().getReference("cards")
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardAdapter
    lateinit var deck: Deck
    private val cardListViewModel by lazy {
        ViewModelProvider(this).get(CardListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_card_list,
            container,
            false)
        val args = CardListFragmentArgs.fromBundle(requireArguments())
        adapter = CardAdapter()
        adapter.data = emptyList()
        binding.cardListRecyclerView.adapter = adapter

        cardListViewModel.loadCardId(args.deckId)

        binding.newCardFab.setOnClickListener {
            auth = Firebase.auth
            val card = Card("", "", args.deckId, auth.currentUser!!.uid)
            val cardDatabase = CardDatabase.getInstance(requireContext())
            executor.execute {
                cardDatabase.cardDao.addCard(card)
            }
            if (SettingsActivity.getUploadData(requireContext())) {
                reference.child(card.id).setValue(card)
            }
            card.createdBefore = false
            it.findNavController().navigate(CardListFragmentDirections.actionCardListFragmentToCardEditFragment(card.id))
        }


        cardListViewModel.cards.observe(viewLifecycleOwner) {
            auth = Firebase.auth
            adapter.data = it.filter { it.deckId == args.deckId && it.userId == auth.currentUser!!.uid }
            adapter.notifyDataSetChanged()
        }

        SettingsActivity.setLoggedIn(requireContext(), true)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_card_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
            R.id.log_out -> {
                auth = Firebase.auth
                auth.signOut()
                view?.findNavController()
                    ?.navigate(CardListFragmentDirections.actionCardListFragmentToLoginFragment())
            }
            R.id.upload_data -> {
                auth = Firebase.auth
                val cardsUpload = adapter.data.filter { it.userId == auth.currentUser?.uid }
                for (card in cardsUpload) {
                    reference.child(card.id).removeValue()
                }
                for (card in cardsUpload) {
                    reference.child(card.id).setValue(card)
                }
                Snackbar.make(requireView(), R.string.firebase_uploading, Snackbar.LENGTH_SHORT).show()
            }
            R.id.download_data -> {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.cardDao.deleteCards()
                }
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val listOfCards = mutableListOf<Card>()
                        for (child in snapshot.children) {
                            child.getValue(Card::class.java)?.let {
                                listOfCards.add(it)
                            }
                        }
                        for (card in listOfCards) {
                            executor.execute {
                                try {
                                    cardDatabase.cardDao.addCard(card)
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