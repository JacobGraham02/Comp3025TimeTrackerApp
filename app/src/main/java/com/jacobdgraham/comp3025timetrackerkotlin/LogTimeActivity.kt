package com.jacobdgraham.comp3025timetrackerkotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.jacobdgraham.comp3025timetrackerkotlin.databinding.ActivityLogTimeBinding

class LogTimeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLogTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectId = intent.getStringExtra("projectId");
        var project = Project()
        val database = FirebaseFirestore.getInstance().collection("projects")

        database.whereEqualTo("id", projectId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    project = document.toObject(Project::class.java)
                    binding.projectTextView.text = project.projectName
                }
            }

        var startTime: Timestamp? = null
        var finishTime: Timestamp? = null
        var category: String? = null

        binding.startButton.setOnClickListener {
            if (binding.startTextView.text.toString().isNullOrBlank()) {
                startTime = Timestamp.now()
                binding.startTextView.text = startTime!!.toDate().toString()
            }
        }

        // If a category is selected and the start time has been pushed, establish the end time and store in the database
        binding.finishButton.setOnClickListener {
            if (startTime != null && binding.spinner.selectedItemPosition > 0) {
                category = binding.spinner.selectedItem.toString()
                finishTime = Timestamp.now()
                binding.finishTextView.text = finishTime!!.toDate().toString()

                val timeRecord = TimeRecord(category, startTime, finishTime)
                binding.totalTimeTextView.text = String.format("Total time: %d minutes", timeRecord.getDuration())

                project.addTimeRecord(timeRecord)
                project?.let{
                    database.document(project.id!!).set(project)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Database updated", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Database was not updated due to a failure", Toast.LENGTH_LONG).show()
                        }
                }

            } else {
                Toast.makeText(this, "Start time and category selected required", Toast.LENGTH_LONG).show()
            }
        }
    }
}