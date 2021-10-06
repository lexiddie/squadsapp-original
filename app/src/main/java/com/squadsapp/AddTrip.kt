package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_trip.*
import android.app.DatePickerDialog
import android.view.View
import java.util.*
import java.text.SimpleDateFormat
import android.app.TimePickerDialog


class AddTrip : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)
        val tripRef = FirebaseDatabase.getInstance().getReference("Trips")
        val userRef = FirebaseDatabase.getInstance().getReference("User")
        val username:String = intent.extras.getString("username")


        val myCalendar = Calendar.getInstance()

        fun updateLabel(){
            val myFormat = "dd/MMM/yy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            txtDate.setText(sdf.format(myCalendar.getTime()))
        }
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        txtDate.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View) {
                DatePickerDialog(this@AddTrip,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
        txtTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val hour = myCalendar.get(Calendar.HOUR_OF_DAY)
                val minute = myCalendar.get(Calendar.MINUTE)
                val timepicker: TimePickerDialog
                timepicker = TimePickerDialog(this@AddTrip, R.style.DialogTheme, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute -> txtTime.setText(String.format("%02d:%02d", hour, minute)) }, hour, minute, true)
                timepicker.show()
            }
        })

        val passengerList = mutableListOf<Passengers>()

        btnCreate.setOnClickListener{
            if (txtFrom.text.toString().isNullOrEmpty() || txtDestination.text.toString().isNullOrEmpty() || txtTime.text.toString().isNullOrEmpty() || txtDate.text.toString().isNullOrEmpty() || txtSeats.text.toString().isNullOrEmpty()){
                SingleToast.show(applicationContext, "Please fill the Information", Toast.LENGTH_LONG)
            }
            else{
                userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val key = tripRef.push().key
                        val maxSeats = txtSeats.text.toString().toInt()
                        if (p0.exists()){
                            for (u in p0.children){
                                val user = u.getValue(User::class.java)
                                if (username.equals(user!!.username)){
                                    val passenger = Passengers(user.username, user.name, user.phonenumber)
                                    passengerList.add(passenger)
                                    val trip = Trip(key!!, false, txtFrom.text.toString(), txtDestination.text.toString(), txtTime.text.toString(), txtDate.text.toString(), maxSeats-1, passengerList)
                                    tripRef.child(key).setValue(trip).addOnCompleteListener{
                                        val intent = Intent(applicationContext, Triplist::class.java)
                                        intent.putExtra("username", username)
                                        startActivity(intent)
                                        SingleToast.show(applicationContext, "Trip successful", Toast.LENGTH_LONG)
                                        txtFrom.text.clear()
                                        txtDestination.text.clear()
                                        txtTime.text.clear()
                                        txtDate.text.clear()
                                        txtSeats.text.clear()
                                    }
                                    return
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
