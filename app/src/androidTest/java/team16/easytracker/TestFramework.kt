package team16.easytracker

import android.bluetooth.BluetoothAdapter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Address
import team16.easytracker.model.Company
import team16.easytracker.model.Worker
import java.time.LocalDate
import java.time.LocalDateTime


open class TestFramework {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    var dbHelper = DbHelper.getInstance(context)
    lateinit var dummyWorker : Worker
    lateinit var dummyCompany : Company
    lateinit var dummyAddress : Address
    lateinit var loggedInWorker : Worker


    @Before
    open fun setup()
    {
        MyApplication.discoveryCancelled = true
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
        dbHelper.onOpen(dbHelper.writableDatabase)
        dbHelper.onCreate(dbHelper.writableDatabase)
    }

    @After
    open fun teardown()
    {
        dbHelper.close()
        dbHelper.clearDb("EasyTrackerTest.db")
    }

    open fun insertDummyWorker(firstName: String = "DummyFirstName", lastName: String = "DummyLastName",
                               dateOfBirth: LocalDate = LocalDate.now(),
                               title: String = "DummyTitle",
                               email: String = "dummy@email.com",
                               password: String = "12345678",
                               phoneNumber: String = "43660551122",
                               createdAt: LocalDateTime = LocalDateTime.now().withNano(0),
                               addressId: Int = 1) : Worker
    {
        val workerId = dbHelper.saveWorker(firstName, lastName, dateOfBirth, title, email, password, phoneNumber, createdAt, addressId)
        dummyWorker = dbHelper.loadWorker(workerId)!!
        return dummyWorker
    }

    open fun insertDummyWorkerWithCompanyAdmin(firstName: String = "DummyFirstName", lastName: String = "DummyLastName",
                                               dateOfBirth: LocalDate = LocalDate.now(),
                                               title: String = "DummyTitle",
                                               email: String = "dummy@email.com",
                                               password: String = "12345678",
                                               phoneNumber: String = "43660551122",
                                               createdAt: LocalDateTime = LocalDateTime.now().withNano(0),
                                               addressId: Int = 1) : Worker
    {
        val workerId = dbHelper.saveWorker(firstName, lastName, dateOfBirth, title, email, password, phoneNumber, createdAt, addressId)
        dummyWorker = dbHelper.loadWorker(workerId)!!
        return dummyWorker
    }



    open fun insertDummyCompany(name: String = "DummyCompany", addressId: Int = 1) : Company
    {
        insertDummyAddress()
        val companyId = dbHelper.saveCompany(name, dummyAddress.getId())
        dummyCompany = dbHelper.loadCompany(companyId)!!
        return dummyCompany
    }

    open fun insertDummyAddress(street : String = "DummyStreet 1", zipCode: String = "8010", city: String = "Graz") : Address
    {
        val addressId = dbHelper.saveAddress(street, zipCode, city)
        dummyAddress = dbHelper.loadAddress(addressId)!!
        return dummyAddress
    }

    open fun setupLoggedInWorker() : Worker
    {
        val address = insertDummyAddress()
        dbHelper.saveWorker("LoggedInFirstName", "LoggedInLastName", LocalDate.now(), "DummyTitle", "loggedIn@email.com", "12345678", "43660551122", LocalDateTime.now().withNano(0), address.getId())
        dbHelper.loginWorker("loggedIn@email.com", "12345678")
        loggedInWorker = MyApplication.loggedInWorker!!
        return MyApplication.loggedInWorker!!
    }

    open fun addloggedInWorkerToCompany()
    {
        insertDummyCompany()
        dbHelper.addWorkerToCompany(loggedInWorker.getId(), dummyCompany.getId(), "BOSS")
        dbHelper.setCompanyAdmin(loggedInWorker.getId(), dummyCompany.getId(), true)
        MyApplication.loggedInWorker = dbHelper.loadWorker(MyApplication.loggedInWorker!!.getId())

    }

    open fun insertDummyBluetoothDevice(mac: String = "00:80:41:ae:fd:69",
                                        device: String = "TestDevice")
    {
        dbHelper.saveBluetoothDevice(mac, device, loggedInWorker.getId())
    }

    open fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                        ?: // has no item on such position
                        return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


}