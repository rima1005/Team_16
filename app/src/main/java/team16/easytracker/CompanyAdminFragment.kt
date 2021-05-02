package team16.easytracker

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import java.lang.Exception
import java.time.LocalDateTime


/**
 * A simple [Fragment] subclass.
 * Use the [CompanyAdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompanyAdminFragment : Fragment(R.layout.fragment_company_admin) {


    lateinit var tvErrorEmail: TextView
    lateinit var tvErrorAddWorker: TextView
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
        tvErrorAddWorker = view.findViewById(R.id.tvErrorAddWorker)
        swAdmin = view.findViewById(R.id.swAdmin)
        btnAddWorker = view.findViewById(R.id.btnAddWorker)

        btnAddWorker.setOnClickListener { addWorker() }
    }


    private fun addWorker() {

        resetErrors()

        var errorOccured = false

        val workerEmail = etWorkerEmail.text.toString()
        val emailError = Validator.validateEmail(workerEmail)
        if (emailError != "") {
            errorOccured = true
            tvErrorEmail.text = "Invalid Email Address"
            tvErrorEmail.visibility = View.VISIBLE
        }

        val companyPosition = etPosition.text.toString()
        val positionError = Validator.validatePosition(companyPosition)
        if (positionError != "") {
            errorOccured = true
            tvErrorPosition.text = "Invalid Employee Position"
            tvErrorPosition.visibility = View.VISIBLE
        }

        if(errorOccured)
            return

        val worker = DbHelper.loadWorker(workerEmail)
        if(worker == null){
            tvErrorAddWorker.visibility = View.VISIBLE
            tvErrorAddWorker.text = "Could not add employee"
            return
        }

        val companyId = MyApplication.loggedInWorker?.company?.getId()

        if(companyId != null) {
            var added = false
            try {
                added = DbHelper.addWorkerToCompany(worker.getId(), companyId, companyPosition)
            }
            catch(e: Exception){
                tvErrorAddWorker.visibility = View.VISIBLE
                tvErrorAddWorker.text = "Could not add employee"
            }
            finally {
                if(added){
                    tvErrorAddWorker.visibility = View.VISIBLE
                    tvErrorAddWorker.setTextColor(Color.GREEN)
                    tvErrorAddWorker.text = "Employee has been added successfully"
                }
                else{
                    tvErrorAddWorker.visibility = View.VISIBLE
                    tvErrorAddWorker.text = "Could not add employee"
                }
            }
        }
        // TODO: maybe add an error message if the adding to company doesn't work
    }

    private fun resetErrors() {
        tvErrorEmail.text = ""
        tvErrorEmail.visibility = View.GONE
        tvErrorPosition.text = ""
        tvErrorPosition.visibility = View.GONE
        tvErrorAddWorker.text = ""
        tvErrorAddWorker.visibility = View.GONE
        tvErrorAddWorker.setTextColor(Color.RED)
    }
}