package com.appninjas.composeweather_2.presentation.screens.weather.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.appninjas.composeweather_2.R
import com.appninjas.composeweather_2.presentation.screens.weather.contract.WeatherContract
import com.appninjas.composeweather_2.presentation.screens.weather.contract.WeatherViewModel
import com.appninjas.composeweather_2.presentation.ui.failure.NetworkFailureScreen
import com.appninjas.composeweather_2.presentation.ui.loading.LoadingScreen
import com.appninjas.composeweather_2.presentation.utils.getActivity
import com.appninjas.domain.model.UserLocation
import com.appninjas.domain.model.Weather
import com.appninjas.domain.model.WeatherPartly
import kotlinx.coroutines.flow.onEach

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    WeatherScreenContent(state = state, onEvent = viewModel::handleEvents)

    val context = LocalContext.current
    LaunchedEffect(state) {
        viewModel.effect.onEach { effect ->
            when(effect) {
                is WeatherContract.Effect.ShowErrorToast -> { Toast.makeText(context, effect.errorMessage, Toast.LENGTH_SHORT).show() }
            }
        }
    }

}

@Composable
private fun WeatherScreenContent(
    state: WeatherContract.State,
    onEvent: (WeatherContract.Event) -> Unit) {

    val context = LocalContext.current
    val location = remember { mutableStateOf<Location?>(null) }

    val onLocationLoaded: (Location) -> Unit = { userLocation ->
         location.value = userLocation
         writeUserLocationToSharedPrefs(context, userLocation)
         onEvent(WeatherContract.Event.LoadWeather(location = UserLocation(lat = userLocation.latitude, lon = userLocation.longitude)))
    }

    val userLocation = getUserLocationFromSharedPrefs(context)
    if (userLocation == null) {
        changeUserLocation(context, onLocationLoaded)
    } else {
        onEvent(WeatherContract.Event.LoadWeather(userLocation))
    }

    when(state) {
        is WeatherContract.State.Loading -> LoadingScreen()
        is WeatherContract.State.WeatherDataLoaded -> Content(state.weather, onLocationLoaded)
        is WeatherContract.State.NetworkFailure -> NetworkFailureScreen()
    }

}

@Composable
private fun Content(weather: Weather, onLocationLoaded: (Location) -> Unit) {

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .align(alignment = Alignment.Start)
                    .clickable { changeUserLocation(context, onLocationLoaded) },
                painter = painterResource(id = R.drawable.baseline_location_on_24),
                contentDescription = "choose location button"
            )
            AsyncImage(
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .padding(top = 15.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weather.iconLink)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = "weather icon")
            Text(
                text = weather.conditionLabel,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Color.Black
            )
            Text(
                text = weather.degreesCount.toString() + "°C",
                fontWeight = FontWeight.Normal,
                fontSize = 38.sp,
                color = Color.Black
            )
            Text(
                text = "Ощущается как: ${weather.feelsLike}°C",
                fontWeight = FontWeight.Light,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 10.dp, end = 10.dp),
            contentPadding = PaddingValues(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(weather.partlyPrediction) {item ->
                PartlyPredictionItem(weatherObject = item)
            }
        }
    }
}

@Composable
private fun PartlyPredictionItem(weatherObject: WeatherPartly) {
    Card(modifier = Modifier
        .width(285.dp)
        .height(95.dp)
        .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 5.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weatherObject.iconLink)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = "small weather icon"
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                Text(text = weatherObject.partOfDay, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Температура: " + weatherObject.temp.toString() + "°C", fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Ощущается как: " + weatherObject.feelsLike.toString() + "°C", fontWeight = FontWeight.Thin, fontSize = 15.sp, color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    PartlyPredictionItem(weatherObject = WeatherPartly(iconLink = "https://yastatic.net/weather/i/icons/funky/dark/ovc.svg", feelsLike = 12, partOfDay = "День", temp = 14))
}

private fun changeUserLocation(context: Context, onLocationLoaded: (Location) -> Unit) {
    if (isLocationPermissionGranted(context)) {
        if (isGPSEnabled(context)) {
            val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    onLocationLoaded(location)
                    locationManager.removeUpdates(this)
                }
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        } else {
            showEnableGPSDialog(context)
        }
    } else {
        showGiveLocationPermissionDialog(context)
        requestLocationPermission(context)
    }
}

private fun requestLocationPermission(context: Context) {
    ActivityCompat.requestPermissions(context.getActivity() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)
}

private fun isLocationPermissionGranted(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

private fun isGPSEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

private fun writeUserLocationToSharedPrefs(context: Context, location: Location) {
    val sharedPrefs = context.getSharedPreferences("main", Context.MODE_PRIVATE)
    sharedPrefs.edit().putString("location", "${location.latitude},${location.longitude}")
}

private fun getUserLocationFromSharedPrefs(context: Context): UserLocation? {
    val sharedPrefs = context.getSharedPreferences("main", Context.MODE_PRIVATE)
    val locationString = sharedPrefs.getString("location", null) ?: return null
    val locStringSplit = locationString.split(",")
    return UserLocation(lat = locStringSplit[0].toDouble(), lon = locStringSplit[1].toDouble())
}

private fun showGiveLocationPermissionDialog(context: Context) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("Требуется разрешение")
        .setMessage("Для корректной работы приложения требуется разрешение на мониторинг локации. Это нужно для грамотного отображения прогноза погоды.")
        .setPositiveButton("Ок") { dialog, _ -> dialog.cancel() }
        .setCancelable(false)
        .create()
    dialog.show()
}

private fun showEnableGPSDialog(context: Context) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("Включите GPS")
        .setMessage("Для корректной работы приложения включите GPS. После включения перезапустите приложение, тогда у вас отобразится погода")
        .setPositiveButton("Ок") { dialog, _ -> dialog.cancel() }
        .setCancelable(false)
        .create()
    dialog.show()
}