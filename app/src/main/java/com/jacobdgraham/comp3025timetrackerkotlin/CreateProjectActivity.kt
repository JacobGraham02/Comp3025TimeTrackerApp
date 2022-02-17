package com.jacobdgraham.comp3025timetrackerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.jacobdgraham.comp3025timetrackerkotlin.databinding.ActivityCreateProjectBinding

class CreateProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPushProjectToDatabase.setOnClickListener {
            val projectName = binding.editTextProjectName.text.toString().trim()
            val projectDescription = binding.editTextProjectDescription.text.toString().trim()

            if (projectName.isNotEmpty() && projectDescription.isNotEmpty()) {
                var project = Project(projectName, projectDescription)

                // Get instance of a Firebase database and connect to an existing collection
                val databaseInstance = FirebaseFirestore.getInstance().collection("projects")

                // Fetch unique id from Firestore for the insertion to collection
                val uniqueId = databaseInstance.document().getId()
                project.id = uniqueId

                databaseInstance.document(uniqueId).set(project).addOnSuccessListener {
                    Toast.makeText(this, "Firebase database updated with new project", Toast.LENGTH_LONG).show()
                }
                    .addOnFailureListener {
                        exception -> Log.w("Firebase database issue", exception.localizedMessage)
                    }
            } else {
                Toast.makeText(this, "Project name and description must be filled in", Toast.LENGTH_LONG).show()
            }
        }
    }
}