package com.example.roomiesync.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityUpdateExpenseBinding
import com.example.roomiesync.model.ExpenseModel
import com.example.roomiesync.repository.ExpenseRepositoryImpl
import com.example.roomiesync.utils.LoadingUtils
import com.example.roomiesync.viewmodel.ExpenseViewModel
import java.util.Calendar

class UpdateExpenseActivity : AppCompatActivity() {
    lateinit var expenseViewModel: ExpenseViewModel
    lateinit var binding: ActivityUpdateExpenseBinding
    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = ExpenseRepositoryImpl()
        expenseViewModel = ExpenseViewModel(repo)

        val expenseId = intent.getStringExtra("expenseId").toString()

        expenseViewModel.getExpenseById(expenseId)
        expenseViewModel.expense.observe(this) {
            binding.expenseNameInputUE.setText(it?.expenseName.toString())
            binding.priceInputUE.setText(it?.expenseAmt.toString())
            binding.userNameInputUE.setText(it?.userName.toString())
        }

        loadingUtils = LoadingUtils(this)

        binding.updateExpenseBtn.setOnClickListener {
            val name = binding.expenseNameInputUE.text.toString()
            val priceString = binding.priceInputUE.text.toString()
            val userName = binding.userNameInputUE.text.toString()

            // Input validations
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an expense name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (priceString.isEmpty()) {
                Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price: Double
            try {
                price = priceString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a user name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedData = mutableMapOf<String, Any>()
            updatedData["expenseName"] = name
            updatedData["price"] = price
            updatedData["userName"] = userName

            expenseViewModel.updateExpense(expenseId, updatedData) { success, message ->
                if (success) {
                    Toast.makeText(this@UpdateExpenseActivity, message, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@UpdateExpenseActivity, message, Toast.LENGTH_LONG).show()
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
