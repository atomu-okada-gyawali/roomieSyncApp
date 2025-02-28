package com.example.roomiesync.repository

import com.example.roomiesync.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImpl : UserRepository {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var ref: DatabaseReference = database.reference
        .child("users")

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun login(
        email: String, password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Login succesfull")
                } else {
                    callback(false, it.exception?.message.toString())
                }
            }
    }

    override fun signup(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(
                        true, "Registration success",
                        auth.currentUser?.uid.toString()
                    )
                } else {
                    callback(false, it.exception?.message.toString(), "")
                }
            }
    }
    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Reset email sent to $email")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        val id = ref.push().key.toString()
        userModel.userId = id

        ref.child(id).setValue(userModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User added successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }


    override fun getUserById(
        userId: String,
        data: MutableMap<String, Any>,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        ref.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val model = snapshot.getValue(UserModel::class.java)
                    callback(model, true, "User fetched successfully")
                } else {
                    callback(null, false, "User not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllUsers(callback: (List<UserModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserModel>()
                if (snapshot.exists()) {
                    for (eachUser in snapshot.children) {
                        val data = eachUser.getValue(UserModel::class.java)
                        if (data != null) {
                            users.add(data)
                        }
                    }
                    callback(users, true, "Users fetched successfully")
                } else {
                    callback(null, false, "No users found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateUser(
        userId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User updated successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteUser(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }
}
