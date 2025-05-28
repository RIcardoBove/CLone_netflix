package co.tiagoaguiar.netflixremake

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.adapter.MovieAdapter
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import co.tiagoaguiar.netflixremake.util.DownloadImageTask
import co.tiagoaguiar.netflixremake.util.MovieTask


class MovieActivity : AppCompatActivity(), MovieTask.Callback {

    private lateinit var progressMovie: ProgressBar
    private val movies = mutableListOf<Movie>()

    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtCast: TextView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)



        txtTitle = findViewById(R.id.movie_txt_title)
        txtDesc = findViewById(R.id.movie_txt_desc)
        txtCast = findViewById(R.id.movie_txt_cast)
        progressMovie = findViewById(R.id.movie_progress)

        val rvSimilar: RecyclerView = findViewById(R.id.movie_rv_similar)

        val movieId =
            intent?.getIntExtra("id", 0) ?: throw IllegalStateException("ID não foi encontrado")

        val url =
            "https://api.tiagoaguiar.co/netflixapp/movie/$movieId?apiKey=281aa89d-c2fb-4c58-970b-d14195a474e1"

        MovieTask(this).execute(url)

        adapter = MovieAdapter(movies, R.layout.movie_item_similar)
        rvSimilar.layoutManager = GridLayoutManager(this, 3)
        rvSimilar.adapter = adapter



        val toobar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toobar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPreExecute() {
        progressMovie.visibility = View.VISIBLE
    }

    override fun onResult(movieDetail: MovieDetail) {
        progressMovie.visibility = View.GONE

        txtTitle.text = movieDetail.movie.title
        txtDesc.text =  movieDetail.movie.desc
        txtCast.text = getString(R.string.cast, movieDetail.movie.cast)

        movies.clear()
        movies.addAll(movieDetail.similar)
        adapter.notifyDataSetChanged()

        DownloadImageTask(object : DownloadImageTask.Callback {
            override fun onResult(bitmap: Bitmap) {
                //Busquei o desenhavel (layer-list)
                val layerDrawable: LayerDrawable =
                    ContextCompat.getDrawable(this@MovieActivity, R.drawable.shadows) as LayerDrawable
                //Busquei o filme que eu quero
                val movieCover = BitmapDrawable(resources, bitmap)

                //atribui o filme para o layer-list
                layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

                //setar a imageView
                val coverImg: ImageView = findViewById(R.id.movie_img)
                coverImg.setImageDrawable(layerDrawable)
            }

        }).execute(movieDetail.movie.coverUrl)

    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        progressMovie.visibility = View.GONE
    }
}