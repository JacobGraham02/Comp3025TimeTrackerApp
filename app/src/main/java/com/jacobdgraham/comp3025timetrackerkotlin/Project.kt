package com.jacobdgraham.comp3025timetrackerkotlin

data class Project(var projectName: String? = null, var description: String? = null, var id:String? = null,
                   var userId:String? = null, var timeRecords: ArrayList<TimeRecord>? = null) {
    override fun toString(): String {
        return if (projectName != null) {
            projectName!!
        } else {
            "Project is not defined"
        }
    }
}