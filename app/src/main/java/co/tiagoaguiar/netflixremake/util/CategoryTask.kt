package co.tiagoaguiar.netflixremake.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class CategoryTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute() {}
        fun onResult(categories: List<Category>)
        fun onFailure(message: String)
    }

    fun execute(url: String) {

        // Nesse momento, estamos utilizando a UI-Thread (1)
        val executor = Executors.newSingleThreadExecutor()
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
                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }
                stream = urlConnection.inputStream // sequencia de bytes

                // Forma 1: Simples e rápida
//                val jsonAsString = stream.bufferedReader().use { it.readText() } // transformar o byte}

                //Forma 2:
                buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)

                val categories = toCategory(jsonAsString)

                handler.post {
                    // Aqui roda dentro da UI-Thread
                    callback.onResult(categories)
                }

                callback.onResult(categories)

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

    private fun toCategory(jsonAsString: String): List<Category> {
        val categories = mutableListOf<Category>()

        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")

        for (i in 0 until jsonCategories.length()) {
            val jsonCategory = jsonCategories.getJSONObject(i)
            val title = jsonCategory.getString("title")
            val movie = jsonCategory.getJSONArray("movie")

            val movies = mutableListOf<Movie>()

            for (j in 0 until movie.length()) {
                val jsonMovie = movie.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl = jsonMovie.getString("cover_url")

                movies.add(Movie(id, coverUrl))
            }

            categories.add(Category(title, movies))

        }

        return categories
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
