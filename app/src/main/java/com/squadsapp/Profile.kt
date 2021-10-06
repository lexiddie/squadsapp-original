package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val ref = FirebaseDatabase.getInstance().getReference("User")

        val username:String = intent.extras.getString("username")

        var users = mutableListOf<User>()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    for (t in p0.children){
                        var user = t.getValue(User::class.java)
                        if (username.equals(user!!.username)){
                            users.add(user!!)
                            tvDisplayName.text = user!!.name
                            tvPhoneNumber.text = user!!.phonenumber
                            tvUsername.text = user!!.username
                            tvEmail.text = user!!.email
                        }
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        btnCheckTrip.setOnClickListener {
            val intent = Intent(this, CheckTrip::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateInformation::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        btnResetPassword.setOnClickListener {
            val intent = Intent(this, setup_new_password::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        btnLogout.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        btnHome.setOnClickListener {
            val intent = Intent(this, Triplist::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}
