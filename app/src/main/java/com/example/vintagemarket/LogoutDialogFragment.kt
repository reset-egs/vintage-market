package com.example.vintagemarket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.vintagemarket.R

class LogoutDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                //redirect back to login fragment
                val navController = findNavController()
                navController.navigate(R.id.action_global_loginFragment)
            }
            .setNegativeButton("No") { _, _ ->
                dismiss()
            }
            .create()
    }
}