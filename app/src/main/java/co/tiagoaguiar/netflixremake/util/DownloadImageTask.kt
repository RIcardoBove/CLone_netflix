package co.tiagoaguiar.netflixremake.util


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class DownloadImageTask(private val callback: Callback) {

    val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onResult(bitmap: Bitmap)
    }

    fun execute(url: String) {

        executor.execute {

            var urlConnection: HttpURLConnection? = null
            var stream: InputStream? = null

            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 2000 // tempo de leitura que vou levar para ler a conexão (2s)
                urlConnection.connectTimeout = 2000 // tempo de conexão que vou levar para abrir a conexão (2s)

                val statusCode = urlConnection.responseCode // pega o código de resposta da requisição

                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }

                stream = urlConnection.inputStream // sequencia de bytes
                val bitmap = BitmapFactory.decodeStream(stream)

                handler.post {
                    callback.onResult(bitmap)
                }

            } catch (e: IOException) {

                val message = e.message ?: "erro desconhecido"
                Log.e("Teste", message, e)

            }  finally {
                urlConnection?.disconnect()
                stream?.close()
            }

        }
    }


}