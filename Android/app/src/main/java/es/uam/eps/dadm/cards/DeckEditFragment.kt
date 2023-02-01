package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckEditBinding
import java.util.concurrent.Executors

class DeckEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    private var reference = FirebaseDatabase.getInstance().getReference("decks")
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentDeckEditBinding
    lateinit var deck: Deck
    lateinit var name: String

    private val deckEditViewModel by lazy {
        ViewModelProvider(this).get(DeckEditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_deck_edit,
            container,
            false)
        val args = DeckEditFragmentArgs.fromBundle(requireArguments())
        deckEditViewModel.loadDeckId(args.deckId)
        deckEditViewModel.deck.observe(viewLifecycleOwner) {
            deck = it
            binding.deck = deck
            name = deck.name
        }
        /*
        deckEditViewModel.decks.observe(viewLifecycleOwner) {
            deck = it.find { it.deckId == args.deckId }!!
            binding.deck = deck
            name = deck.name
        } */
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val nameTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deck.name = s.toString()
            }
        }
        binding.editTextName.addTextChangedListener(nameTextWatcher)

        binding.deckEditAcceptButton.setOnClickListener {
            if (deck.name == "") {
                Toast.makeText(context, R.string.deck_edit_toast, Toast.LENGTH_SHORT).show()
            }
            else {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.deckDao.update(deck)
                }
                if (SettingsActivity.getUploadData(requireContext()))
                    reference.child(deck.deckId).setValue(deck)
                deck.createdBefore = true
                view?.findNavController()
                    ?.navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
            }
        }

        binding.deckEditCancelButton.setOnClickListener {
            if (!deck.createdBefore) {
                if (SettingsActivity.getUploadData(requireContext()))
                    reference.child(deck.deckId).removeValue()
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.deckDao.delete(deck)
                }
            }
            else {
                deck.name = name
            }
            view?.findNavController()
                ?.navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
        }

        binding.deckEditDeleteButton.setOnClickListener {
            val cardDatabase = CardDatabase.getInstance(requireContext())
            executor.execute {
                cardDatabase.deckDao.deleteDeckWithCards(deck.deckId)
                cardDatabase.deckDao.delete(deck)
            }
            if (SettingsActivity.getUploadData(requireContext()))
                reference.child(deck.deckId).removeValue()
            view?.findNavController()
                ?.navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_deck_edit, menu)
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
                    ?.navigate(DeckEditFragmentDirections.actionDeckEditFragmentToLoginFragment())
            }
        }
        return true
    }

}