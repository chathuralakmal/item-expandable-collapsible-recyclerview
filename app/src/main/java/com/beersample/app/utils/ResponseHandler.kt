package com.beersample.app.utils

import com.beersample.app.tableview.model.Beer
import org.json.JSONException

interface ResponseHandler {
    @Throws(JSONException::class)
    fun serviceResponse(status: Boolean, message: String, tag: String)

    @Throws(JSONException::class)
    fun serviceBeerResponse(status: Boolean, beers: List<Beer>, message: String, page:String, tag: String) {
    }
}