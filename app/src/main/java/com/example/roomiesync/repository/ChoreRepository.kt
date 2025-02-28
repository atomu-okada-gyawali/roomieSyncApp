package com.example.roomiesync.repository

import com.example.roomiesync.model.ChoreModel


interface ChoreRepository {
    fun addChore(chore: ChoreModel, callback: (Boolean, String) -> Unit)
    fun getChoreById(choreId: String, callback: (ChoreModel?,Boolean, String, ) -> Unit)
    fun getAllChores(callback: (List<ChoreModel>?, Boolean,String,) -> Unit)
    fun updateChore(choreId: String, data: MutableMap<String, Any>,callback: (Boolean, String) -> Unit)
    fun deleteChore(choreId: String, callback: (Boolean, String) -> Unit)
}
