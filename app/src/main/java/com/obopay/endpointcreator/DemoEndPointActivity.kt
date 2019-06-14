package com.obopay.endpointcreator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Saravana kumar Chinnaraj
 * @version 1.0
 * @Date 11, June, 2019
 * @company Obopay
 */
class DemoEndPointActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.configure_url).setOnClickListener {
            startActivityForResult(
                EndPointCreatorActivity.newInstance(this,
                    ""),
                EndPointCreatorActivity.ENDPOINT_CREATOR_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EndPointCreatorActivity.ENDPOINT_CREATOR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val createdEndPoint = data!!.getStringExtra(EndPointCreatorActivity.CREATED_URL)
                Toast.makeText(this, createdEndPoint, Toast.LENGTH_LONG).show()
            }
        }
    }
}
