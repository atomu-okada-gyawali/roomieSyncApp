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

        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)


        binding.signUp.setOnClickListener {
            var email = binding.registerEmail.text.toString()
            var password = binding.registerPassword.text.toString()
            var name = binding.registerFname.text.toString()
            var address = binding.registerAddress.text.toString()
            var contact = binding.registerContact.text.toString()

            userViewModel.signup(email, password) {
                    success, message, userId ->
                if (success) {
                    var userModel = UserModel(userId,
                    name,
                    contact,
                    email ,
                    0,
                    address,
                    password
                    )
                    userViewModel.addUserToDatabase(userId,userModel){
                            success,message->
                        if(success){
//                            loadingUtils.dismiss()
                            Toast.makeText(
                                this@RegistrationActivity,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        }else{
//                            loadingUtils.dismiss()
                            Toast.makeText(
                                this@RegistrationActivity,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
//                    loadingUtils.dismiss()
                    Toast.makeText(
                        this@RegistrationActivity,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//
//                        var userId = auth.currentUser?.uid
//
//                        var userModel = UserModel(
//                            userId.toString(), firstName,
//                            lastName, address,
//                            phone, email
//                        )
//
//                        ref.child(userId.toString()).setValue(userModel)
//                            .addOnCompleteListener {
//                            if(it.isSuccessful){
//                                Toast.makeText(
//                                    this@RegistrationActivity,
//                                    "Registration success", Toast.LENGTH_LONG
//                                ).show()
//                            }else{
//                                Toast.makeText(
//                                    this@RegistrationActivity,
//                                    it.exception?.message, Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//
//
//                    } else {
//                        Toast.makeText(
//                            this@RegistrationActivity,
//                            it.exception?.message, Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
        }
        binding.go2LoginBtn.setOnClickListener{
            var intent = Intent(this@RegistrationActivity,
                LoginActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}