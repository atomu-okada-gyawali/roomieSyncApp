package com.example.roomiesync.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityAddExpenseBinding
import com.example.roomiesync.model.ExpenseModel
import com.example.roomiesync.repository.ExpenseRepositoryImpl
import com.example.roomiesync.utils.LoadingUtils
import com.example.roomiesync.viewmodel.ExpenseViewModel

class AddExpenseActivity : AppCompatActivity() {

    lateinit var expenseViewModel: ExpenseViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var binding: ActivityAddExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtils = LoadingUtils(this)
        val repo = ExpenseRepositoryImpl()
        expenseViewModel = ExpenseViewModel(repo)

        binding.addExpenseBtn.setOnClickListener {
            loadingUtils.show()
            val name = binding.expenseNameInput.text.toString()
            val priceString = binding.priceInput.text.toString()
            val userName = binding.userNameInput.text.toString()

            // Input validations
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an expense name", Toast.LENGTH_SHORT).show()
                loadingUtils.dismiss()
                return@setOnClickListener
            }

            if (priceString.isEmpty()) {
                Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show()
                loadingUtils.dismiss()
                return@setOnClickListener
            }

            val price: Double
            try {
                price = priceString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                loadingUtils.dismiss()
                return@setOnClickListener
            }

            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a user name", Toast.LENGTH_SHORT).show()
                loadingUtils.dismiss()
                return@setOnClickListener
            }

            // Create the expense model if validations pass
            val model = ExpenseModel("", name, price, userName)

            expenseViewModel.addExpense(model) { success, message ->
                if (success) {
                    Toast.makeText(this@AddExpenseActivity, message, Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                    finish()
                } else {
                    Toast.makeText(this@AddExpenseActivity, message, Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
