package es.sdos.library.androidextensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

private const val SEPARATOR = ","

fun Location.toLatLng() = LatLng(latitude, longitude)

fun LatLng.toGoogleMapsFormat(separator: String ? = SEPARATOR) =
    latitude.toString() + separator + longitude.toString()
