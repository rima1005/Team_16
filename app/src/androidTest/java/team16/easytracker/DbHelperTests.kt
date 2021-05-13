package team16.easytracker

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import team16.easytracker.database.Contracts.Company as CompanyContract
import team16.easytracker.database.Contracts.Worker as WorkerContract
import team16.easytracker.database.DbHelper
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class DbHelperTests {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** NEVER use setTransactionSuccessful() */


    val COMPANY_DUMMY_NAME = "company 1"
    val WORKER_DUMMY_FIRST_NAME = "Max"
    val WORKER_DUMMY_LAST_NAME = "Mustermann"
    val DUMMY_EMAIL = "test1.test@test.at"
    val DUMMY_PASSWORD = "securePassword"

    val DUMMY_MAC = "FF:FF:FF:FF:FF:FF"
    val DUMMY_BLUETOOTH_DEVICE_NAME = "Test device"
    val DUMMY_WORKER_ID = -1


    @Before
    fun init() {
        DbHelper.writableDatabase.beginTransaction()
    }

    @After
    fun tearDown() {
        DbHelper.writableDatabase.endTransaction()
    }

    private fun insertDummyCompany(): Long {
        val values = ContentValues().apply {
            put(CompanyContract.COL_NAME, COMPANY_DUMMY_NAME)
            put(CompanyContract.COL_ADDRESS_ID, 1)
        }

        return DbHelper.writableDatabase.insert(CompanyContract.TABLE_NAME, null, values)
    }

    private fun insertDummyWorker(): Long {

        val values = ContentValues().apply {
            put(WorkerContract.COL_FIRST_NAME, WORKER_DUMMY_FIRST_NAME)
            put(WorkerContract.COL_LAST_NAME, WORKER_DUMMY_LAST_NAME)
            //put(Worker.COL_DATE_OF_BIRTH, "1998-05-10")
            put(WorkerContract.COL_TITLE, "")
            put(WorkerContract.COL_EMAIL, "max.mustermann@gmail.com")
            put(WorkerContract.COL_PASSWORD, "1235165465")
            put(WorkerContract.COL_PHONE_NUMBER, "+43 9509579554")
            //put(Worker.COL_CREATED_AT, DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
            put(WorkerContract.COL_ADDRESS_ID, 1)
        }

        return DbHelper.writableDatabase.insert(WorkerContract.TABLE_NAME, null, values)
    }

    fun insertDummyBluetoothDevice() {
        DbHelper.saveBluetoothDevice(DUMMY_MAC, DUMMY_BLUETOOTH_DEVICE_NAME, DUMMY_WORKER_ID)
    }

    @Test
    fun testInsertCompany() {
        val newRowId = insertDummyCompany()
        assert(newRowId > 0)
    }

    @Test
    fun testInsertWorker() {
        val newRowId = insertDummyWorker()
        assert(newRowId > 0)
    }

    @Test
    fun testReadCompany() {
        val newRowId = insertDummyCompany()
        val query =
            "SELECT * FROM ${CompanyContract.TABLE_NAME} WHERE ${CompanyContract.COL_ID} = ?"
        val result = DbHelper.readableDatabase.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val companyName = result.getString(result.getColumnIndexOrThrow(CompanyContract.COL_NAME))
        result.close()
        assert(companyName == COMPANY_DUMMY_NAME)
    }

    @Test
    fun testReadWorker() {
        val newRowId = insertDummyWorker()
        val query = "SELECT * FROM ${WorkerContract.TABLE_NAME} WHERE ${WorkerContract.COL_ID} = ?"
        val result = DbHelper.readableDatabase.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val workerFirstName =
            result.getString(result.getColumnIndexOrThrow(WorkerContract.COL_FIRST_NAME))
        val workerLastName =
            result.getString(result.getColumnIndexOrThrow(WorkerContract.COL_LAST_NAME))
        result.close()
        assert(workerFirstName == WORKER_DUMMY_FIRST_NAME)
        assert(workerLastName == WORKER_DUMMY_LAST_NAME)

    }

    //TODO: Test works only on single execution, not in combination with other tests? probably a synchronization/locking problem
    @Test
    fun testExecuteSQLScript() {
        val assetManager = appContext.assets
        val inputStream = assetManager.open("dbtest1.sql")
        val lockedbycurrent = DbHelper.writableDatabase.isDbLockedByCurrentThread()
        val isReadOnly = DbHelper.writableDatabase.isReadOnly()

        DbHelper.executeSQLFile(DbHelper.writableDatabase, inputStream)
        val newRowId = insertDummyCompany()
        val query =
            "SELECT * FROM ${CompanyContract.TABLE_NAME} WHERE ${CompanyContract.COL_ID} = ?"
        val result = DbHelper.readableDatabase.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val columnIndex = result.getColumnIndex("test1")
        result.close()
        assert(columnIndex != -1)
    }

    @Test
    fun testCompanyModel() {
        val dummyAddressId = 69;
        val id = DbHelper.saveCompany(COMPANY_DUMMY_NAME, dummyAddressId)
        val company = DbHelper.loadCompany(id)
        assert(company != null)
        assert(company?.name == COMPANY_DUMMY_NAME)
        assert(company?.addressId == dummyAddressId)
    }

    @Test
    fun testWorkerModel() {
        val now = LocalDateTime.now().withNano(0)
        val id = DbHelper.saveWorker(
            WORKER_DUMMY_FIRST_NAME,
            WORKER_DUMMY_LAST_NAME,
            now.toLocalDate(),
            "",
            DUMMY_EMAIL,
            DUMMY_PASSWORD,
            "",
            now,
            1
        )
        val worker = DbHelper.loadWorker(id)
        assert(worker != null)
        assert(worker?.firstName == WORKER_DUMMY_FIRST_NAME)
        assert(worker?.dateOfBirth == now.toLocalDate())
        assert(worker?.createdAt == now)
    }

    @Test
    fun testAddressModel() {
        val dummyStreet = "TEST"
        val dummyZipCode = "1234"
        val dummyCity = "Graz"
        val id = DbHelper.saveAddress(dummyStreet, dummyZipCode, dummyCity)
        val address = DbHelper.loadAddress(id)
        assert(address != null)
        assert(address?.street == dummyStreet)
        assert(address?.zipCode == dummyZipCode)
        assert(address?.city == dummyCity)
    }

    @Test
    fun testTrackingModel() {
        val dummyName = "TEST"
        val now = LocalDateTime.now()
        val id = DbHelper.saveTracking(dummyName, 1, now, now, "desc", "bluetooth")
        val tracking = DbHelper.loadTracking(id)
        assert(tracking != null)
        assert(tracking?.name == dummyName)
    }

    @Test
    fun testLogin() {
        val id = DbHelper.saveWorker(
            WORKER_DUMMY_FIRST_NAME,
            WORKER_DUMMY_LAST_NAME,
            LocalDate.now(),
            "",
            DUMMY_EMAIL,
            DUMMY_PASSWORD,
            "",
            LocalDateTime.now(),
            1
        )
        val worker = DbHelper.loginWorker(DUMMY_EMAIL, DUMMY_PASSWORD)
        assert(worker != null)
        assert(worker?.getId() == id)
    }

    @Test
    fun testAddWorkerToCompany() {
        val workerId = insertDummyWorker().toInt()
        val companyId = insertDummyCompany().toInt()
        val success = DbHelper.addWorkerToCompany(workerId, companyId, "pos")
        assert(success)
        val worker = DbHelper.loadWorker(workerId)
        assert(worker?.company != null)
        assert(worker?.position != null)
        assert(worker?.admin == false)

        // invalid workerId should throw
        var exceptionThrown = false
        try {
            DbHelper.addWorkerToCompany(-1, companyId, "pos")
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
        }
        assert(exceptionThrown)

        // invalid companyId should throw
        exceptionThrown = false
        try {
            DbHelper.addWorkerToCompany(workerId, -1, "pos")
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
        }
        assert(exceptionThrown)

        // double insert shouldn't break
        val doubleInsertSuccess = DbHelper.addWorkerToCompany(workerId, companyId, "pos")
        assert(doubleInsertSuccess)
    }

    @Test
    fun testCompanyExists() {
        insertDummyCompany()
        assert(DbHelper.companyExists(COMPANY_DUMMY_NAME))
        assert(!DbHelper.companyExists(""))
    }

    @Test
    fun testSetCompanyAdmin() {
        val companyId = insertDummyCompany().toInt()
        val workerId = insertDummyWorker().toInt()
        DbHelper.addWorkerToCompany(workerId, companyId, "pos")

        // admin should be false now (default)

        DbHelper.setCompanyAdmin(workerId, companyId, true)
        val adminWorker = DbHelper.loadWorker(workerId)
        assert(adminWorker?.admin == true)

        DbHelper.setCompanyAdmin(workerId, companyId, false)
        val nonAdminWorker = DbHelper.loadWorker(workerId)
        assert(nonAdminWorker?.admin == false)
    }

    @Test
    fun testDeleteTracking() {
        val dummyName = "TEST"
        val now = LocalDateTime.now()
        val id = DbHelper.saveTracking(dummyName, 1, now, now, "desc", "bluetooth")
        val tracking = DbHelper.loadTracking(id)
        assert(tracking != null)
        assert(tracking?.name == dummyName)
        val success = DbHelper.deleteTracking(id)
        assert(success == 1)
    }

    @Test
    fun testSaveBluetoothDevice() {
        val success = DbHelper.saveBluetoothDevice(DUMMY_MAC, DUMMY_BLUETOOTH_DEVICE_NAME, DUMMY_WORKER_ID)
        assert(success)
        val bluetoothDevice = DbHelper.loadBluetoothDevice(DUMMY_MAC)
        assert(bluetoothDevice != null)
        assert(bluetoothDevice!!.mac == DUMMY_MAC)
        assert(bluetoothDevice.name == DUMMY_BLUETOOTH_DEVICE_NAME)
        assert(bluetoothDevice.workerId == DUMMY_WORKER_ID)
    }

    @Test
    fun testLoadBluetoothDevice() {
        insertDummyBluetoothDevice()
        val bluetoothDevice = DbHelper.loadBluetoothDevice(DUMMY_MAC)
        assert(bluetoothDevice != null)
        assert(bluetoothDevice!!.mac == DUMMY_MAC)
        assert(bluetoothDevice.name == DUMMY_BLUETOOTH_DEVICE_NAME)
        assert(bluetoothDevice.workerId == DUMMY_WORKER_ID)
    }
}