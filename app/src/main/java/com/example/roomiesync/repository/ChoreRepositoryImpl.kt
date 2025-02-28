package com.example.roomiesync.repository

import com.example.roomiesync.model.ChoreModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChoreRepositoryImpl : ChoreRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("chores")

    override fun addChore(chore: ChoreModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key ?: return callback(false, "Failed to generate chore ID")
        val choreWithId = chore.copy(choreId = id)

        ref.child(id).setValue(choreWithId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Chore Added Successfully")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }

    override fun getChoreById(choreId: String, callback: (ChoreModel?,Boolean, String,) -> Unit) {
        ref.child(choreId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chore = snapshot.getValue(ChoreModel::class.java)
                    callback(chore, true,"Chore fetched successfully")
                } else {
                    callback(null,false, "Chore not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }
        })
    }

    override fun getAllChores(callback: (List<ChoreModel>?,Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chores = mutableListOf<ChoreModel>()
                    for (eachData in snapshot.children) {
                        val chore = eachData.getValue(ChoreModel::class.java)
                        chore?.let { chores.add(it) }
                    }
                    callback(chores,true,"Chores fetched successfully")
                } else {
                    callback(emptyList(), false,"Chores fetched unsuccessfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false,error.message)
            }
        })
    }

    override fun updateChore(
        choreId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(choreId).updateChildren(data).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Product updated successfully")
            }else{
                callback(false,"${it.exception?.message}")

            }
        }
    }

    override fun deleteChore(choreId: String, callback: (Boolean, String) -> Unit) {
        ref.child(choreId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Chore deleted Successfully")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }
}