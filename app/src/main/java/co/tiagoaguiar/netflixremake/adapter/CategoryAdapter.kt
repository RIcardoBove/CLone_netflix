package co.tiagoaguiar.netflixremake.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.model.Category

class CategoryAdapter(private val categories: List<Category>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories.get(position)
        holder.bind(category)


    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            val txTitle = itemView.findViewById<TextView>(R.id.txt_title)
            txTitle.text = category.name

            val rvCategory = itemView.findViewById<RecyclerView>(R.id.rv_category)
            rvCategory.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            rvCategory.adapter = MovieAdapter(category.movies, R.layout.movie_item)
        }
    }

}