package fr.isen.derollezcassandra.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Price (@SerializedName("price")val price: String): Serializable