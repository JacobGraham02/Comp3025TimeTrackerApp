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
 */
class ProjectAdapter(val context: Context, val projectsList: List<Project>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val projectTextView = itemView.findViewById<TextView>(R.id.projectTextView)
        val descriptionTextView = itemView.findViewById<TextView>(R.id.descriptionTextView)
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
        }
    }

    /**
     * Returns the number of projects in recycler view
     */
    override fun getItemCount(): Int {
        return projectsList.size
    }
}