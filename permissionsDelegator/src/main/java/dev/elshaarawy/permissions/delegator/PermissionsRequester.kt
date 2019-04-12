package dev.elshaarawy.permissions.delegator

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

/**
 * @author Mohamed Elshaarawy on Apr 12, 2019.
 */
abstract class PermissionsRequester(protected val requestCode: Int, private vararg val permissions: String) {

    protected lateinit var onAllGranted: () -> Unit

    protected fun Context.guaranteePermissions(): Pair<Boolean, List<String>> {
        val unGrantedPermissions = mutableListOf<String>()

        permissions.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val isGranted = ActivityCompat.checkSelfPermission(this, it) ==
                        PackageManager.PERMISSION_GRANTED
                if (!isGranted)
                    unGrantedPermissions.add(it)
            }
        }

        val isAllGranted = unGrantedPermissions.isEmpty()

        return isAllGranted to unGrantedPermissions
    }

    abstract fun withPermission(
        onRequestPermissions: (List<String>) -> Unit = {},
        onAllGranted: () -> Unit
    )

    abstract fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onAllDenied: (List<String>) -> Unit={}
    )
}
