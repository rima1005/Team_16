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
class DbHelperTests : TestFramework() {


    private val COMPANY_DUMMY_NAME = "company 1"
    private val WORKER_DUMMY_FIRST_NAME = "Max"
    private val WORKER_DUMMY_LAST_NAME = "Mustermann"
    private val DUMMY_EMAIL = "test1.test@test.at"
    private val DUMMY_PASSWORD = "securePassword"

    private val DUMMY_MAC = "FF:FF:FF:FF:FF:FF"
    private val DUMMY_BLUETOOTH_DEVICE_NAME = "Test device"
    private val DUMMY_WORKER_ID = -1

    private fun insertDummyCompany(): Long {
        val values = ContentValues().apply {
            put(CompanyContract.COL_NAME, COMPANY_DUMMY_NAME)
            put(CompanyContract.COL_ADDRESS_ID, 1)
        }

        return dbHelper.writableDatabase.insert(CompanyContract.TABLE_NAME, null, values)
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

        return dbHelper.writableDatabase.insert(WorkerContract.TABLE_NAME, null, values)
    }

    @Override
    override fun insertDummyBluetoothDevice(mac: String, device: String) {
        dbHelper.saveBluetoothDevice(DUMMY_MAC, DUMMY_BLUETOOTH_DEVICE_NAME, DUMMY_WORKER_ID)
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
        val result = dbHelper.readableDatabase.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val companyName = result.getString(result.getColumnIndexOrThrow(CompanyContract.COL_NAME))
        result.close()
        assert(companyName == COMPANY_DUMMY_NAME)
    }

    @Test
    fun testReadWorker() {
        val newRowId = insertDummyWorker()
        val query = "SELECT * FROM ${WorkerContract.TABLE_NAME} WHERE ${WorkerContract.COL_ID} = ?"
        val result = dbHelper.readableDatabase.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val workerFirstName =
            result.getString(result.getColumnIndexOrThrow(WorkerContract.COL_FIRST_NAME))
        val workerLastName =
            result.getString(result.getColumnIndexOrThrow(WorkerContract.COL_LAST_NAME))
        result.close()
        assert(workerFirstName == WORKER_DUMMY_FIRST_NAME)
        assert(workerLastName == WORKER_DUMMY_LAST_NAME)

    }


    @Test
    fun testExecuteSQLScript() {
        val assetManager = context.assets
        val inputStream = assetManager.open("dbtest1.sql")
        dbHelper.executeSQLFile(inputStream, dbHelper.writableDatabase)
        val newRowId = insertDummyCompany()
        val query =
            "SELECT * FROM ${CompanyContract.TABLE_NAME} WHERE ${CompanyContract.COL_ID} = ?"
        val result = dbHelper.readableDatabase.rawQuery(query, arrayOf(newRowId.toString()))
        result.moveToFirst()
        val columnIndex = result.getColumnIndex("test1")
        result.close()
        assert(columnIndex != -1)
    }

    @Test
    fun testCompanyModel() {
        val dummyAddressId = 69
        val id = dbHelper.saveCompany(COMPANY_DUMMY_NAME, dummyAddressId)
        val company = dbHelper.loadCompany(id)
        assert(company != null)
        assert(company?.name == COMPANY_DUMMY_NAME)
        assert(company?.addressId == dummyAddressId)
    }

    @Test
    fun testWorkerModel() {
        val now = LocalDateTime.now().withNano(0)
        val id = dbHelper.saveWorker(
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
        val worker = dbHelper.loadWorker(id)
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
        val id = dbHelper.saveAddress(dummyStreet, dummyZipCode, dummyCity)
        val address = dbHelper.loadAddress(id)
        assert(address != null)
        assert(address?.street == dummyStreet)
        assert(address?.zipCode == dummyZipCode)
        assert(address?.city == dummyCity)
    }

    @Test
    fun testTrackingModel() {
        val dummyName = "TEST"
        val now = LocalDateTime.now()
        val id = dbHelper.saveTracking(dummyName, 1, now, now, "desc", "bluetooth")
        val tracking = dbHelper.loadTracking(id)
        assert(tracking != null)
        assert(tracking?.name == dummyName)
    }

    @Test
    fun testLogin() {
        val id = dbHelper.saveWorker(
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
        val worker = dbHelper.loginWorker(DUMMY_EMAIL, DUMMY_PASSWORD)
        assert(worker != null)
        assert(worker?.getId() == id)
    }

    @Test
    fun testAddWorkerToCompany() {
        val workerId = insertDummyWorker().toInt()
        val companyId = insertDummyCompany().toInt()
        val success = dbHelper.addWorkerToCompany(workerId, companyId, "pos")
        assert(success)
        val worker = dbHelper.loadWorker(workerId)
        assert(worker?.company != null)
        assert(worker?.position != null)
        assert(worker?.admin == false)

        // invalid workerId should throw
        var exceptionThrown = false
        try {
            dbHelper.addWorkerToCompany(-1, companyId, "pos")
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
        }
        assert(exceptionThrown)

        // invalid companyId should throw
        exceptionThrown = false
        try {
            dbHelper.addWorkerToCompany(workerId, -1, "pos")
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
        }
        assert(exceptionThrown)

        // double insert shouldn't break
        val doubleInsertSuccess = dbHelper.addWorkerToCompany(workerId, companyId, "pos")
        assert(doubleInsertSuccess)
    }

    @Test
    fun testCompanyExists() {
        insertDummyCompany()
        assert(dbHelper.companyExists(COMPANY_DUMMY_NAME))
        assert(!dbHelper.companyExists(""))
    }

    @Test
    fun testSetCompanyAdmin() {
        val companyId = insertDummyCompany().toInt()
        val workerId = insertDummyWorker().toInt()
        dbHelper.addWorkerToCompany(workerId, companyId, "pos")

        // admin should be false now (default)

        dbHelper.setCompanyAdmin(workerId, companyId, true)
        val adminWorker = dbHelper.loadWorker(workerId)
        assert(adminWorker?.admin == true)

        dbHelper.setCompanyAdmin(workerId, companyId, false)
        val nonAdminWorker = dbHelper.loadWorker(workerId)
        assert(nonAdminWorker?.admin == false)
    }

    @Test
    fun testDeleteTracking() {
        val dummyName = "TEST"
        val now = LocalDateTime.now()
        val id = dbHelper.saveTracking(dummyName, 1, now, now, "desc", "bluetooth")
        val tracking = dbHelper.loadTracking(id)
        assert(tracking != null)
        assert(tracking?.name == dummyName)
        val success = dbHelper.deleteTracking(id)
        assert(success == 1)
    }

    @Test
    fun testSaveBluetoothDevice() {
        val success = dbHelper.saveBluetoothDevice(DUMMY_MAC, DUMMY_BLUETOOTH_DEVICE_NAME, DUMMY_WORKER_ID)
        assert(success)
        val bluetoothDevice = dbHelper.loadBluetoothDevice(DUMMY_MAC, DUMMY_WORKER_ID)
        assert(bluetoothDevice != null)
        assert(bluetoothDevice!!.mac == DUMMY_MAC)
        assert(bluetoothDevice.name == DUMMY_BLUETOOTH_DEVICE_NAME)
        assert(bluetoothDevice.workerId == DUMMY_WORKER_ID)
    }

    @Test
    fun testLoadBluetoothDevice() {
        insertDummyBluetoothDevice()
        val bluetoothDevice = dbHelper.loadBluetoothDevice(DUMMY_MAC, DUMMY_WORKER_ID)
        assert(bluetoothDevice != null)
        assert(bluetoothDevice!!.mac == DUMMY_MAC)
        assert(bluetoothDevice.name == DUMMY_BLUETOOTH_DEVICE_NAME)
        assert(bluetoothDevice.workerId == DUMMY_WORKER_ID)
    }

    @Test
    fun testLoadBluetoothDevicesForWorker() {
        val macs = arrayOf("FF:FF:FF:FF:FF:FD", "FF:FF:FF:FF:FF:FE", "FF:FF:FF:FF:FF:FF")
        val names = arrayOf("a", "b", "c")

        for (i in macs.indices) {
            dbHelper.saveBluetoothDevice(macs[i], names[i], DUMMY_WORKER_ID)
        }

        val devices = dbHelper.loadBluetoothDevicesForWorker(DUMMY_WORKER_ID)
        assert(devices.size == macs.size)

        for (i in devices.indices) {
            assert(devices[i].mac == macs[i])
            assert(devices[i].name == names[i])
            assert(devices[i].workerId == DUMMY_WORKER_ID)
        }
    }

    @Test
    fun testUpdateBluetoothDevice() {
        insertDummyBluetoothDevice()
        val updatedBTDeviceName = "Updated BT device name"
        dbHelper.updateBluetoothDevice(DUMMY_MAC, updatedBTDeviceName, DUMMY_WORKER_ID)
        val bluetoothDevice = dbHelper.loadBluetoothDevice(DUMMY_MAC, DUMMY_WORKER_ID)
        assert(bluetoothDevice != null)
        assert(bluetoothDevice!!.mac == DUMMY_MAC)
        assert(bluetoothDevice.name == updatedBTDeviceName)
        assert(bluetoothDevice.workerId == DUMMY_WORKER_ID)
    }

    @Test
    fun testDeleteBluetoothDevice()
    {
        insertDummyBluetoothDevice()
        val success = dbHelper.deleteBluetoothDeviceOfWorker(DUMMY_MAC, DUMMY_WORKER_ID)
        assert(success == 1)
    }

}