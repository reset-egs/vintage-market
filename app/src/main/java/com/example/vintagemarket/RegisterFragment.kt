package com.example.vintagemarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.vintagemarket.R

class RegistrationFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Register"

        val view = inflater.inflate(R.layout.fragment_register, container, false)
        auth = FirebaseAuth.getInstance()

        val emailEditText = view.findViewById<EditText>(R.id.email_edit_text)
        val passwordEditText = view.findViewById<EditText>(R.id.password_edit_text)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a valid email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val navController = findNavController()
                        navController.navigate(R.id.action_registrationFragment_to_itemListFragment)
                    }
                }
                .addOnFailureListener(requireActivity()) { exception ->
                    Toast.makeText(requireContext(), "Registration failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}
