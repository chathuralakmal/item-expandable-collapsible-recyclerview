package com.beersample.app.tableview.model

class Beer {
    var id = 0f
    var name: String? = null
    var tagline: String? = null
    var firstBrewed: String? = null
    var description: String? = null
    var imageUrl: String? = null
    var abv = 0f
    var ibu = 0f
    var targetFg = 0f
    var targetOg = 0f
    var ebc = 0f
    var srm = 0f
    var ph = 0f
    var attenuationLevel = 0f
    var foodPairing = ArrayList<String>()
    var brewersTips: String? = null
    var contributedBy: String? = null

    // State of the item
    var expanded = false
}