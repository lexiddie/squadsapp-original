package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LogIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var ref = FirebaseDatabase.getInstance(). getReference("User")

        btnRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLostpassword.setOnClickListener{
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            if (txtUsername_login.text.isNullOrEmpty() || txtPassword_login.text.isNullOrEmpty()){
                SingleToast.show(this, "Please fill the Username & Password", Toast.LENGTH_LONG)
            }else{
                ref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        for (u in p0!!.children){
                            val user = u.getValue(User::class.java)
                            if (txtUsername_login.text.toString().trim().equals(user!!.username)){
                                if (txtPassword_login.text.toString().trim().equals(user!!.password)) {
                                    SingleToast.show(applicationContext, "Login Successful", Toast.LENGTH_LONG)
                                    var intent = Intent(applicationContext, Triplist::class.java)
                                    intent.putExtra("username", user.username)
                                    startActivity(intent)
                                    return
                                }
                            }
                        }
                        SingleToast.show(applicationContext, "Username or Password is Incorrect", Toast.LENGTH_LONG)
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }
}
