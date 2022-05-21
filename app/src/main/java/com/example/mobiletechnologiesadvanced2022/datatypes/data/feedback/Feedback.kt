package com.example.mobiletechno

import com.google.gson.annotations.SerializedName


data class Feedback (

  @SerializedName("id"       ) var id       : String? = null,
  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("value"    ) var value    : String? = null,
  @SerializedName("location" ) var location : String? = null,
  @SerializedName("_links"   ) var Links    : Links?  = Links()

)

{
  override fun toString(): String = name + ", " + value
}