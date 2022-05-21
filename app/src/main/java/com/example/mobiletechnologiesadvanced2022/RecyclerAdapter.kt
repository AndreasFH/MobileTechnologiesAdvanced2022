package com.example.mobiletechnologiesadvanced2022

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val toDos: List<ToDo>) : RecyclerView.Adapter<RecyclerAdapter.ToDoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ToDoHolder {

        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        return ToDoHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ToDoHolder, position: Int) {
        val toDoItem = toDos[position]
        holder.bindTodo(toDoItem)

    }

    override fun getItemCount(): Int {
        return toDos.size
    }

    //1
    class ToDoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var toDoItem: ToDo? = null

        fun bindTodo(toDo: ToDo) {
            this.toDoItem = toDo

            if (toDo.title!!.length > 20) {
                val trimmedTitle = toDo.title?.take(20) + "..."
                view.findViewById<TextView>(R.id.textView_todo_title).text = trimmedTitle

            }
            else {
                view.findViewById<TextView>(R.id.textView_todo_title).text = toDo.title
            }

            if(toDo.completed == true) {
                view.findViewById<TextView>(R.id.textView_todo_completed).text = "✔"
                view.findViewById<TextView>(R.id.textView_todo_completed).setTextColor(Color.GREEN)

            }
            else {
                view.findViewById<TextView>(R.id.textView_todo_completed).text = "X"
                view.findViewById<TextView>(R.id.textView_todo_completed).setTextColor(Color.RED)

            }
        }

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!" + this.toDoItem?.id.toString())

             val action = DataFragmentDirections.actionDataFragmentToDetailFragment(this.toDoItem?.id ?: -1)
            v.findNavController().navigate(action)
        }


    }


}