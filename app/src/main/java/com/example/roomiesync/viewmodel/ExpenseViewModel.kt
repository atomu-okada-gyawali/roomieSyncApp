package com.example.roomiesync.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.roomiesync.model.ExpenseModel
import com.example.roomiesync.repository.ExpenseRepository

class ExpenseViewModel(val repo: ExpenseRepository) {

    fun addExpense(expense: ExpenseModel, callback: (Boolean, String) -> Unit) {
        repo.addExpense(expense, callback)
    }

    fun updateExpense(
        expenseId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit) {
        repo.updateExpense(expenseId,data, callback)
    }

    fun deleteExpense(expenseId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteExpense(expenseId, callback)
    }

    var _expense = MutableLiveData<ExpenseModel>()
    var expense = MutableLiveData<ExpenseModel>()
        get() = _expense

    var _getAllExpenses = MutableLiveData<List<ExpenseModel>>()
    var getAllExpenses = MutableLiveData<List<ExpenseModel>>()
        get() = _getAllExpenses

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading

    fun getExpenseById(expenseId: String) {
        repo.getExpenseById(expenseId) { expenses, success, message ->
            if (success) {
                _expense.value = expenses
            }
        }
    }

    fun getAllExpenses() {
        _loading.value = true
        repo.getAllExpenses { products, success, message ->
            if (success) {
                _getAllExpenses.value = products
                _loading.value = false
            }
        }
    }

}
