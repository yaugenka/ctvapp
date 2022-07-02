package com.example.ctv

import android.cultraview.tv.CtvDataProvider
import android.cultraview.tv.CtvNetwork
import android.cultraview.tv.CtvSystem
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.gson.GsonBuilder

class MainActivity : FragmentActivity(),
    PhoneFragment.PhoneFragmentListener, ConnectionFragment.ConnectionFragmentListener,
        SmsFragment.SmsFragmentListener
{
    companion object {
        const val TAG = "MainActivity"
    }
    private val gson = GsonBuilder().create()
    private var subs = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, PhoneFragment(), PhoneFragment.TAG)
                .commit()
        }
    }

    override fun onPhoneInput(phone: String, subs: Boolean) {
        this.subs = subs
        //CtvNetwork не реализован
        if (CtvNetwork.getInstance().networkConnectState != 0) {
            val mac = CtvNetwork.getInstance().wireMac
            val deviceInfo = CtvSystem.getInstance().deviceHardwareInfo
            LogEntry(phone, mac, deviceInfo).let {
                Log.i(TAG, gson.toJson(it))
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, ConnectionFragment(), ConnectionFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onConnect() {
        //пакет com.cultraview.setting отсутствует
/*        startActivity(
            Intent("android.settings.NETWORK_SETTINGS").apply {
                component = ComponentName("com.cultraview.setting", "com.cultraview.setting.NetworkActivity")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity, SmsFragment(), SmsFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun onSmsInput() {
        if (subs) {
            //класс CtvDataProviderService отсутствует, но очевидно имется в ввиду CtvDataProvider
            CtvDataProvider.getInstance().putBoolean("customized", "ChSubscriptionSuccess", subs)
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, HdmiFragment(), HdmiFragment.TAG)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, NosubsFragment(), NosubsFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.run {
            if (backStackEntryCount > 0) {
                popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                (findFragmentByTag(PhoneFragment.TAG) as? PhoneFragment)?.resetInput()
            }
            else
                super.onBackPressed()
        }
    }
}