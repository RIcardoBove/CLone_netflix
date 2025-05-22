package co.tiagoaguiar.netflixremake.util

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MovieTask(private val callback: Callback) {

    val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    interface Callback {

        fun onPreExecute() {}
        fun onResult(movieDetail: MovieDetail)
        fun onFailure(message: String)
    }

    fun execute(url: String) {

        // Nesse momento, estamos utilizando a UI-Thread (1)

        executor.execute {
            callback.onPreExecute()

            var urlConnection: HttpURLConnection? = null
            var buffer: BufferedInputStream? = null
            var stream: InputStream? = null

            try {
                // Nesse momento, estamos utilizando a NOVA-THREAD [processo paralelo](2)

                val requestUrl = URL(url)
                urlConnection =
                    requestUrl.openConnection() as HttpURLConnection // abrir a conexão
                urlConnection.readTimeout =
                    2000 // tempo de leitura que vou levar para ler a conexão (2s)
                urlConnection.connectTimeout =
                    2000 // tempo de conexão que vou levar para abrir a conexão (2s)

                val statusCode: Int =
                    urlConnection.responseCode // pega o código de resposta da requisição

                if (statusCode == 400) {
                    stream = urlConnection.errorStream
                    buffer = BufferedInputStream(stream)
                    val jsonAsString = toString(buffer)

                    val json = JSONObject(jsonAsString)

                    val message = json.getString("message")
                    throw IOException(message)

                } else if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }



                stream = urlConnection.inputStream // sequencia de bytes


                val jsonAsString =
                    stream.bufferedReader().use { it.readText() } // transformar o byte}

                val movieDetail = toMovieDetail(jsonAsString)

                handler.post {
                    // Aqui roda dentro da UI-Thread
                    callback.onResult(movieDetail)
                }

            } catch (e: IOException) {

                handler.post {
                    callback.onFailure(e.message ?: "Erro desconhecido")
                }

            } finally {
                urlConnection?.disconnect()
                stream?.close()
                buffer?.close()
            }

        }

    }

    private fun toMovieDetail(jsonAsString: String): MovieDetail {
        val json = JSONObject(jsonAsString)

        val id = json.getInt("id")
        val title = json.getString("title")
        val desc = json.getString("desc")
        val cast = json.getString("cast")
        val coverUrl = json.getString("cover_url")
        val jsonMovies = json.getJSONArray("movie")

        val similars = mutableListOf<Movie>()

        for (i in 0 until jsonMovies.length()) {
            val jsonMovie = jsonMovies.getJSONObject(i)

            val similarId = jsonMovie.getInt("id")
            val similarCoverUrl = jsonMovie.getString("cover_url")

            val m = Movie(similarId, similarCoverUrl)

            similars.add(m)
        }

        val movie = Movie(id, coverUrl, title, desc, cast)

        return MovieDetail(movie, similars)

    }


    private fun toString(stream: BufferedInputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int

        while (true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }

        return String(baos.toByteArray())
    }


}
