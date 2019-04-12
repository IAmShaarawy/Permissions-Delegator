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