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

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * @author Mohamed Elshaarawy on Apr 12, 2019.
 */
class PermissionsDelegate private constructor(private val requestCode: Int, private vararg val permissions: String) {
    private lateinit var instance: PermissionsRequester

    companion object {
        operator fun invoke(requestCode: Int, vararg permissions: String): PermissionsDelegate {
            return PermissionsDelegate(requestCode, *permissions)
        }
    }

    operator fun getValue(thisRef: Any, property: KProperty<*>): PermissionsRequester {
        return if (::instance.isInitialized) instance else
            when (thisRef) {
                is Fragment -> FragmentPermissionsRequester(WeakReference(thisRef), requestCode, *permissions)
                is AppCompatActivity -> AppCompatActivityPermissionsRequester(
                    WeakReference(thisRef), requestCode, *permissions
                )
                else -> throw TypeCastException("Can't cast $thisRef to any thing because it is not supported")
            }.also { instance = it }
    }
}