package co.tiagoaguiar.netflixremake

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
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
import co.tiagoaguiar.netflixremake.util.MovieTask


class MovieActivity : AppCompatActivity(), MovieTask.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val txtTitle: TextView = findViewById(R.id.movie_txt_title)
        val txtDesc: TextView = findViewById(R.id.movie_txt_desc)
        val txtCast: TextView = findViewById(R.id.movie_txt_cast)
        val rvSimilar: RecyclerView = findViewById(R.id.movie_rv_similar)

        val movieId = intent?.getIntExtra("id", 0) ?: throw IllegalStateException("ID não foi encontrado")

        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$movieId?apiKey=281aa89d-c2fb-4c58-970b-d14195a474e1"

        MovieTask(this).execute(url)

        val movies = mutableListOf<Movie>()

        rvSimilar.layoutManager = GridLayoutManager(this, 3)
        rvSimilar.adapter = MovieAdapter(movies, R.layout.movie_item_similar)

        txtTitle.text = "Batman Begins"
        txtDesc.text = "Essa é a descrição do filme do Batman"
        txtCast.text = getString(R.string.cast, "Ator A, Ator B, Atriz A, Atriz B")

        val toobar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toobar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        //Busquei o desenhavel (layer-list)
        val layerDrawable: LayerDrawable =
            ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable

        //Busquei o filme que eu quero
        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)

        //atribui o filme para o layer-list
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

        //setar a imageView
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResult(movieDetail: MovieDetail) {
        Log.i("teste", movieDetail.toString())
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}