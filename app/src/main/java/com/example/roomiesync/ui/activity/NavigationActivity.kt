package com.example.roomiesync.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityNavigationBinding
import com.example.roomiesync.ui.fragments.ChoreFragment
import com.example.roomiesync.ui.fragments.ExpenseFragment
import com.example.finaltaskmanager.ui.fragment.Roommate_Fragment

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ChoreFragment())



        // Set up the BottomNavigationView listener
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chore_btn -> replaceFragment(ChoreFragment())
                R.id.roommate_btn -> replaceFragment(Roommate_Fragment())
                R.id.expense_btn -> replaceFragment(ExpenseFragment())
                else -> false
            }
            true
        }
    }

    // Function to replace the fragment
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFrame, fragment)
        fragmentTransaction.commit()
    }
}
