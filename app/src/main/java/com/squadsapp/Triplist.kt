package com.squadsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_triplist.*


class Triplist : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triplist)

        val ref = FirebaseDatabase.getInstance().getReference("Trips")

        val username:String = intent.extras.getString("username")

        //Display all Trips List
        var trips = mutableListOf<Trip>()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                trips.clear()
                if(p0!!.exists()){
                    for (t in p0.children){
                        val trip = t.getValue(Trip::class.java)
                        trips.add(trip!!)
                    }
                    val sortedTrips = trips.sortedWith(compareBy(Trip::from, Trip::date))
                    val adapter = TripAdapter(applicationContext, R.layout.custom_listview_trip, sortedTrips, username)
                    lvTripsList.adapter = adapter

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        btnProfile.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        btnAddTrip.setOnClickListener{
            val intent = Intent(this, AddTrip::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}
