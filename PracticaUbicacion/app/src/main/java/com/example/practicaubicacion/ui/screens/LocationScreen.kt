package com.example.practicaubicacion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationScreen(
    modifier: Modifier = Modifier,
    getLocation: () -> Unit,
    vm: LocationScreenViewModel = LocationScreenViewModel()
) {
    val hasLocationPermissions = ActivityCompat.checkSelfPermission(
        LocalContext.current,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    val uiState by vm.uiState.collectAsStateWithLifecycle()


    val currentLocation =
        LatLng(
            uiState.latitude.toDoubleOrNull() ?: 0.0,
            uiState.longitude.toDoubleOrNull() ?: 0.0
        )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
    }
    LaunchedEffect(uiState.latitude) {
        if (hasLocationPermissions) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(currentLocation, 15f),
                durationMs = 1000
            )
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                getLocation()
            },
            modifier = modifier
        ) {
            Text(text = "Obtener ubicación")
        }
        if (uiState.latitude.isNotEmpty() && uiState.longitude.isNotEmpty()) {
            Text(text = "Latitud: ${uiState.latitude}")
            Text(text = "Longitud: ${uiState.longitude}")
        } else {
            Text(text = "No se ha obtenido la ubicación aún")
        }
        if (hasLocationPermissions) {
            GoogleMap(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                if (uiState.locationSelected) {
                    Marker(
                        state = MarkerState(position = currentLocation),
                        title = "Tu ubicación",
                        snippet = "Ubicación actual"
                    )
                }
                Marker(
                    state = MarkerState(position = LatLng(-17.783151400201643, -63.1820880455232)),
                    title = "Plaza 24 de Septiembre",
                    snippet = "Marcador en la plaza"
                )
            }
        }
    }
}