package dev.elshaarawy.permissions.delegator

import android.content.Context
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
    override fun applyWithPermission(
        onRequestPermissions: (List<String>) -> Unit,
        onAllGranted: Context.() -> Unit
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
                it.onAllGranted()
            } else if (requestCode == this.requestCode) {
                onAllDenied(unGrantedPermissions)
            }
        }
    }
}