package com.beersample.app.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.beersample.app.tableview.model.Beer
import org.json.JSONArray
import org.json.JSONObject

object Util {
    //Pass JSON Array and Create Model List
    fun createBeerList(jsonArray: JSONArray): List<Beer> {
        val beerList = arrayListOf<Beer>()

        for (i in 0 until jsonArray.length()) {
            val beerItem = jsonArray.getJSONObject(i)

            val beer = Beer()

            beer.id = nullCheckOnFloatItem(beerItem,"id").toFloat()
            beer.name = beerItem.getString("name")
            beer.tagline = beerItem.getString("tagline")
            beer.firstBrewed = beerItem.getString("first_brewed")
            beer.description = beerItem.getString("description")
            beer.imageUrl = beerItem.getString("image_url")
            beer.abv = nullCheckOnFloatItem(beerItem,"abv").toFloat()
            beer.ibu = nullCheckOnFloatItem(beerItem,"ibu").toFloat()
            beer.targetFg = nullCheckOnFloatItem(beerItem,"target_fg").toFloat()
            beer.targetOg = nullCheckOnFloatItem(beerItem,"target_og").toFloat()
            beer.ebc = nullCheckOnFloatItem(beerItem,"ebc").toFloat()
            beer.srm = nullCheckOnFloatItem(beerItem,"srm").toFloat()
            beer.ph = nullCheckOnFloatItem(beerItem,"ph").toFloat()
            beer.attenuationLevel = nullCheckOnFloatItem(beerItem,"attenuation_level").toFloat()

            val foodPair = beerItem.getJSONArray("food_pairing")
            val foodPairingArray = ArrayList<String>()
            for(x in 0 until foodPair.length()){
                val stringItem = foodPair.getString(x)
                foodPairingArray.add(stringItem)
            }
            beer.foodPairing = foodPairingArray

            beer.brewersTips = beerItem.getString("brewers_tips")
            beer.contributedBy = beerItem.getString("contributed_by")

            beerList.add(beer)
        }

        return beerList
    }

    //Null check for Floating Values
    private fun nullCheckOnFloatItem(item: JSONObject, key: String) : String {
        if(!item.isNull(key)){
            return item.getString(key)
        }
        return "0"
    }

    //Remove Spaces and put underscore for Text Search
    fun removeSpace(query: String) : String {
        return query.replace("\\s".toRegex(), "_")
    }

    fun showAlert(message: String, title: String, context: Context) {
        val alert = AlertDialog.Builder(context).create()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setButton(
            AlertDialog.BUTTON_POSITIVE,
            context.getString(android.R.string.ok),
            { _, _ -> })
        alert.show()
    }

}

