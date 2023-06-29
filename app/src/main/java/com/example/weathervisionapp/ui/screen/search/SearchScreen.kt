package com.example.weathervisionapp.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.example.weathervisionapp.R
import com.example.weathervisionapp.data.local.Constants.ERROR_SCREEN
import com.example.weathervisionapp.data.model.forecast.FiveDaysForecastResponse
import com.example.weathervisionapp.data.model.forecast.ListItem
import com.example.weathervisionapp.data.model.geocoding.GeocodingResponse
import com.weatherapp.data.model.weather.CurrentWeatherResponse
import com.example.weathervisionapp.data.network.Resource
import com.example.weathervisionapp.data.viewmodel.SearchViewModel
import com.example.weathervisionapp.ui.theme.BIG_MARGIN
import com.example.weathervisionapp.ui.theme.LARGE_MARGIN
import com.example.weathervisionapp.ui.theme.MEDIUM_MARGIN
import com.example.weathervisionapp.ui.theme.SMALL_MARGIN
import com.example.weathervisionapp.ui.theme.*
import com.example.weathervisionapp.util.RequestState
import com.example.weathervisionapp.util.handleApiError

@OptIn(ExperimentalCoilApi::class)
@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavHostController
) {

    var windSpeed: Int? = null
    var forecast: List<ListItem> = listOf()
    var currentWeather: CurrentWeatherResponse? = null
    val context = LocalContext.current


    if (viewModel.requestState.value == RequestState.COMPLETE) {
        val geocodingResponse by viewModel.geocoding.collectAsState()
        val weatherResponse by viewModel.currentWeather.collectAsState()
        val forecastResponse by viewModel.weatherForecast.collectAsState()

        // Observe on geocoding response from api
        if (geocodingResponse is Resource.Success) {
            val response =
                geocodingResponse as Resource.Success<List<GeocodingResponse>>
            response.value.also { geocoding ->
                if (geocoding.isNotEmpty()) {
                    viewModel.initCurrentWeather(
                        geocoding[0].lat ?: 0.0,
                        geocoding[0].lon ?: 0.0
                    )
                } else DisplayInvalidSearch(navController, viewModel)
            }
        } else if (geocodingResponse is Resource.Failure) {
            context.handleApiError(geocodingResponse as Resource.Failure)
            viewModel.requestState.value = RequestState.IDLE
        }

        // Observe on weather response from api
        if (weatherResponse is Resource.Success) {
            currentWeather =
                (weatherResponse as Resource.Success<CurrentWeatherResponse>).value
            windSpeed =
                currentWeather.wind?.speed?.times(60)?.times(60)?.div(1000)
                    ?.toInt()
        } else if (weatherResponse is Resource.Failure) {
            context.handleApiError(weatherResponse as Resource.Failure)
        }

        // Observe on forecast response from api
        if (forecastResponse is Resource.Success) {
            forecast =
                (forecastResponse as Resource.Success<FiveDaysForecastResponse>).value.list!!
        } else if (forecastResponse is Resource.Failure) {
            context.handleApiError(forecastResponse as Resource.Failure)
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(bottom = BIG_MARGIN)
    ) {

        item {
            Header(viewModel, currentWeather, windSpeed)
        }

    }
}


@ExperimentalCoilApi
@Composable
fun Header(
    viewModel: SearchViewModel,
    currentWeather: CurrentWeatherResponse?,
    windSpeed: Int?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LARGE_MARGIN),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "",
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Search",
                fontSize = 25.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Enter City Name",
                fontSize = 16.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.padding(top = MEDIUM_MARGIN)
            )

            SearchBar(
                modifier = Modifier
                    .padding(top = LARGE_MARGIN)
                    .background(MaterialTheme.colors.surface),
                text = viewModel.searchTextState.value,
                onTextChange = { newText ->
                    viewModel.searchTextState.value = newText
                },
                onSearchClicked = {
                    if (it.isNotEmpty()) {
                        viewModel.initGeocoding(it)
                    }
                }
            )
        }

        currentWeather?.let {
            val lastIndex =
                currentWeather.main?.temp.toString().indexOf(".")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LARGE_MARGIN),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = currentWeather.name.toString(),
                    fontSize = 30.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Card(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.temperature),
                            contentDescription = "",
                            modifier = Modifier.size(130.dp)
                        )

                        Spacer(modifier = Modifier.padding(end = 8.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${
                                    currentWeather.main?.temp.toString()
                                        .substring(0, lastIndex)
                                }°",
                                fontFamily = FontFamily.Serif,
                                fontSize = 90.sp,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.padding(top = SMALL_MARGIN)
                            )

                            Text(
                                text = currentWeather.weather?.get(0)?.description.toString(),
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.secondary
                            )
                        }


                    }
                }


                //Card 3

                Card(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(4.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.wind),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.padding(end = 8.dp))

                        Text(
                            text = "$windSpeed ${
                                stringResource(
                                    id = R.string.km_h
                                )
                            }",
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier.padding(start = MEDIUM_MARGIN)
                        )
                    }
                }


                //Card 4

                Card(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(4.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.view),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.padding(end = 8.dp))

                        Text(
                            text = "${stringResource(id = R.string.visibility)} ${
                                currentWeather.visibility?.div(
                                    1000
                                )
                            } ${
                                stringResource(id = R.string.km)
                            }",
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier.padding(start = SMALL_MARGIN)
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(top = LARGE_MARGIN))
            }


        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                modifier = Modifier
                    .alpha(ContentAlpha.medium),
                text = stringResource(id = R.string.search),
                color = MaterialTheme.colors.primary
            )
        },
        textStyle = TextStyle(color = MaterialTheme.colors.primaryVariant),
        singleLine = true,
        leadingIcon = {
            IconButton(modifier = Modifier.alpha(ContentAlpha.disabled),
                onClick = {
                    onSearchClicked(text)
                    focusManager.clearFocus()
                }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    tint = MaterialTheme.colors.primaryVariant,
                    contentDescription = ""
                )
            }
        },
        trailingIcon = {
            IconButton(modifier = Modifier.alpha(ContentAlpha.disabled),
                onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else focusManager.clearFocus()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = MaterialTheme.colors.primaryVariant,
                    contentDescription = ""
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClicked(text)
                focusManager.clearFocus()
            }
        )
    )
}


@Composable
fun DisplayInvalidSearch(
    navController: NavHostController,
    viewModel: SearchViewModel
) {
    navController.navigate(
        "$ERROR_SCREEN/${stringResource(id = R.string.error)}/${
            stringResource(
                id = R.string.invalid_city
            )
        }/${
            stringResource(
                id = R.string.ok
            )
        }"
    )
    viewModel.requestState.value = RequestState.IDLE
}
