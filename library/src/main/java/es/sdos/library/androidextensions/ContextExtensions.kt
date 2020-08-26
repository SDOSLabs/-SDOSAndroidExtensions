package es.sdos.library.androidextensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.model.LatLng

private const val GOOGLE_MAPS_URL = "http://maps.google.com/maps?q="
private const val ACTION_SEND_TYPE = "text/plain"
private const val PREFIX_MAIL_TO = "mailto:"
private const val PREFIX_PHONE = "tel:"

fun Context.startUrlActionView(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun Context.startGoogleMapsByLatLng(latLng: LatLng,
                                    googleMapsUrl: String? = GOOGLE_MAPS_URL
) {
    startUrlActionView(googleMapsUrl + latLng.toGoogleMapsFormat())
}

fun Context.startCallActionView(phone: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(PREFIX_PHONE + phone)))
}

fun Context.startActionSendView(
    from: String? = null,
    to: String? = null,
    subject: String? = null,
    message: String? = null,
    titleChooser: String?,
    sendType: String? = ACTION_SEND_TYPE
    ) {
    val action = if (to != null) {
        Intent.ACTION_SENDTO
    } else {
        Intent.ACTION_SEND
    }
    startActivity(Intent.createChooser(Intent(action).apply {
        type = sendType
        from?.let {
            putExtra(Intent.EXTRA_EMAIL, it)
        }
        to?.let {
            setData(Uri.parse(PREFIX_MAIL_TO + it))
        }
        subject?.let {
            putExtra(Intent.EXTRA_SUBJECT, it)
        }
        message?.let {
            putExtra(Intent.EXTRA_TEXT, it)
        }
    }, titleChooser))
}
