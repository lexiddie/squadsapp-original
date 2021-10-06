package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_setup_new_password.*

class setup_new_password : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_new_password)

        val ref = FirebaseDatabase.getInstance().getReference("User")
        val username:String = intent.extras.getString("username")

        btnConfrim.setOnClickListener{
            if (txtNewPassword.text.toString().isEmpty() || txtConfirmNewPassword.text.toString().isEmpty()){
                SingleToast.show(applicationContext, "Please fill the field", Toast.LENGTH_LONG)
            }
            else{
                if (txtNewPassword.text.toString() != txtConfirmNewPassword.text.toString()){
                    SingleToast.show(applicationContext, "Yours password does not match", Toast.LENGTH_LONG)
                }
                else{
                    ref.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()){
                                for (u in p0.children){
                                    val user = u.getValue(User::class.java)
                                    if (username.equals(user!!.username)){
                                        val password:String = txtNewPassword.text.toString()
                                        val update = User(user.name, user.email, user.phonenumber, user.username, password)
                                        ref.child(u.key!!).setValue(update).addOnCompleteListener(){
                                            val intent = Intent(applicationContext, Profile::class.java)
                                            intent.putExtra("username", username)
                                            startActivity(intent)
                                            finish()
                                            SingleToast.show(applicationContext, "Yours password has been updated", Toast.LENGTH_LONG)
                                        }
                                        txtNewPassword.text.clear()
                                        txtConfirmNewPassword.text.clear()
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
}
