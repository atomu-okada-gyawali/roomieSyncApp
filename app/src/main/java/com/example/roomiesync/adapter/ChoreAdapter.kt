package com.example.roomiesync.adapter
import com.example.roomiesync.model.ChoreModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomiesync.R
import com.example.roomiesync.ui.activity.UpdateChoreActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChoreAdapter(var context: Context,
                     var data : ArrayList<ChoreModel>)
    : RecyclerView.Adapter<ChoreAdapter.ChoreViewHolder>() {

    class ChoreViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var updateBtn : TextView = itemView.findViewById(R.id.updateBtn)
        var choreName : TextView = itemView.findViewById(R.id.expenseNameEV)
        var date : TextView = itemView.findViewById(R.id.priceEV)
        var userName : TextView = itemView.findViewById(R.id.userNameEV)

    }
    fun convertLongToDateString(timeInMillis: Long, format: String = "yyyy-MM-dd"): String {
        val date = Date(timeInMillis)
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreViewHolder {
        val itemView : View = LayoutInflater.from(context).inflate(
            R.layout.chore_sample,
            parent,false)
        return ChoreViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChoreViewHolder, position: Int) {
        holder.choreName.text = "Chore: ${data[position].choreName}"
        holder.date.text = "Date: ${convertLongToDateString(data[position].date)}"
        holder.userName.text = "User: ${data[position].userName}"

        holder.updateBtn.setOnClickListener {
           val intent = Intent(context, UpdateChoreActivity::class.java)
           intent.putExtra("choreId",data[position].choreId)
           context.startActivity(intent)
       }
   }

    fun updateData(chores: List<ChoreModel>){
        data.clear()
        data.addAll(chores)
        notifyDataSetChanged()

    }

    fun getchoreId(position: Int) : String{
        return data[position].choreId
    }

}