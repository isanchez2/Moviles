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
import es.uam.eps.dadm.cards.databinding.FragmentCardEditBinding
import java.util.concurrent.Executors

class CardEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    private var reference = FirebaseDatabase.getInstance().getReference("cards")
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentCardEditBinding
    lateinit var card: Card
    lateinit var question: String
    lateinit var answer: String
    var deckId: Long = 0
    private val viewModel by lazy {
        ViewModelProvider(this).get(CardEditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_card_edit,
            container,
            false)
        val args = CardEditFragmentArgs.fromBundle(requireArguments())

        viewModel.loadCardId(args.cardId)
        // Ajusta un observador a la propiedad card de CardEditViewModel
        viewModel.card.observe(viewLifecycleOwner) {
            card = it
            binding.card = card
            question = card.question
            answer = card.answer
        }

        /*
        if (SettingsActivity.getUploadData(requireContext())) {
            viewModelF.cards.observe(viewLifecycleOwner) {
                card = it.find { it.id == args.cardId }!!
                binding.card = card
                question = card.question
                answer = card.answer
                deckId = card.deckId
            }
        } */
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val questionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                card.question = s.toString()
            }
        }
        binding.editTextQuestion.addTextChangedListener(questionTextWatcher)

        val answerTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                card.answer = s.toString()
            }
        }
        binding.editTextAnswer.addTextChangedListener(answerTextWatcher)

        binding.cardEditAcceptButton.setOnClickListener {
            if (card.question == "" || card.answer == "") {
                Toast.makeText(context, R.string.card_edit_toast, Toast.LENGTH_SHORT).show()
            }
            else {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.cardDao.update(card)
                }
                if (SettingsActivity.getUploadData(requireContext()))
                    reference.child(card.id).setValue(card)
                card.createdBefore = true
                view?.findNavController()
                    ?.navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
            }
        }
        binding.cardEditCancelButton.setOnClickListener {
            if (!card.createdBefore) {
                if (SettingsActivity.getUploadData(requireContext()))
                    reference.child(card.id).removeValue()
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.cardDao.delete(card)
                }
            }
            view?.findNavController()
                ?.navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
        }
        binding.cardEditDeleteButton.setOnClickListener {
            val cardDatabase = CardDatabase.getInstance(requireContext())
            executor.execute {
                cardDatabase.cardDao.delete(card)
            }
            if (SettingsActivity.getUploadData(requireContext()))
                reference.child(card.id).removeValue()
            view?.findNavController()?.navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_card_edit, menu)
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
                    ?.navigate(CardEditFragmentDirections.actionCardEditFragmentToLoginFragment())
            }
        }
        return true
    }
}