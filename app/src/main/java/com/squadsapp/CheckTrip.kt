package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_check_trip.*

class CheckTrip : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_trip)

        val TripRef = FirebaseDatabase.getInstance().getReference("Trips")
        val username:String = intent.extras.getString("username")
        var trips = mutableListOf<Trip>()
        TripRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                trips.clear()
                if (p0!!.exists()){
                    for (i in p0.children){
                        val trip = i.getValue(Trip::class.java)
                        for (j in trip!!.passengersList){
                            if (username.equals(j.username)){
                                trips.add(trip!!)
                            }
                        }
                    }
                    val sortedTrips = trips.sortedWith(compareBy(Trip::from, Trip::date))
                    val adapter = TripAdapter(applicationContext, R.layout.custom_listview_trip, sortedTrips, username)
                    lvCheckTrips.adapter = adapter
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

        btnProfile.setOnClickListener {
            val intent = Intent(application, Profile::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        btnHome.setOnClickListener {
            val intent = Intent(application, Triplist::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}
