package co.tiagoaguiar.netflixremake.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R

 class MainAdapter: RecyclerView.Adapter<MainAdapter.MovieViewHolder>(){

     inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 60
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

    }

}