package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_update_information.*
import android.widget.Toast
import android.text.SpannableStringBuilder



class UpdateInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_information)

        val username:String = intent.extras.getString("username")
        val UserRef = FirebaseDatabase.getInstance().getReference("User")
        val TripRef = FirebaseDatabase.getInstance().getReference("Trips")

        var updateinfo = Passengers("", "", "")

        UserRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    for (t in p0.children){
                        var user = t.getValue(User::class.java)
                        val name = SpannableStringBuilder(user!!.name)
                        val email = SpannableStringBuilder(user!!.email)
                        val phone = SpannableStringBuilder(user!!.phonenumber)
                        if (username.equals(user!!.username)){
                            txtNameUpdate.text = name
                            txtEmailUpdate.text = email
                            txtPhoneNumberUpdate.text = phone
                        }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })


        btnConfrimUpdate.setOnClickListener{
            if (txtNameUpdate.text.toString().isNullOrEmpty() || txtEmailUpdate.text.toString().isNullOrEmpty() || txtPhoneNumberUpdate.text.toString().isNullOrEmpty()){
                SingleToast.show(applicationContext, "Please fill the information to update", Toast.LENGTH_LONG)
            }
            else{
                UserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0!!.exists()){
                            for (u in p0.children){
                                var user = u.getValue(User::class.java)
                                if (username.equals(user!!.username)){
                                    val name:String = txtNameUpdate.text.toString()
                                    val email:String = txtEmailUpdate.text.toString()
                                    val phone:String = txtPhoneNumberUpdate.text.toString()
                                    val update = User(name, email, phone, user!!.username, user!!.password)
                                    val passenger = Passengers(username, name, phone)
                                    updateinfo = passenger
                                    UserRef.child(u.key!!).setValue(update).addOnCompleteListener() {
                                        val intent = Intent(applicationContext, Profile::class.java)
                                        intent.putExtra("username", username)
                                        startActivity(intent)
                                        finish()
                                SingleToast.show(applicationContext, "Your information has been updated", Toast.LENGTH_LONG)
                            }
                            txtNameUpdate.text.clear()
                            txtEmailUpdate.text.clear()
                            txtPhoneNumberUpdate.text.clear()
                            return
                        }
                            }
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
                TripRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0!!.exists()){
                            for (i in p0.children){
                                var trip = i.getValue(Trip::class.java)
                                var childnumber = 0
                                for (j in trip!!.passengersList){
                                    if (username.equals(j.username)){
                                        TripRef.child(i.key!!).child("passengersList").child(childnumber.toString()).setValue(updateinfo).addOnCompleteListener {
                                        }
                                    }
                                    childnumber += 1
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
