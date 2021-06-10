package team16.easytracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {

    val dashboardFragment = DashboardFragment()
    val trackingsFragment = TrackingsFragment()
    val bluetoothDevicesFragment = BluetoothDevicesFragment()
    val companyFragment = CompanyFragment()
    val profileFragment = ProfileFragment()
    val companyAdminFragment = CompanyAdminFragment()
    val createCompanyFragment = CreateCompanyFragment()

    lateinit var currentFragment: Fragment

    lateinit var navigationView : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeactivity)

        MyApplication.updateResources(this)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, dashboardFragment, "TAG_DASHBOARD")
            commit()
        }
        currentFragment = dashboardFragment

        val stringbuilder = StringBuilder()
        stringbuilder.append((MyApplication.loggedInWorker?.firstName?.get(0) ?: ""))
        stringbuilder.append((MyApplication.loggedInWorker?.lastName?.get(0) ?: ""))

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)

        val header = navigationView.getHeaderView(0)

        val headerName = header.findViewById<TextView>(R.id.tvNavHeaderName)
        headerName.text = MyApplication.loggedInWorker?.firstName + " " + MyApplication.loggedInWorker?.lastName

        val headerButton = header.findViewById<Button>(R.id.btnNavHeaderProfile)
        headerButton.text = stringbuilder.toString()
        headerButton.setOnClickListener {
            setCurrentFragment(profileFragment)
            navigationView.menu.forEach { menuItem -> menuItem.isChecked = false }
            headerName.setTextColor(Color.parseColor("#168AAD"))
            drawerLayout.closeDrawer(Gravity.LEFT)
        }

        setSupportActionBar(topAppBar)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            topAppBar,
            R.string.openDrawer,
            R.string.closeDrawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            headerName.setTextColor(Color.BLACK)
            when (menuItem.itemId) {
                R.id.itemDashboard -> setCurrentFragment(dashboardFragment, "TAG_DASHBOARD")
                R.id.itemTrackings -> setCurrentFragment(trackingsFragment, "TAG_TRACKINGS")
                R.id.itemBluetoothDevices -> setCurrentFragment(bluetoothDevicesFragment, "TAG_BLUETOOTHDEVICES")
                R.id.itemCreateCompany -> setCurrentFragment(createCompanyFragment, "TAG_CREATECOMPANY", false)
                R.id.itemOverview -> setCurrentFragment(companyFragment, "TAG_OVERVIEW")
                R.id.itemAddEmployee -> setCurrentFragment(companyAdminFragment, "TAG_ADDEMPLOYEE")
                R.id.itemLogout -> logout()
            }
            menuItem.isChecked = true
            drawerLayout.closeDrawer(Gravity.LEFT)
            true
        }

        val menuItems = navigationView.menu
        if (MyApplication.loggedInWorker?.company == null) {
            menuItems.findItem(R.id.itemAddEmployee).isVisible = false
            menuItems.findItem(R.id.itemOverview).isVisible = false
        } else {
            menuItems.findItem(R.id.itemAddEmployee).isVisible = MyApplication.loggedInWorker?.admin!!
            menuItems.findItem(R.id.itemCreateCompany).isVisible = false
        }
        menuItems.findItem(R.id.itemDashboard).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MyApplication.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return currentFragment.onOptionsItemSelected(item)
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String = "", addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            if(addToBackStack)
                addToBackStack(tag)
            commit()
        }
        currentFragment = fragment
    }

    private fun logout() {
        MyApplication.loggedInWorker = null
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    var doubleBackToExit = false
    @Suppress("DEPRECATION") //Handler should not be used anymore
    @Override
    override fun onBackPressed() {

        if(supportFragmentManager.backStackEntryCount == 0)
        {
            if (doubleBackToExit)
            {
                super.onBackPressed()
                return
            }
            doubleBackToExit = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExit = false }, 2000)
        }
        else
        {
            val header = navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.tvNavHeaderName).setTextColor(Color.BLACK)
            if(dashboardFragment.isVisible)
                navigationView.menu.findItem(R.id.itemDashboard).isChecked = true
            if(trackingsFragment.isVisible)
                navigationView.menu.findItem(R.id.itemTrackings).isChecked = true
            if(companyFragment.isVisible)
                navigationView.menu.findItem(R.id.itemOverview).isChecked = true
            if(companyAdminFragment.isVisible)
                navigationView.menu.findItem(R.id.itemAddEmployee).isChecked = true
            if(createCompanyFragment.isVisible)
                navigationView.menu.findItem(R.id.itemCreateCompany).isChecked = true
            if(profileFragment.isVisible)
            {
                navigationView.menu.forEach { menuItem -> menuItem.isChecked = false }
                header.findViewById<TextView>(R.id.tvNavHeaderName).setTextColor(Color.parseColor(R.color.colorPrimary.toString()))
            }
            super.onBackPressed()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentFragment.onActivityResult(requestCode, resultCode, data)
    }
}
