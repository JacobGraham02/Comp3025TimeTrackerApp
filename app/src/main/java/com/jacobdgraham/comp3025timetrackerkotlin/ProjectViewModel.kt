package com.jacobdgraham.comp3025timetrackerkotlin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProjectViewModel : ViewModel() {
    // MutableLiveData means this list of data is mutable, and can change. This list is listening for any changes that occur to any of its elements
    private val projects = MutableLiveData<List<Project>>()
    private var auth : FirebaseAuth

    // Querying a database returns a snapshot: a one-time picture of the database.
    init {
        auth=Firebase.auth
        val userId = Firebase.auth.currentUser?.uid

        var databaseCollection = FirebaseFirestore.getInstance().collection("projects")
            .whereEqualTo("userId", userId)
            .orderBy("projectName")
            .addSnapshotListener { documents, exception ->
                if (exception != null) {
                    Log.w("Database response", "Snapshot listener failed: ${exception.code}")
                    return@addSnapshotListener
                }

                // Loop over documents and create Project objects
                documents?.let {
                    val projectList = ArrayList<Project>()

                    for (document in documents) {
                        Log.i("Firebase database response", "${document.data}")

                        // Convert the JSON document into a Project object
                        val project = document.toObject(Project::class.java)
                        projectList.add(project)
                    }
                    projects.value = projectList
                }
            }
    }

    fun getProjects(): LiveData<List<Project>> {
        return projects
    }
}