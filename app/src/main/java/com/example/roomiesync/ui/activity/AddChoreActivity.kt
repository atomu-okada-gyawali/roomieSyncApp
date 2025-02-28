package com.example.roomiesync.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.example.roomiesync.R
import com.example.roomiesync.databinding.ActivityAddChore2Binding
import com.example.roomiesync.model.ChoreModel
import com.example.roomiesync.repository.ChoreRepositoryImpl
import com.example.roomiesync.utils.LoadingUtils
import com.example.roomiesync.viewmodel.ChoreViewModel
import java.util.Calendar


class AddChoreActivity : AppCompatActivity() {

    lateinit var choreViewModel: ChoreViewModel
    lateinit var loadingUtils: LoadingUtils



    lateinit var binding: ActivityAddChore2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        fun getDatePickerLongValue(): Long {
            val year = binding.datePicker.year
            val month = binding.datePicker.month // 0-based month
            val dayOfMonth = binding.datePicker.dayOfMonth

            // Create a Calendar instance
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            // Get the long value (milliseconds)
            return calendar.timeInMillis
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddChore2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtils = LoadingUtils(this)
        val repo = ChoreRepositoryImpl()
        choreViewModel = ChoreViewModel(repo)
        binding.addChoreBtn.setOnClickListener {
            loadingUtils.show()
            var name = binding.choreNameInput.text.toString()
            var date = getDatePickerLongValue()
            var userName = binding.userNameInput.text.toString()

            var model = ChoreModel("",name,date,userName)

            choreViewModel.addChore(model){
                    success,message->
                if(success){
                    Toast.makeText(this@AddChoreActivity,
                        message,
                        Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                    finish()
                }else{
                    Toast.makeText(this@AddChoreActivity,
                        message,
                        Toast.LENGTH_LONG).show()
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