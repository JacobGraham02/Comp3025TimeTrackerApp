package com.jacobdgraham.comp3025timetrackerkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

/**
 * Connect and access the elements in the item_project layout file
 * The purpose of a ProjectAdapter is to make the RecyclerView 'adaptable' and able
 * to display various Project objects and its corresponding data onto the RecyclerView.
 * them on the RecyclerView. The ProjectAdapter takes the list of data retrieved from Firebase database and converts that to the specified data type
 * in Project.
 *
 */
class ProjectAdapter(val context: Context, private val projectsList: List<Project>, val itemListener: ProjectItemListener) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val projectTextView: TextView = itemView.findViewById<TextView>(R.id.projectTextView)
        val descriptionTextView: TextView = itemView.findViewById<TextView>(R.id.descriptionTextView)
    }

    /**
     * Inflates the individual ViewHolder with the RecyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    /**
     * Binds the viewHolder with a project object
     */
    override fun onBindViewHolder(projectViewHolder: ProjectViewHolder, position: Int) {
        val project = projectsList[position]
        with (projectViewHolder) {
            projectTextView.text = project.projectName
            descriptionTextView.text = project.description
            itemView.setOnClickListener {
                itemListener.projectSelected(project)
            }
        }
    }

    /**
     * Returns the number of projects in recycler view
     */
    override fun getItemCount(): Int {
        return projectsList.size
    }

    interface ProjectItemListener {
        fun projectSelected(project: Project) {

        }
    }
}