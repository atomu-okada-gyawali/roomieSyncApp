package com.example.roomiesync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityRegistrationBinding
import com.example.roomiesync.repository.UserRepositoryImpl
import com.example.roomiesync.viewmodel.UserViewModel
import com.example.roomiesync.model.UserModel

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        binding.signUp.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val name = binding.registerFname.text.toString()
            val address = binding.registerAddress.text.toString()
            val contact = binding.registerContact.text.toString()

            // Input validations
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contact.isEmpty()) {
                Toast.makeText(this, "Please enter your contact number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.signup(email, password) { success, message, userId ->
                if (success) {
                    val userModel = UserModel(userId, name, contact, email, 0, address, password)
                    userViewModel.addUserToDatabase(userId, userModel) { success, message ->
                        Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.go2LoginBtn.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
