package com.example.mobiletechnologiesadvanced2022

import com.google.gson.annotations.SerializedName


data class ToDo (

  @SerializedName("userId"    ) var userId    : Int?     = null,
  @SerializedName("id"        ) var id        : Int?     = null,
  @SerializedName("title"     ) var title     : String?  = null,
  @SerializedName("completed" ) var completed : Boolean? = null

)