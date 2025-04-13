package co.tiagoaguiar.netflixremake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.adapter.CategoryAdapter
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie

class MainActivity : AppCompatActivity() {

    // m-v-c Model- [View- Controller] Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = mutableListOf<Category>()
        for (j in 0 until 10){
            val movies = mutableListOf<Movie>()
            for (i in 0 until 10) {
                val movie = Movie(R.drawable.movie)
                movies.add(movie)
            }
            val category = Category("Cat $j", movies)
            categories.add(category)
        }



        val adapter = CategoryAdapter(categories)
        val rvMovie: RecyclerView = findViewById(R.id.rv_main)
        rvMovie.layoutManager = LinearLayoutManager(this)
        rvMovie.adapter = adapter
    }


}