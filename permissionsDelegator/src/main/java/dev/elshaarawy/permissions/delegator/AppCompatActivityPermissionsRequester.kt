/*
 * Copyright (C) 2019 Mohamed Elshaarawy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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