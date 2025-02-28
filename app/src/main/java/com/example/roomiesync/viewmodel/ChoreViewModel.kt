package com.example.roomiesync.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.roomiesync.model.ChoreModel
import com.example.roomiesync.repository.ChoreRepository

class ChoreViewModel(val repo: ChoreRepository) {

    fun addChore(chore: ChoreModel, callback: (Boolean, String) -> Unit) {
        repo.addChore(chore, callback)
    }

    fun updateChore(
        choreId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit) {
        repo.updateChore(choreId,data, callback)
    }

    fun deleteChore(choreId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteChore(choreId, callback)
    }

    var _chore = MutableLiveData<ChoreModel>()
    var chore = MutableLiveData<ChoreModel>()
        get() = _chore

    var _getAllChores = MutableLiveData<List<ChoreModel>>()
    var getAllChores = MutableLiveData<List<ChoreModel>>()
        get() = _getAllChores

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading

    fun getChoreById(choreId: String) {
        repo.getChoreById(choreId) { chores, success, message ->
            if (success) {
                _chore.value = chores
            }
        }
    }

    fun getAllChores() {
        _loading.value = true
        repo.getAllChores { products, success, message ->
            if (success) {
                _getAllChores.value = products
                _loading.value = false
            }
        }
    }

}
