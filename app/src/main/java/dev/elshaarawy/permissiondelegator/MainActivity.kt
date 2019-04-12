package dev.elshaarawy.permissiondelegator

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.elshaarawy.permissions.delegator.PermissionsDelegate

class MainActivity : AppCompatActivity() {

    private val cameraPermissionsDelegate by PermissionsDelegate(43, Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraPermissionsDelegate.withPermission {
            Toast.makeText(this, "Guaranteed Camera Permission", Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPermissionsDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
