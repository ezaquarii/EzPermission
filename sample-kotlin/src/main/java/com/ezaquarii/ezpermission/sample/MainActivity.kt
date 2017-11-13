// Copyright (C) EzPermission by Krzysztof Narkiewicz (hello@ezaquarii.com)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.ezaquarii.ezpermission.sample

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ezaquarii.ezpermission.EzPermission
import com.ezaquarii.ezpermission.sample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmField val REQUEST_CAMERA_PERMISSION = 1000;
        @JvmField val REQUEST_AUDIO_PERMISSION = 2000;
    }

    private val mInitCamera = EzPermission.of(this, REQUEST_CAMERA_PERMISSION, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .onGranted{ onCameraGranted() }
            .onRationale{ onCameraRationale() }
            .onDenied{ onCameraDenied() }
            .onDeniedPermanantly{ onCameraDeniedPermanently() }
            .build()

    private val mRecordAudio = EzPermission.of(this, REQUEST_AUDIO_PERMISSION, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .onGranted{ onAudioGranted() }
            .onRationale{ onAudioRationale() }
            .onDenied{ onAudioPermissionDenied() }
            .onDeniedPermanantly{ onAudioPermissionDeniedPermanently() }
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mInitCamera.restoreInstanceState(savedInstanceState)
        mRecordAudio.restoreInstanceState(savedInstanceState)

        mInitCamera.debug = true
        mRecordAudio.debug = true
    }

    override fun onStart() {
        super.onStart()
        mInitCamera.call();
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mInitCamera.saveInstanceState(outState)
        mRecordAudio.saveInstanceState(outState)
    }

    fun onClickedRecordAudio(v: View) {
        mRecordAudio.call()
    }

    fun onClickedTakePicture(v: View) {
        toast("Picture taken")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mInitCamera.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mRecordAudio.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /*************************/
    /*** Audio permissions ***/
    /*************************/

    private fun onAudioGranted() {
        toast("Recording audio")
    }

    private fun onAudioRationale() {
        AlertDialog.Builder(this)
                .setMessage(R.string.audio_rationale)
                .setNegativeButton(android.R.string.cancel, { dialog, i -> mRecordAudio.rejectRationale() })
                .setPositiveButton(android.R.string.ok, {dialog, i -> mRecordAudio.acceptRationale() })
                .show()
    }

    private fun onAudioPermissionDenied() {
        toast("Ok, maybe some other time.")
    }

    private fun onAudioPermissionDeniedPermanently() {
        AlertDialog.Builder(this)
                .setMessage(R.string.audio_rationale_denied_permanently)
                .setNegativeButton("Nope", { dialog, i -> toast("No means no") })
                .setPositiveButton("Settings", {dialog, i -> EzPermission.launchApplicationDetailsSettings(this) })
                .show()
    }

    /**************************/
    /*** Camera permissions ***/
    /**************************/

    /**
     * Camera permission is granted and camera preview should be enabled.
     * Button to take photos is also enabled.
     */
    private fun onCameraGranted() {
        toast("Camera preview enabled")
        // Lousy simulation of camera preview :)
        // Just imagine one yourself here
        camera_rationale.setText(R.string.camera_permission_granted)
        camera_rationale.setOnClickListener(null)
        button_take_picture.isEnabled = true
    }

    /**
     * Normally we'd display a rationale dialog, but in modeless mode rationale
     * is constantly visible, so we do nothing.
     */
    private fun onCameraRationale() {
        log("Camera rationale displayed")
    }

    /**
     * This action is triggered when user actively deny camera permission.
     * Rationale should be left on the screen, but we probably want to encourage
     * user to try again.
     */
    private fun onCameraDenied() {
        log("Camera permission denied - not finally")
        camera_rationale.setText(R.string.camera_rationale_with_retry_instruction)
        button_take_picture.isEnabled = false
        camera_rationale.setOnClickListener {
            mInitCamera.call()
        }
    }

    /**
     * This is called when user denied the permission for good. We still
     * leave the rationale on screen, but enabling permission is more tricky,
     * as it requires drilling through Settings.
     */
    private fun onCameraDeniedPermanently() {
        log("Camera permission denied - permanently!")
        camera_rationale.setText(R.string.camera_permission_denied_permanently)
        camera_rationale.setOnClickListener {
            EzPermission.launchApplicationDetailsSettings(this)
        }
        button_take_picture.isEnabled = false
    }

    /*****************/
    /*** Utilities ***/
    /*****************/

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun log(message: String) {
        Log.d("EzPermission", message)
    }
}
