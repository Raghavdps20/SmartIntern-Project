package com.example.weathervisionapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathervisionapp.data.local.Constants.CITY_INDEX
import com.example.weathervisionapp.data.local.Constants.CITY_NAME
import com.example.weathervisionapp.data.repository.CityRepo
import com.example.weathervisionapp.util.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repo: CityRepo,
    private val dataStore: DataStoreRepo
) : ViewModel() {

    fun getAllCities() =
        repo.getCities()

    fun saveCityIndex(index: Int) {
        viewModelScope.launch {
            dataStore.writeInt(CITY_INDEX,index)
        }
    }

    fun saveCityName(city: String) {
        viewModelScope.launch {
            dataStore.writeString(CITY_NAME,city)
        }
    }
}