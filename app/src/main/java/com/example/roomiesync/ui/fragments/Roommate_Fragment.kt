package com.example.finaltaskmanager.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.roomiesync.R
import com.example.roomiesync.repository.UserRepositoryImpl
import com.example.roomiesync.ui.activity.LoginActivity
import com.example.roomiesync.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class Roommate_Fragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var currentEmailTextView: TextView
    private lateinit var currentUsernameTextView: TextView
    private lateinit var logoutCardView: CardView
    private lateinit var currentAddressTextView:TextView
    private lateinit var currentContactTextView:TextView
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout (ensure the file name matches your XML file, here it's fragment_profile2.xml)
        val view = inflater.inflate(R.layout.fragment_roommate_, container, false)

        auth = FirebaseAuth.getInstance()


        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)
        // Bind UI components from the XML
        currentEmailTextView = view.findViewById(R.id.currentEmail)
        currentContactTextView = view.findViewById(R.id.currentContact)
        currentUsernameTextView = view.findViewById(R.id.currentUsername)
        currentAddressTextView= view.findViewById(R.id.currentAddress)
        logoutCardView = view.findViewById(R.id.logout)

        // Display the currently logged in user's email and username directly from FirebaseAuth and Database
        val currentUser = auth.currentUser
        println(currentUser)
        if (currentUser != null) {

            val userId = currentUser.uid
            println("Fetching user with ID: $userId") // Log the user ID
            userViewModel.getUserById(userId) { userModel, success, message ->

                if (userModel != null) {
                    currentUsernameTextView.text = "Username : ${userModel.name}"
                    currentAddressTextView.text = "Address: ${userModel.address}"
                    currentEmailTextView.text = "Email:${userModel.email}"
                    currentContactTextView.text = "Contact:${userModel.contact}"

                } else {
                    currentUsernameTextView.text = "Username:"
                    currentAddressTextView.text = "Address:"
                    currentEmailTextView.text = "Email:"
                    currentContactTextView.text = "Contact:"

        Toast.makeText(requireContext(), "Failed to fetch user: $message", Toast.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "userId: ${currentUser.uid}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            currentEmailTextView.text = "Not logged in"
            currentUsernameTextView.text = "Username : N/A"
        }

        // Set click listener for logout
        logoutCardView.setOnClickListener {
            logout()
        }
        return view
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
    }
}

