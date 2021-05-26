package team16.easytracker

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import team16.easytracker.database.DbHelper
import team16.easytracker.model.Address
import team16.easytracker.model.Company
import team16.easytracker.model.Worker
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.log

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
        var workerId = dbHelper.saveWorker(firstName, lastName, dateOfBirth, title, email, password, phoneNumber, createdAt, addressId)
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
        var workerId = dbHelper.saveWorker(firstName, lastName, dateOfBirth, title, email, password, phoneNumber, createdAt, addressId)
        dummyWorker = dbHelper.loadWorker(workerId)!!
        return dummyWorker
    }



    open fun insertDummyCompany(name: String = "DummyCompany", addressId: Int = 1) : Company
    {
        var companyId = dbHelper.saveCompany(name, addressId)
        dummyCompany = dbHelper.loadCompany(companyId)!!
        return dummyCompany
    }

    open fun insertDummyAddress(street : String = "DummyStreet", zipCode: String = "8010", city: String = "Graz") : Address
    {
        var addressId = dbHelper.saveAddress(street, zipCode, city)
        dummyAddress = dbHelper.loadAddress(addressId)!!
        return dummyAddress
    }

    open fun setupLoggedInWorker() : Worker
    {
        //var address = insertDummyAddress()
        dbHelper.saveWorker("LoggedInFirstName", "LoggedInLastName", LocalDate.now(), "DummyTitle", "loggedIn@email.com", "12345678", "43660551122", LocalDateTime.now().withNano(0), 1)
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

    open fun insertDummyBluetoothDevice()
    {
        dbHelper.saveBluetoothDevice("00:80:41:ae:fd:69", "TestDevice", loggedInWorker.getId())
    }
}