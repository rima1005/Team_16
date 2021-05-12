package team16.easytracker

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.lang.StringBuilder

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeactivity)


        MyApplication.updateResources(this)

        val dashboardFragment = DashboardFragment()
        val trackingsFragment = Trackings()
        val companyFragment = CompanyFragment()
        val profileFragment = Profile()

        setCurrentFragment(dashboardFragment)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        val header = navigationView.getHeaderView(0)
        val headerName = header.findViewById<TextView>(R.id.tvNavHeaderName)
        headerName.text = MyApplication.loggedInWorker?.firstName + " " + MyApplication.loggedInWorker?.lastName
        val headerButton = header.findViewById<Button>(R.id.btnNavHeaderProfile)
        headerButton.setOnClickListener {
            setCurrentFragment(profileFragment)
            drawerLayout.closeDrawer(Gravity.LEFT)
        }
        val stringbuilder = StringBuilder()
        stringbuilder.append((MyApplication.loggedInWorker?.firstName?.get(0) ?: ""))
        stringbuilder.append((MyApplication.loggedInWorker?.lastName?.get(0) ?: ""))
        headerButton.text = stringbuilder.toString()
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, topAppBar, R.string.openDrawer, R.string.closeDrawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId)
            {
                R.id.itemDashboard -> setCurrentFragment(dashboardFragment)
                R.id.itemTrackings -> setCurrentFragment(trackingsFragment)
                R.id.itemCreateCompany -> setCurrentFragment(companyFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            menuItem.isChecked = true
            drawerLayout.closeDrawer(Gravity.LEFT)
            true
        }
        /*
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dashboard->setCurrentFragment(dashboardFragment)
                R.id.trackings->setCurrentFragment(trackingsFragment)
                R.id.company->setCurrentFragment(companyFragment)
                R.id.profile->setCurrentFragment(profileFragment)
            }
            true
        }*/

    }

    private fun setCurrentFragment(fragment: Fragment)=
    supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment,fragment)
        commit()
    }


}
