package es.uam.eps.dadm.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener {
            val email = binding.emailTextName.text.toString()
            val password = binding.passwordTextName.text.toString()
            signIn(email, password)
        }
        binding.createAccountButton.setOnClickListener {
            val email = binding.emailTextName.text.toString()
            val password = binding.passwordTextName.text.toString()
            createAccount(email, password)
        }
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            view?.findNavController()
                ?.navigate(LoginFragmentDirections.actionLoginFragmentToDeckListFragment())
        }
    }

    private fun signIn(email: String, password: String) {
        if (email == "" || password == "") {
            Toast.makeText(context, R.string.login_failed_empty, Toast.LENGTH_SHORT).show()
        }
        else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        view?.findNavController()
                            ?.navigate(LoginFragmentDirections.actionLoginFragmentToDeckListFragment())
                    } else {
                        Toast.makeText(context, R.string.login_failed_error, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun createAccount(email: String, password: String) {
        if (email == "" || password == "") {
            Toast.makeText(context, R.string.login_failed_empty, Toast.LENGTH_SHORT).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        view?.findNavController()
                            ?.navigate(LoginFragmentDirections.actionLoginFragmentToDeckListFragment())
                    } else {
                        Toast.makeText(context, R.string.login_failed_error, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}