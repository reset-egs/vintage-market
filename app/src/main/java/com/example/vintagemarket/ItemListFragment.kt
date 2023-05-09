package com.example.vintagemarket

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vintagemarket.databinding.FragmentItemListBinding
import com.example.vintagemarket.models.Item
import com.example.vintagemarket.models.ItemListAdapter
import com.example.vintagemarket.models.ItemViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val itemViewModel: ItemViewModel by activityViewModels()

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLogoutDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Vintage Market"

        itemViewModel.itemsLiveData.observe(viewLifecycleOwner) { items ->
            binding.itemListRecyclerView.visibility = if (items == null) View.GONE else View.VISIBLE
            if (items != null) {
                val adapter = ItemListAdapter(items) { id ->
                    val action =
                        ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(id)
                    findNavController().navigate(action)
                }

                var columns = 1
                val currentOrientation = this.resources.configuration.orientation
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    columns = 2
                } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    columns = 1
                }
                binding.itemListRecyclerView.layoutManager =
                    GridLayoutManager(this.context, columns)

                binding.itemListRecyclerView.adapter = adapter
            }
        }
        val sortSpinner = binding.sortSpinner

        val sortAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options,
            android.R.layout.simple_spinner_item
        )
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = sortAdapter

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(parent?.getItemAtPosition(position).toString()){
                    "Sort by price (ascending)" -> itemViewModel.sortByPrice()
                    "Sort by price (descending)" -> itemViewModel.sortByPriceDescending()
                    "Sort by description (ascending)" -> itemViewModel.sortByDescription()
                    "Sort by description (descending)" -> itemViewModel.sortByDescriptionDescending()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.searchButton.setOnClickListener(){
            val description = binding.searchEditText.text.toString()
            itemViewModel.filterByDescription(description)
        }


    }

    override fun onResume() {
        super.onResume()
        itemViewModel.reload()
        (activity as MainActivity).showFAB()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).hideFAB()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // create a function to navigate to the item detail fragment when an item is clicked on the list of items passing the item id as an argument overriding the onItemClick function from the ItemListAdapter class


    private fun showLogoutDialog() {
        LogoutDialogFragment().show(childFragmentManager, "LogoutDialogFragment")
    }
}