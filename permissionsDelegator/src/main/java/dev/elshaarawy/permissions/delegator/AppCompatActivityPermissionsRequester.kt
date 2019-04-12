package dev.elshaarawy.permissions.delegator

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

/**
 * @author Mohamed Elshaarawy on Apr 12, 2019.
 */
internal class AppCompatActivityPermissionsRequester(
    private val activity: WeakReference<AppCompatActivity>,
    requestCode: Int,
    vararg permissions: String
) : PermissionsRequester(requestCode, *permissions) {
    override fun withPermission(
        onRequestPermissions: (List<String>) -> Unit,
        onAllGranted: () -> Unit
    ) {
        this.onAllGranted = onAllGranted
        activity
        activity.get()?.run {
            val (isGranted, unGrantedPermissions) = guaranteePermissions()

            if (isGranted) {
                onAllGranted()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onRequestPermissions(unGrantedPermissions)
                requestPermissions(unGrantedPermissions.toTypedArray(), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onAllDenied: (List<String>) -> Unit
    ) {
        activity.get()?.let {
            val (isGranted, unGrantedPermissions) = it.guaranteePermissions()
            if (requestCode == this.requestCode && isGranted) {
                onAllGranted()
            } else if (requestCode == this.requestCode) {
                onAllDenied(unGrantedPermissions)
            }
        }
    }
}