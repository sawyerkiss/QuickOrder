package com.example.quickorder.shoppingcart

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category")
    var category: String? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("price")
    var price: String? = null,

    @SerializedName("isPrescription")
    var isPrescription: Boolean? = null
)