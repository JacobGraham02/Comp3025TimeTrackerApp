package com.jacobdgraham.comp3025timetrackerkotlin

import com.google.firebase.Timestamp

class TimeRecord(var activity: String? = null, var startTime: Timestamp? = null, var endTime : Timestamp? = null)