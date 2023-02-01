package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.databinding.FragmentCardDetailsBinding
import java.util.concurrent.Executors

class CardDetailsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentCardDetailsBinding
    lateinit var card: Card
    lateinit var question: String
    lateinit var answer: String
    lateinit var deckId: String
    private val cardDetailsViewModel by lazy {
        ViewModelProvider(this).get(CardDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_card_details,
            container,
            false)
        val args = CardDetailsFragmentArgs.fromBundle(requireArguments())
        cardDetailsViewModel.loadCardId(args.cardId)
        cardDetailsViewModel.card.observe(viewLifecycleOwner) {
            card = it
            binding.card = card
            question = card.question
            answer = card.answer
            deckId = card.deckId
        }
        /* cardDetailsViewModel.cards.observe(viewLifecycleOwner) {
            card = it.find { it.id == args.cardId }!!
            binding.card = card
            question = card.question
            answer = card.answer
            deckId = card.deckId
        } */
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_card_details, menu)
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
                    ?.navigate(CardDetailsFragmentDirections.actionCardDetailsFragmentToLoginFragment())
            }
        }
        return true
    }
}