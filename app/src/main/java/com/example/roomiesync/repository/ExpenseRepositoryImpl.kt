package com.example.roomiesync.repository

import com.example.roomiesync.model.ExpenseModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExpenseRepositoryImpl : ExpenseRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("expenses")

    override fun addExpense(expense: ExpenseModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key ?: return callback(false, "Failed to generate expense ID")
        val expenseWithId = expense.copy(expenseId = id)

        ref.child(id).setValue(expenseWithId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Expense Added Successfully")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }

    override fun getExpenseById(expenseId: String, callback: (ExpenseModel?,Boolean, String,) -> Unit) {
        ref.child(expenseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val expense = snapshot.getValue(ExpenseModel::class.java)
                    callback(expense, true,"Expense fetched successfully")
                } else {
                    callback(null,false, "Expense not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }
        })
    }

    override fun getAllExpenses(callback: (List<ExpenseModel>?,Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val expenses = mutableListOf<ExpenseModel>()
                    for (eachData in snapshot.children) {
                        val expense = eachData.getValue(ExpenseModel::class.java)
                        expense?.let { expenses.add(it) }
                    }
                    callback(expenses,true,"Expenses fetched successfully")
                } else {
                    callback(emptyList(), false,"Expenses fetched unsuccessfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }
        })
    }

    override fun updateExpense(
        expenseId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(expenseId).updateChildren(data).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Product updated successfully")
            }else{
                callback(false,"${it.exception?.message}")

            }
        }
    }

    override fun deleteExpense(expenseId: String, callback: (Boolean, String) -> Unit) {
        ref.child(expenseId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Expense deleted Successfully")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }
}