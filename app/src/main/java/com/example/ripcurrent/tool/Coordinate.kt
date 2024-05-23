
import android.Manifest
import android.location.Geocoder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.ripcurrent.R
import com.example.ripcurrent.tool.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class Coordinate(
    var lng: Double = 0.0,
    var lat: Double = 0.0,
    var address: String = ""
)



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getCoordinate(): Coordinate {
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val coordinate = remember { mutableStateOf(Coordinate()) }

    // 使用 LaunchedEffect 來處理位置數據
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            locationService.getLastLocation()
            locationService.locationFlow.collect { location ->
                location?.let {

                    coordinate.value= getAddressFromCoordinates(context,it.first, it.second)
                }
            }
        }
    }

    // 如果權限尚未授予，顯示 AlertDialog
    if (!locationPermissionState.status.isGranted) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = {
                    locationPermissionState.launchPermissionRequest()
                }) {
                    Text(stringResource(R.string.request_location_permission))
                }
            },
            title = { Text(stringResource(R.string.location_permission_required)) },
            text = { Text(stringResource(R.string.this_app_requires_location_permission_to_obtain_your_current_location)) }
        )
    }

    return coordinate.value
}
// 逆地理編碼方法
suspend fun getAddressFromCoordinates(context: android.content.Context, latitude: Double, longitude: Double): Coordinate {
    val coordinate= Coordinate()
    coordinate.lat= latitude
    coordinate.lng= longitude
    coordinate.address = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: "地址無法識別"
        } catch (e: Exception) {
            e.printStackTrace()
            "地址無法識別"
        }
    }
    return coordinate
}