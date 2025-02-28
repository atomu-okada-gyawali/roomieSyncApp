package com.example.roomiesync.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityUpdateChoreBinding
import com.example.roomiesync.model.ChoreModel
import com.example.roomiesync.repository.ChoreRepositoryImpl
import com.example.roomiesync.utils.LoadingUtils
import com.example.roomiesync.viewmodel.ChoreViewModel
import java.util.Calendar

class UpdateChoreActivity : AppCompatActivity() {
    lateinit var choreViewModel: ChoreViewModel
    lateinit var binding: ActivityUpdateChoreBinding
    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateChoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo = ChoreRepositoryImpl()
        choreViewModel = ChoreViewModel(repo)

        var choreId = intent.getStringExtra("choreId").toString()

        choreViewModel.getChoreById(choreId)
        choreViewModel.chore.observe(this) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.date

            binding.choreNameInputUC.setText(it?.choreName.toString())
            binding.datePickerUC.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            binding.userNameInputUC.setText(it?.userName.toString())
        }

        loadingUtils = LoadingUtils(this)

        binding.updateChoreBtn.setOnClickListener {
            var name = binding.choreNameInputUC.text.toString()
            var date = getDatePickerLongValue() // Calling the function here
            var userName = binding.userNameInputUC.text.toString()

            var updatedData = mutableMapOf<String, Any>()
            updatedData["choreName"] = name
            updatedData["date"] = date
            updatedData["userName"] = userName

            choreViewModel.updateChore(choreId, updatedData) { success, message ->
                if (success) {
                    Toast.makeText(this@UpdateChoreActivity, message, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@UpdateChoreActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Move the function outside of onCreate
    private fun getDatePickerLongValue(): Long {
        val year = binding.datePickerUC.year
        val month = binding.datePickerUC.month // 0-based month
        val dayOfMonth = binding.datePickerUC.dayOfMonth

        // Create a Calendar instance
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        // Get the long value (milliseconds)
        return calendar.timeInMillis
    }
}
