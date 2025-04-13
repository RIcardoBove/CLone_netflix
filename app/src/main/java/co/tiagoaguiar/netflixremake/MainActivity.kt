package co.tiagoaguiar.netflixremake

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.adapter.MainAdapter
import co.tiagoaguiar.netflixremake.model.Movie

class MainActivity : AppCompatActivity() {

    // m-v-c Model- [View- Controller] Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieList = mutableListOf<Movie>()
        for (i in 0 until 50) {
            val movie = Movie(R.drawable.movie)
            movieList.add(movie)
        }

        val adapter = MainAdapter(movieList)
        val rvMain: RecyclerView = findViewById(R.id.rv_main)
        rvMain.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvMain.adapter = adapter
    }


}