package com.example.vintagemarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vintagemarket.databinding.FragmentItemDetailBinding
import com.example.vintagemarket.models.ItemViewModel
import com.example.vintagemarket.repository.ItemRepository
import com.google.firebase.auth.FirebaseAuth

class ItemDetailFragment : Fragment() {

    private lateinit var binding: FragmentItemDetailBinding
    private val itemViewModel: ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        val bundle = requireArguments()
        val itemDetailFragmentArgs = ItemDetailFragmentArgs.fromBundle(bundle)
        val id = itemDetailFragmentArgs.id
        val selectedItem = itemViewModel[id]

        if (selectedItem != null) {
            binding.itemDetailTitle.text = selectedItem.description
            binding.itemDetailPrice.text = selectedItem.price.toString() + " kr."
            binding.itemDetailEmail.text = selectedItem.sellerEmail
            binding.itemDetailPhone.text = selectedItem.sellerPhone
        } else {
            binding.itemDetailTitle.text = "Item not found"
            return
        }

        if (userEmail == selectedItem.sellerEmail) {
            binding.buttonDeleteItem.visibility = View.VISIBLE
            binding.buttonDeleteItem.setOnClickListener {
                itemViewModel.delete(selectedItem.id)
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        } else {
            binding.buttonDeleteItem.visibility = View.GONE
        }

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = selectedItem.description

    }
}
