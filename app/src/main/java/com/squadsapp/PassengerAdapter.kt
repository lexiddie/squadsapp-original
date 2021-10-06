package com.squadsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.custom_passengers_list.view.*

/**
 * Created by lexiddie on 9/12/17.
 */

class PassengerAdapter (val mCtx: Context, val layoutResId:Int, val passengerList: List<Passengers>)
    :ArrayAdapter<Passengers>(mCtx, layoutResId, passengerList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        var users = passengerList[position]
        view.tvName.text = users.name
        view.tvPhone.text = users.phonenumber

        return  view
    }

}