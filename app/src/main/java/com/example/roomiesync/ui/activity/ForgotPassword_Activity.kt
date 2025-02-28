package com.example.roomiesync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityForgotPasswordBinding
import com.example.roomiesync.repository.UserRepositoryImpl
import com.example.roomiesync.viewmodel.UserViewModel

class ForgotPassword_Activity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)
        var repo = UserRepositoryImpl()
        userViewModel= UserViewModel(repo)
        binding.resetPasswordButton.setOnClickListener{
            var email :String = binding.emailEditText.text.toString()
            userViewModel.forgetPassword(email){
                    success,message->
                if(success){
                    //dissmiss
                    Toast.makeText(this@ForgotPassword_Activity,message, Toast.LENGTH_LONG).show()

                    var intent = Intent(this@ForgotPassword_Activity,NavigationActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    //dismiss
                    Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                }

                var intent = Intent(this@ForgotPassword_Activity,
                    LoginActivity::class.java)
                startActivity(intent)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}