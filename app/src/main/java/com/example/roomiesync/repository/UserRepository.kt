package com.example.roomiesync.repository

import com.example.roomiesync.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

interface UserRepository {
    fun login(email:String,password:String,
              callback:(Boolean,String)->Unit)

    fun signup(email:String,password:String,
               callback: (Boolean, String,String) -> Unit)

    fun forgetPassword(email:String,
                       callback: (Boolean, String) -> Unit)

    fun getCurrentUser() : FirebaseUser?
    fun addUserToDatabase(userId: String, userModel: UserModel,
                          callback: (Boolean, String) -> Unit)
fun getUserById(        userId: String,
                        data: MutableMap<String, Any>,
                        callback: (UserModel?,Boolean, String) -> Unit);
fun getAllUsers(
    callback: (List<UserModel>?,Boolean, String) -> Unit);
fun updateUser(userId: String,
               data: MutableMap<String, Any>,
               callback: (Boolean, String)  -> Unit);
fun deleteUser(productId: String, callback: (Boolean, String) -> Unit);

}
