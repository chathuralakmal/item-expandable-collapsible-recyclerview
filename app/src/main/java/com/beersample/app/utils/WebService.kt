package com.beersample.app.utils

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.lang.IllegalStateException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

object WebService {

    private const val TAG = "BEERS"
    private const val WEB_SERVICE_URL = "https://api.punkapi.com/v2/beers?"

    const val INVALID_RESPONSE = "An Invalid Response Received."
    const val INVALID_REQUEST = "An Invalid Request sent for Beer API."
    const val NETWORK_ERROR = "Network Error. Please check your network connection."

    fun getBeerData(responseHandler: ResponseHandler, query: String, values: HashMap<String, String>){

        //Convert HashMap to KeyValue Params
        val keyValueParams: String = values.entries.stream()
            .map { e -> encode(e.key).toString() + "=" + encode(Util.removeSpace(e.value)) }
            .collect(Collectors.joining("&"))

        val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()

        val searchQuery = if(query.isNotEmpty()) "beer_name=${Util.removeSpace(query)}&" else ""

        val requestUrl = WEB_SERVICE_URL + searchQuery + keyValueParams

        Log.e("REQUEST URL ",requestUrl)

        val request =
            Request.Builder().url(requestUrl).get().build()

        val call = client.newCall(request)

        call.enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                responseHandler.serviceResponse(false, NETWORK_ERROR, TAG)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    when (response.code) {
                        200 -> {
                            val result = response.body?.string().toString()
                            val beerList = Util.createBeerList(JSONArray(result))
                            values.get("page")?.let {
                                responseHandler.serviceBeerResponse(true,beerList,"",
                                    it,TAG)
                            }
                        }
                        400 -> {
                            responseHandler.serviceResponse(false, INVALID_REQUEST, TAG)
                        }
                        else -> {
                            responseHandler.serviceResponse(false, INVALID_RESPONSE, TAG)
                        }
                    }
                }catch (e: Exception){
                    responseHandler.serviceResponse(false, INVALID_RESPONSE, TAG)
                }
            }
        })
    }

    private fun encode(s: String?): String? {
        return try {
            URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException(e)
        }
    }
}