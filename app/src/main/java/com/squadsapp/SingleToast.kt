package com.squadsapp

import android.content.Context
import android.widget.Toast

/**
 * Created by lexiddie on 3/12/17.
 */
object SingleToast {

    private var toast: Toast? = null

    fun show(context: Context, text: String, duration: Int) {
        if (toast != null) toast!!.cancel()
        toast = Toast.makeText(context, text, duration)
        toast!!.show()
    }
}