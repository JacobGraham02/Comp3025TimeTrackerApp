package com.jacobdgraham.comp3025timetrackerkotlin

import com.google.firebase.Timestamp

class TimeRecord(var activity: String? = null, var startTime: Timestamp? = null, var endTime : Timestamp? = null) {

    fun getDuration(): Long {
        if (startTime != null && endTime != null) {
            val difference = endTime!!.toDate().time - startTime!!.toDate().time
            return difference / 1000 / 60 // Convert from 1/1000 of a second to minutes
        }
        return 0
    }
}