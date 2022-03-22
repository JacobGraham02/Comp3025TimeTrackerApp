package com.jacobdgraham.comp3025timetrackerkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jacobdgraham.comp3025timetrackerkotlin.databinding.ActivityCreateProjectBinding

class CreateProjectActivity : AppCompatActivity(), ProjectAdapter.ProjectItemListener {

    private lateinit var binding : ActivityCreateProjectBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase authentication
        auth = Firebase.auth

        binding.btnPushProjectToDatabase.setOnClickListener {
            val projectName = binding.editTextProjectName.text.toString().trim()
            val projectDescription = binding.editTextProjectDescription.text.toString().trim()

            if (projectName.isNotEmpty() && projectDescription.isNotEmpty()) {
                // Get instance of a Firebase database and connect to an existing collection
                val databaseInstance = FirebaseFirestore.getInstance().collection("projects")

                val uniqueId = databaseInstance.document().id

                var userId = auth.currentUser!!.uid

                var project = Project(projectName, projectDescription, uniqueId, userId, ArrayList<TimeRecord>())

                // Fetch unique id from Firestore for the insertion to collection
                project.id = uniqueId

                databaseInstance.document(uniqueId).set(project).addOnSuccessListener {
                    Toast.makeText(this, "Firebase database updated with new project", Toast.LENGTH_LONG).show()
                }
                    .addOnFailureListener {
                        exception -> Log.w("Firebase database issue", exception!!.localizedMessage)
                    }
            } else {
                Toast.makeText(this, "Project name and description must be filled in", Toast.LENGTH_LONG).show()
            }
        }

        val viewModel : ProjectViewModel by viewModels()
        viewModel.getProjects().observe(this, { projects ->
            binding.recyclerView.adapter = ProjectAdapter(this, projects, this)
        })
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

    override fun projectSelected(project: Project) {
        var intent = Intent(this, LogTimeActivity::class.java)
        intent.putExtra("projectId", project.id) // Passing extra information to a new intent
        startActivity(intent)
    }
}