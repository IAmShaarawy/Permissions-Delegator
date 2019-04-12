# Permissions-Delegator

```kotlin
class MainActivity : AppCompatActivity() {

    private val cameraPermissionsDelegate by PermissionsDelegate(43, Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraPermissionsDelegate.applyWithPermission {
            Toast.makeText(this, "Guaranteed Camera Permission", Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPermissionsDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
```

License
=======
    Copyright (C) 2019 Mohamed Elshaarawy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
