package com.squadsapp

/**
 * Created by lexiddie on 26/11/17.
 */

class User(val name:String, val email:String, val phonenumber: String, val username: String, val password: String){
    constructor() : this("", "", "", "", "")
}