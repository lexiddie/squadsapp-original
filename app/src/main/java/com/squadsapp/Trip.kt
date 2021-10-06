package com.squadsapp

/**
 * Created by lexiddie on 26/11/17.
 */
class  Trip (var tripID: String, var isTripEnded: Boolean, var from: String, var destination: String, var time:String, var date:String, var Seats:Int, var passengersList: List<Passengers>){
    constructor() : this ("", false,"", "", "", "",0 , emptyList())
}