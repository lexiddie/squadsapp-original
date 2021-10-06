package com.squadsapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val ref = FirebaseDatabase.getInstance().getReference("User")

        btnRegisterR.setOnClickListener {
            if (txtName.text.isNullOrEmpty() || txtEmail.text.isNullOrEmpty() || txtPhoneNumber.text.isNullOrEmpty() || txtUsername.text.isNullOrEmpty() || txtPassword.text.isNullOrEmpty() || txtConfirmPassword.text.isNullOrEmpty()) {
                SingleToast.show(this, "Please fill the field", Toast.LENGTH_LONG)
            }
            else {
                if (txtPassword.text.toString() != txtConfirmPassword.text.toString()) {
                    SingleToast.show(this, "Password does not match", Toast.LENGTH_LONG)
                }
                else {
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            var name:String = txtName.text.toString()
                            var email:String = txtEmail.text.toString()
                            var username:String = txtUsername.text.toString()
                            var phone:String = txtPhoneNumber.text.toString()
                            var password:String = txtPassword.text.toString()
                            var childloop = 0
                            var childcount:Int = p0!!.childrenCount.toInt()
                            for (u in p0!!.children){
                                var newuser = u.getValue(User::class.java)
                                childloop += 1
                                if (username.equals(newuser!!.username)){
                                    SingleToast.show(applicationContext, "This username isn't available.", Toast.LENGTH_LONG)
                                    return
                                }
                            }
                            if (childloop == childcount || childcount == 0){
                                val userlogin = User(name, email, phone, username, password)
                                ref.child(ref.push().key!!).setValue(userlogin).addOnCompleteListener() {
                                    val intent = Intent(applicationContext, Profile::class.java)
                                    intent.putExtra("username", username)
                                    finish()
                                    startActivity(intent)
                                    SingleToast.show(applicationContext, "Register successful!", Toast.LENGTH_LONG)
                                }
                                txtName.text.clear()
                                txtEmail.text.clear()
                                txtUsername.text.clear()
                                txtPassword.text.clear()
                                txtPhoneNumber.text.clear()
                                txtConfirmPassword.text.clear()
                                return
                            }
                        }
                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
                }

            }
        }
    }
}
