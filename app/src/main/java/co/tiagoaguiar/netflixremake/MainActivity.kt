package co.tiagoaguiar.netflixremake

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.adapter.CategoryAdapter
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {

    private val categories = mutableListOf<Category>()
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CategoryAdapter

    // m-v-c Model- [View- Controller] Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_main)

        adapter = CategoryAdapter(categories) {
            val intent = Intent(this@MainActivity, MovieActivity::class.java)
            intent.putExtra("id", it)
            startActivity(intent)
        }

        val rvMovie: RecyclerView = findViewById(R.id.rv_main)
        rvMovie.layoutManager = LinearLayoutManager(this)
        rvMovie.adapter = adapter

        CategoryTask(this).execute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=281aa89d-c2fb-4c58-970b-d14195a474e1")
    }

    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE

    }

    override fun onResult(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE

    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }


}