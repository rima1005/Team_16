package team16.easytracker

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator

class CompanyAdminFragment : Fragment(R.layout.fragment_company_admin) {


    lateinit var tvErrorEmail: TextView
    lateinit var tvErrorPosition: TextView

    lateinit var etPosition: EditText
    lateinit var etWorkerEmail: EditText

    lateinit var swAdmin: SwitchCompat

    lateinit var btnAddWorker: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etWorkerEmail = view.findViewById(R.id.etEmail)
        tvErrorEmail = view.findViewById(R.id.tvErrorEmail)
        etPosition = view.findViewById(R.id.etCompanyPosition)
        tvErrorPosition = view.findViewById(R.id.tvErrorCompanyPosition)
        swAdmin = view.findViewById(R.id.swAdmin)
        btnAddWorker = view.findViewById(R.id.btnAddWorker)

        btnAddWorker.setOnClickListener { addWorker() }
    }


    private fun addWorker() {

        resetErrors()

        var errorOccured = false

        val workerEmail = etWorkerEmail.text.toString()
        val emailError = Validator.validateEmail(workerEmail, resources)
        if (emailError != "") {
            errorOccured = true
            tvErrorEmail.text = emailError
            tvErrorEmail.visibility = View.VISIBLE
        }

        val companyPosition = etPosition.text.toString()
        val positionError = Validator.validatePosition(companyPosition, resources)
        if (positionError != "") {
            errorOccured = true
            tvErrorPosition.text = positionError
            tvErrorPosition.visibility = View.VISIBLE
        }

        if(errorOccured)
            return

        val worker = DbHelper.getInstance().loadWorker(workerEmail)
        if(worker == null){
            Snackbar.make(view!!,getString(R.string.error_adding_employee), Snackbar.LENGTH_SHORT).show()
            return
        }

        val companyId = MyApplication.loggedInWorker?.company?.getId()

        if(companyId != null) {
            var added = false
            try {
                added = DbHelper.getInstance().addWorkerToCompany(worker.getId(), companyId, companyPosition)
            }
            catch(e: Exception){
                Snackbar.make(view!!,getString(R.string.error_adding_employee), Snackbar.LENGTH_SHORT).show()
            }
            finally {
                if(added){
                    Snackbar.make(view!!,getString(R.string.success_adding_employee), Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(view!!,getString(R.string.error_adding_employee), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetErrors() {
        tvErrorEmail.text = ""
        tvErrorEmail.visibility = View.GONE
        tvErrorPosition.text = ""
        tvErrorPosition.visibility = View.GONE
    }
}