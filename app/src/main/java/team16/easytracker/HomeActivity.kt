package team16.easytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dashboard->setCurrentFragment(dashboardFragment)
                R.id.trackings->setCurrentFragment(trackingsFragment)
                R.id.company->setCurrentFragment(companyFragment)
                R.id.profile->setCurrentFragment(profileFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
    supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment,fragment)
        commit()
    }
}