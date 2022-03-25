package com.jacobdgraham.comp3025timetrackerkotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        val projectId = intent.getStringExtra("projectId")

        if (projectId == null) {
            Toast.makeText(this, "Select a project to log time", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, CreateProjectActivity::class.java))
        }

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

        binding.floatingActionButton.setOnClickListener {
            var intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
        }

        setSupportActionBar(binding.toolbarMain.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_log_time -> {
                startActivity(Intent(applicationContext, LogTimeActivity::class.java))
                return true
            }
            R.id.action_add_project -> {
                // startActivity(Intent(applicationContext, CreateProjectActivity::class.java))
                return true
            }
            R.id.action_view_summary -> {
                // Page to be created
                return true
            }
            R.id.action_edit_profile -> {
                // Page to be created
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}