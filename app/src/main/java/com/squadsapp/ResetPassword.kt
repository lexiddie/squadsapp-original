package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val userRef = FirebaseDatabase.getInstance().getReference("User")


        btnReset.setOnClickListener{
            if (txtNameReset.text.toString().isEmpty() || txtEmailReset.text.toString().isEmpty() || txtUsernameReset.text.toString().isEmpty() || txtPhoneNumberReset.text.toString().isEmpty()){
                SingleToast.show(this,"Please fill the Information!!", Toast.LENGTH_LONG)
            }
            else{
                userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val name:String = txtNameReset.text.toString()
                        val email:String = txtEmailReset.text.toString()
                        val username:String = txtUsernameReset.text.toString()
                        val phone:String = txtPhoneNumberReset.text.toString()
                        if (p0.exists()){
                            for (u in p0.children){
                                val user = u.getValue(User::class.java)
                                if (!(!name.equals(user!!.name) || !email.equals(user.email) || !username.equals(user.username) || !phone.equals(user.phonenumber))){
                                    val intent = Intent(applicationContext, setup_new_password::class.java)
                                    intent.putExtra("username", user.username)
                                    startActivity(intent)
                                    SingleToast.show(applicationContext, "Successful", Toast.LENGTH_LONG)
                                    txtNameReset.text.clear()
                                    txtEmailReset.text.clear()
                                    txtPhoneNumberReset.text.clear()
                                    txtUsernameReset.text.clear()
                                    return

                                }
                                else{
                                    SingleToast.show(applicationContext, "Account Information Does Not Match", Toast.LENGTH_SHORT)
                                }
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }
}
