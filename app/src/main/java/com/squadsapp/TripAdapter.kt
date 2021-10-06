package com.squadsapp

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.custom_listview_trip.view.*

class TripAdapter(val mCtx: Context, val layoutResId:Int, val tripList: List<Trip>, val username:String)
    :ArrayAdapter<Trip>(mCtx, layoutResId, tripList){
    override fun getView(position:Int, convertView:View?, parent: ViewGroup?):View  {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(layoutResId, null)

        val trip = tripList[position]

        view.tvFrom.text = trip.from
        view.tvDestination.text = trip.destination
        view.tvTime.text = trip.time
        view.tvDate.text = trip.date
        view.tvSeats.text = trip.Seats.toString()

        //Event click on Trips List
        view.setOnClickListener{
            val intent = Intent(mCtx, TripInfo::class.java)
            intent.putExtra("tripID", trip.tripID)
            intent.putExtra("username", username)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mCtx.startActivity(intent)
        }
        return view
    }
}