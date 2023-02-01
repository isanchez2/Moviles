package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.databinding.FragmentTitleBinding

class TitleFragment: Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false)

        auth = Firebase.auth
        binding.cardsTitleTextView.setOnClickListener { view ->
            /*
            if (CardsApplication.numberOfDueCards() > 0)
                view.findNavController()
                    .navigate(R.id.action_titleFragment_to_deckListFragment)
            else
                Toast.makeText(requireActivity(), R.string.empty_cards, Toast.LENGTH_LONG).show() */
            if (auth.currentUser != null) {
                view.findNavController()
                    .navigate(TitleFragmentDirections.actionTitleFragmentToDeckListFragment())
            }
            else {
                view.findNavController()
                    .navigate(TitleFragmentDirections.actionTitleFragmentToLoginFragment())
            }
        }
        binding.tutorialFab.setOnClickListener {
            view?.findNavController()
                ?.navigate(TitleFragmentDirections.actionTitleFragmentToTutorialFragment())
        }
        return binding.root
    }
}