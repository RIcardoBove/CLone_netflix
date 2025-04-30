package co.tiagoaguiar.netflixremake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.adapter.CategoryAdapter
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity() {

    // m-v-c Model- [View- Controller] Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val categories = mutableListOf<Category>()



        val adapter = CategoryAdapter(categories)
        val rvMovie: RecyclerView = findViewById(R.id.rv_main)
        rvMovie.layoutManager = LinearLayoutManager(this)
        rvMovie.adapter = adapter

        CategoryTask().execute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=281aa89d-c2fb-4c58-970b-d14195a474e1")
    }


}