package com.example.roomiesync.adapter
import com.example.roomiesync.model.ExpenseModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomiesync.R
import com.example.roomiesync.ui.activity.UpdateExpenseActivity

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseAdapter(var context: Context,
                     var data : ArrayList<ExpenseModel>)
    : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var updateBtn : TextView = itemView.findViewById(R.id.updateBtn)
        var expenseName : TextView = itemView.findViewById(R.id.expenseNameEV)
        var price : TextView = itemView.findViewById(R.id.priceEV)
        var userName : TextView = itemView.findViewById(R.id.userNameEV)

    }
    fun convertLongToDateString(timeInMillis: Long, format: String = "yyyy-MM-dd"): String {
        val date = Date(timeInMillis)
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView : View = LayoutInflater.from(context).inflate(
            R.layout.expense_sample,
            parent,false)
        return ExpenseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.expenseName.text = data[position].expenseName
        holder.price.text = data[position].expenseAmt.toString()
        holder.userName.text = data[position].userName

        holder.updateBtn.setOnClickListener {
           val intent = Intent(context, UpdateExpenseActivity::class.java)
           intent.putExtra("expenseId",data[position].expenseId)
           context.startActivity(intent)
       }
   }

    fun updateData(expenses: List<ExpenseModel>){
        data.clear()
        data.addAll(expenses)
        notifyDataSetChanged()

    }

    fun getexpenseId(position: Int) : String{
        return data[position].expenseId
    }

}