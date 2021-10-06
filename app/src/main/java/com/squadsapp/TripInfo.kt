package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_trip_info.*

class TripInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_info)

        val TripRef = FirebaseDatabase.getInstance().getReference("Trips")
        val UserRef = FirebaseDatabase.getInstance().getReference("User")

        val tripID = intent.extras.getString("tripID")
        val username = intent.extras.getString("username")

        var passengers = mutableListOf<Passengers>()

        //Display Passengers as List
        TripRef.child(tripID!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val trip = p0.getValue(Trip::class.java)
                    if (!trip!!.passengersList.isEmpty()){
                        //After launch this Activity all previous Passengers will auto collected to #passengers mutableList and when add new passenger, this will be add new and previous Passengers
                        passengers = trip.passengersList as MutableList<Passengers>
                    }
                    val adapter = PassengerAdapter(applicationContext, R.layout.custom_passengers_list, passengers)
                    lvTripsInfo.adapter = adapter
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        //Display Trip Information
        TripRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (t in p0.children){
                        val trip = t.getValue(Trip::class.java)
                        if (tripID.equals(trip!!.tripID)){
                            tvFrom.text = trip.from
                            tvDestination.text = trip.destination
                            tvTime.text = trip.time
                            tvDate.text = trip.date
                            tvSeats.text = trip.Seats.toString()
                        }
                    }

                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

        btnLeftTrip.setOnClickListener {
            TripRef.child(tripID).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val trip = p0.getValue(Trip::class.java)
                    var checkloop = 0
                    for (t in trip!!.passengersList){
                        checkloop += 1
                        val count = trip.passengersList.count()
                        if (username == t.username){
                            val removepassenger = trip.passengersList as MutableList
                            removepassenger.removeAt(checkloop-1)
                            TripRef.child(tripID).child("passengersList").setValue(removepassenger)
                            if (count == 1){
                                TripRef.child(tripID).removeValue()
                            }
                            else{
                                trip.Seats += 1
                                TripRef.child(tripID).setValue(trip).addOnCompleteListener{
                                }
                            }
                            SingleToast.show(applicationContext, "Your trip has been left", Toast.LENGTH_LONG)
                            val intent = Intent(applicationContext, Triplist::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            return
                        }
                        else if (checkloop == count){
                            SingleToast.show(applicationContext, "You did not join this trip!!", Toast.LENGTH_LONG)
                            return
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        }

        btnJoinTrip.setOnClickListener{
            TripRef.child(tripID).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val trip = p0.getValue(Trip::class.java)
                    var checkloop = 0
                    for (i in trip!!.passengersList){
                        checkloop += 1
                        val count = trip.passengersList.count()
                        if (username!!.equals(i.username)){
                            SingleToast.show(applicationContext, "You already joined.", Toast.LENGTH_LONG)
                            return
                        }
                        else if (checkloop == count){
                            if (!trip.isTripEnded && trip.Seats > 0){
                                UserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()){
                                            for (u in p0.children){
                                                val user = u.getValue(User::class.java)
                                                if (username.equals(user!!.username)){
                                                    val passenger = Passengers(user.username, user.name, user.phonenumber)
                                                    passengers.add(passenger)
                                                    trip.passengersList = passengers
                                                    trip.Seats -= 1
                                                    TripRef.child(tripID).setValue(trip).addOnCompleteListener{
                                                        SingleToast.show(applicationContext, "Join successful.", Toast.LENGTH_LONG)
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
                            else{
                                SingleToast.show(applicationContext, "The seat is not available.", Toast.LENGTH_LONG)
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
