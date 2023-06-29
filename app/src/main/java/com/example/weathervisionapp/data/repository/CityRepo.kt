package com.example.weathervisionapp.data.repository

import android.content.Context
import com.example.weathervisionapp.R
import com.example.weathervisionapp.data.model.City
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CityRepo @Inject constructor(
    @ApplicationContext val context: Context
) {

    fun getCities(): List<City> {
        context.apply {
            return listOf(
                City(0,getString(R.string.mumbai)),
                City(1,getString(R.string.delhi)),
                City(2,getString(R.string.bangalore)),
                City(3,getString(R.string.hyderabad)),
                City(4,getString(R.string.ahmedabad)),
                City(5,getString(R.string.chennai)),
                City(6,getString(R.string.kolkata)),
                City(7,getString(R.string.giza)),
                City(8,getString(R.string.madrid)),
                City(9,getString(R.string.saint_petersburg)),
                City(10,getString(R.string.rome)),
                City(11,getString(R.string.kuwait)),
                City(12,getString(R.string.pakistan))
            )
        }
    }
}