package co.tiagoaguiar.netflixremake.util

import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class CategoryTask {

    fun execute(url: String) {

        try {
            // Nesse momento, estamos utilizando a UI-Thread (1)
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                // Nesse momento, estamos utilizando a NOVA-THREAD [processo paralelo](2)

                val requestUrl = URL(url)
                val urlConnection =
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

                // Forma 1: Simples e rápida
                val stream =  urlConnection.inputStream // sequencia de bytes
                val jsonAsString = stream.bufferedReader().use { it.readText() } // transformar o byte}
                Log.i("TESTE", jsonAsString)

            }

        } catch (e: IOException) {
            Log.e("TESTE", e.message ?: "Erro desconhecido", e )

        }

    }


}
