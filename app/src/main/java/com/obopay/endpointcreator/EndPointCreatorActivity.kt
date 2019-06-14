package com.obopay.endpointcreator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.net.MalformedURLException
import java.net.URL

class EndPointCreatorActivity : AppCompatActivity(), TextWatcher, RadioGroup.OnCheckedChangeListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var domainEditTextView: TextInputEditText
    private lateinit var domainTextInputLayout: TextInputLayout
    private lateinit var protocolRadioButton: RadioGroup
    private lateinit var newEndpointTextView: TextView
    private lateinit var wwwCheckBox: CheckBox

    private lateinit var existingURL: URL

    companion object {
        private const val EXISTING_URL = "_existing_url"
        const val ENDPOINT_CREATOR_REQUEST_CODE = 2001
        const val CREATED_URL = "_created_url"

        fun newInstance(context: Context, existingEndPoint: String): Intent {
            val intent = Intent(context, EndPointCreatorActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXISTING_URL, existingEndPoint)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endpoint_creator)
        title = getString(R.string.str_title)

        domainEditTextView = findViewById(R.id.domain_input_edit_text)
        domainTextInputLayout = findViewById(R.id.domain_input_layout)
        protocolRadioButton = findViewById(R.id.protocol_rg)
        newEndpointTextView = findViewById(R.id.new_url_tv)
        wwwCheckBox = findViewById(R.id.www_cb)

        val url = intent.getStringExtra(EXISTING_URL)
        setExistingURL(url)

        protocolRadioButton.setOnCheckedChangeListener(this)
        wwwCheckBox.setOnCheckedChangeListener(this)
        domainEditTextView.addTextChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            createEndPoint()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createEndPoint() {
        val domain = domainEditTextView.text.toString()
        val validDomainName = Utils.checkDomainName(domain)
        if (!validDomainName.isNullOrEmpty()) {
            domainTextInputLayout.error = validDomainName
        } else {
            domainTextInputLayout.error = null
            Utils.dismissKeyboard(domainEditTextView)

            val bundle = Bundle()
            bundle.putString(CREATED_URL, newEndpointTextView.text.toString())
            val intent = Intent()
            intent.putExtras(bundle)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun showCreatedURL() {
        val domain = domainEditTextView.text.toString()
        var path = ""
        try {
            path = existingURL.path
        } catch (ex: UninitializedPropertyAccessException) {}

        newEndpointTextView.text =
            Utils.createFullURL(
                protocolRadioButton.checkedRadioButtonId == R.id.https_rb,
                wwwCheckBox.isChecked,
                domain,
                path
            )
    }

    override fun afterTextChanged(s: Editable?) {
        showCreatedURL()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        showCreatedURL()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        showCreatedURL()
    }

    private fun setExistingURL(url: String) {
        try {
            existingURL = URL(url)
            updateView(url)
        } catch (ex: MalformedURLException) {
            Toast.makeText(this, "No valid URL found", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateView(url: String) {
        if (Utils.isSecured(existingURL.protocol)) {
            findViewById<RadioButton>(R.id.https_rb).isChecked = true
        } else {
            findViewById<RadioButton>(R.id.http_rb).isChecked = true
        }

        var domain = existingURL.host
        if (Utils.hasWorldWideWeb(domain)) {
            findViewById<CheckBox>(R.id.www_cb).isChecked = true
            domain = Utils.removeWorldWide(domain)
        }
        domainEditTextView.setText(domain)
        newEndpointTextView.text = url
    }
}
