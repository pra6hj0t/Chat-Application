package com.example.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Sign_up : AppCompatActivity() {

    private lateinit var edtName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnSignup : Button
    private lateinit var mDbRef : DatabaseReference

    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignup = findViewById(R.id.btn_signup)

        mAuth = FirebaseAuth.getInstance()


        btnSignup.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val name = edtName.text.toString()

            signup(name,email,password)
        }

    }

    private fun signup(name : String , email: String, password: String) {
        //logic for creating new users

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUserToDataBase(name,email,mAuth.currentUser?.uid!!)

                    val intent = Intent(this@Sign_up,MainActivity::class.java)
                    Toast.makeText(this@Sign_up,"Done",Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Sign_up,"Some Error Occurred",Toast.LENGTH_SHORT).show()

                }
            }

    }

    private fun addUserToDataBase(name: String, email: String, uid: String) {

        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("Users").child(uid).setValue(User(name,email,uid))
    }
}