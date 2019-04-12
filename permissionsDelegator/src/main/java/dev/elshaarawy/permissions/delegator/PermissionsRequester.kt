package dev.elshaarawy.permissions.delegator

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * @author Mohamed Elshaarawy on Apr 12, 2019.
 */
abstract class PermissionsRequester(protected val requestCode: Int, private vararg val permissions: String) {

    protected lateinit var onAllGranted: Context.() -> Unit

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

    /**
     * run  [onAllGranted] lambda when [permissions] are all granted
     * */
    abstract fun applyWithPermission(
        onRequestPermissions: (List<String>) -> Unit = {},
        onAllGranted: Context.() -> Unit
    )

    /**
     * This function must be called in  [AppCompatActivity.onRequestPermissionsResult]
     *
     * or [Fragment.onRequestPermissionsResult] methods
     *
     * or [applyWithPermission] #onAllGranted lambda will not be invoked
     * */
    abstract fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onAllDenied: (List<String>) -> Unit = {}
    )
}
