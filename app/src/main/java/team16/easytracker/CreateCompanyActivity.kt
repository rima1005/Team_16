package team16.easytracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import team16.easytracker.utils.Validator

class CreateCompanyActivity : AppCompatActivity(){
    lateinit var etCompanyName: EditText
    lateinit var etStreet: EditText
    lateinit var etZipCode: EditText
    lateinit var etCity: EditText
    lateinit var etPosition: EditText
    lateinit var btnCreateCompany: Button

    lateinit var tvErrorCompanyName: TextView
    lateinit var tvErrorCompanyPosition: TextView
    lateinit var tvErrorZipCode : TextView
    lateinit var tvErrorStreet : TextView
    lateinit var tvErrorCity : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createcompanyactivity)

        etCompanyName = findViewById(R.id.etCompanyName)
        etStreet = findViewById(R.id.etStreet)
        etZipCode = findViewById(R.id.etPostCode)
        etCity = findViewById(R.id.etCity)
        etPosition = findViewById(R.id.etCompanyPosition)
        btnCreateCompany = findViewById(R.id.btnCreateCompany)

        tvErrorCompanyName = findViewById(R.id.tvErrorCompanyName)
        tvErrorCompanyPosition = findViewById(R.id.tvErrorCompanyPosition)
        tvErrorZipCode = findViewById(R.id.tvErrorPostCode)
        tvErrorStreet = findViewById(R.id.tvErrorStreet)
        tvErrorCity = findViewById(R.id.tvErrorCity)


        btnCreateCompany.setOnClickListener { createCompany() }


    }

    fun createCompany(){

        resetErrorMessages()

        val errorCompanyName = Validator.validateCompanyName(etCompanyName.text.toString())
        if (errorCompanyName != "")
        {
            tvErrorCompanyName.text = errorCompanyName
            tvErrorCompanyName.visibility = View.VISIBLE
        }

        val errorPosition = Validator.validatePosition(etPosition.text.toString())
        if (errorPosition != "")
        {
            tvErrorCompanyPosition.text = errorPosition
            tvErrorCompanyPosition.visibility = View.VISIBLE
        }

        val errorZipCode = Validator.validatePostCode(etZipCode.text.toString())
        if (errorZipCode != "")
        {
            tvErrorZipCode.text = errorZipCode
            tvErrorZipCode.visibility = View.VISIBLE
        }

        val errorCity = Validator.validateCity(etCity.text.toString())
        if (errorCity != "")
        {
            tvErrorCity.text = errorCity
            tvErrorCity.visibility = View.VISIBLE
        }

        val errorStreet = Validator.validateStreet(etStreet.text.toString())
        if (errorStreet != "")
        {
            tvErrorStreet.text = errorStreet
            tvErrorStreet.visibility = View.VISIBLE
        }
    }

    fun resetErrorMessages() {
        tvErrorCompanyPosition.text = ""
        tvErrorCompanyPosition.visibility = View.GONE

        tvErrorCity.text = ""
        tvErrorCity.visibility = View.GONE

        tvErrorStreet.text = ""
        tvErrorStreet.visibility = View.GONE

        tvErrorZipCode.text = ""
        tvErrorZipCode.visibility = View.GONE

        tvErrorCompanyName.text = ""
        tvErrorCompanyName.visibility = View.GONE
    }
}