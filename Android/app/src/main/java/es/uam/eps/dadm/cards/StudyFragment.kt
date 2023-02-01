package es.uam.eps.dadm.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.databinding.FragmentStudyBinding

class StudyFragment: Fragment() {
    lateinit var binding: FragmentStudyBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: StudyViewModel by lazy {
        ViewModelProvider(this).get(StudyViewModel::class.java)
    }
    private var listener = View.OnClickListener { v ->
        val quality = when (v?.id) {
            binding.difficultButton.id -> 0
            binding.doubtButton.id -> 3
            binding.easyButton.id -> 5
            else -> -1
        }
        // Llama al método update de viewModel
        binding.viewModel?.update(quality)


        // Si la propiedad card de viewModel es null
        // informa al usuario mediante un Toast de que
        // no quedan tarjetas que repasar
        if (binding.viewModel?.card == null) {
            Toast.makeText(activity, R.string.empty_cards, Toast.LENGTH_LONG).show()
        }
        binding.invalidateAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_study,
            container,
            false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Asigna un observador a dueCard
        viewModel.dueCard.observe(viewLifecycleOwner) {
            viewModel.card = it
            binding.invalidateAll()
        }
        binding.answerButton.setOnClickListener {
            viewModel.card?.answered = true
            binding.invalidateAll()
        }
        // Ajusta el escuchador listener a los botones de dificultad
        binding.easyButton.setOnClickListener(this.listener)
        binding.doubtButton.setOnClickListener(this.listener)
        binding.difficultButton.setOnClickListener(this.listener)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_study, menu)
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
                    ?.navigate(StudyFragmentDirections.actionStudyFragmentToLoginFragment())
            }
        }
        return true
    }
}