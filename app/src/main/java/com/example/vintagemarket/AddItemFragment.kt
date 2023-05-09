package com.example.vintagemarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vintagemarket.databinding.FragmentAddItemBinding
import com.example.vintagemarket.models.Item
import com.example.vintagemarket.models.ItemViewModel
import com.google.firebase.auth.FirebaseAuth

class AddItemFragment : Fragment() {

    private lateinit var viewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddItemBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Add an item"

        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        binding.buttonAddItem.setOnClickListener {
            val description = binding.editTextDescription.text.toString()
            val price = binding.editTextPrice.text.toString().toIntOrNull() ?: 0
            val sellerPhone = binding.editTextPhone.text.toString()
            val sellerEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

            // Validation logic here
            if (description.isNotEmpty() && price > 0 && sellerEmail.isNotEmpty() &&
                sellerPhone.isNotEmpty()) {
                // Add item logic here

                val newItem = Item(1, description, price, sellerEmail, sellerPhone)
                viewModel.add(newItem)

                // Clear the form fields
                binding.editTextDescription.setText("")
                binding.editTextPrice.setText("")
                binding.editTextPhone.setText("")

                // Navigate back to the item list fragment
                Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
