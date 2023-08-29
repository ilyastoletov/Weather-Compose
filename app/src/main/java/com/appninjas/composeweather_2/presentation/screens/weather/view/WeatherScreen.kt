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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.appninjas.composeweather_2.presentation.screens.weather.contract.WeatherContract
import com.appninjas.composeweather_2.presentation.screens.weather.contract.WeatherViewModel
import com.appninjas.composeweather_2.presentation.ui.failure.NetworkFailureScreen
import com.appninjas.composeweather_2.presentation.ui.loading.LoadingScreen
import com.appninjas.composeweather_2.presentation.utils.getActivity
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
                is WeatherContract.Effect.ShowErrorToast -> { Toast.makeText(context, "При загрузке данных произошла ошибка. Проверьте качество подключения к интернету и попробуйте снова", Toast.LENGTH_SHORT).show() }
            }
        }
    }

}

@Composable
private fun WeatherScreenContent(
    state: WeatherContract.State,
    onEvent: (WeatherContract.Event) -> Unit) {

    val context = LocalContext.current
    val geocodeAddress = remember { mutableStateOf("") }
    val location = remember { mutableStateOf<Location?>(null) }

    val onLocationLoaded: (Location) -> Unit = { userLocation ->
         location.value = userLocation
         onEvent(WeatherContract.Event.LoadWeather(lat = userLocation.latitude, lon = userLocation.longitude))
    }

    when(state) {
        is WeatherContract.State.Loading -> { LoadingScreen(); changeUserLocation(context, onLocationLoaded) }
        is WeatherContract.State.WeatherDataLoaded -> Content(state.weather)
        is WeatherContract.State.GeocodeAddressLoaded -> geocodeAddress.value = state.address.address
        is WeatherContract.State.NetworkFailure -> NetworkFailureScreen()
    }

}

@Composable
private fun Content(weather: Weather) {

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Row(modifier = Modifier.padding(top = 25.dp)) {
               Image(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .clickable {
                           /* if (locationState.value != null) {
                                onEvent(
                                    WeatherContract.Event.GeocodeCoordinates(
                                        lat = locationState?.value?.latitude!!,
                                        lon = locationState?.value?.longitude!!
                                    )
                                )
                            }*/
                           // changeUserLocation(context, onLocationLoaded)
                                   },
                    painter = rememberVectorPainter(image = Icons.Default.LocationOn),
                    contentDescription = "choose location button"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = geocodeAddress.value,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontSize = when(geocodeAddress.value.length) {
                        in 0..30 -> 18.sp
                        in 30..50 -> 15.sp
                        in 50..90 -> 13.sp
                        else -> 11.sp
                        },
                    color = Color.Black
                )
            }*/
            /*Image(
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .padding(top = 15.dp),
                painter = painterResource(id = R.drawable.clear_cloudy),
                contentDescription = "weather icon"
            )*/
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
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                color = Color.Black
            )
            Text(
                text = weather.degreesCount.toString() + "°C",
                fontWeight = FontWeight.Light,
                fontSize = 38.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "Прогноз на 12 ч.",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            userScrollEnabled = true
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
        .width(100.dp)
        .height(50.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row() {
            AsyncImage(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weatherObject.iconLink)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = "small weather icon"
            )
            Column() {
                Text(text = weatherObject.partOfDay, fontWeight = FontWeight.Normal, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = weatherObject.temp.toString() + "°C", fontWeight = FontWeight.Thin, fontSize = 14.sp)
            }
        }
    }
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
    val gpsEnabled =  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    Log.d("weae", gpsEnabled.toString())
    return gpsEnabled
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