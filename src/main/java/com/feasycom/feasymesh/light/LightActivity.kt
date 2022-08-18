package com.feasycom.feasymesh.light

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.feasycom.feasymesh.MainActivity
import com.feasycom.feasymesh.R
import com.feasycom.feasymesh.di.Injectable
import com.feasycom.feasymesh.library.ApplicationKey
import com.feasycom.feasymesh.library.transport.Element
import com.feasycom.feasymesh.library.transport.LightCtlSetUnacknowledged
import com.feasycom.feasymesh.library.transport.LightHslSetUnacknowledged
import com.feasycom.feasymesh.utils.Utils
import com.feasycom.feasymesh.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_light.*
import java.util.*
import javax.inject.Inject

class LightActivity: AppCompatActivity(), Injectable {

    companion object{
        private const val TAG = "LightActivity"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private val mViewModel by lazy {
        ViewModelProvider(this, mViewModelFactory)[SharedViewModel::class.java]
    }
    /*val mViewModel by lazy {
        Log.e(TAG, "                                      ${mViewModelFactory ?: "null"}" )
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light)

        setSupportActionBar(toolbar)
        // noinspection ConstantConditions
        // supportActionBar!!.setTitle(R.string.app_name)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        view_pager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = 2
            override fun createFragment(position: Int): Fragment =
                    when(position){
                        0 -> WhiteFragment()
                        1 -> ColourFragment()
                        else -> WhiteFragment()
                    }
        }

        view_pager.isUserInputEnabled = false  //true:滑动,false:禁止滑动

        TabLayoutMediator(tab_layout, view_pager){ tab, position ->
            when(position){
                0 -> tab.text = "White"
                1 -> tab.text = "Colour"
            }
        }.attach()


        /*var list = FloatArray(3)
        Color.RGBToHSV(255,0,255, list)
        list.hsvToHsl()*/
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isConnectedToNetwork = mViewModel.isConnectedToProxy.value
        if (isConnectedToNetwork != null && isConnectedToNetwork) {
            menuInflater.inflate(R.menu.disconnect, menu)
        } else {
            menuInflater.inflate(R.menu.connect, menu)
        }
        return true
    }

    fun sendCtl(lightTemperature: Int, lightLightness: Int = 0xFFFF){
        val element: Element? = mViewModel.selectedElement?.value
        if (element != null) {
            val model = mViewModel.selectedModel?.value
            if (model != null) {
                val appKeyIndex = model.boundAppKeyIndexes[0]

                val appKey: ApplicationKey = mViewModel.networkLiveData?.meshNetwork?.getAppKey(appKeyIndex)!!
                val node = mViewModel.selectedMeshNode.value
                if (node != null) {
                    val tid = Random().nextInt()
                    val message = LightCtlSetUnacknowledged(appKey,  lightLightness, lightTemperature , 0 ,tid)
                    mViewModel.meshManagerApi.createMeshPdu(node.unicastAddress, message)
                }
            }
        }
    }

    fun sendHsl(h: Double = 0.0, s: Double = 0.0, l: Double = 0.0){

        val element: Element? = mViewModel.selectedElement?.value
        if (element != null) {
            val model = mViewModel.selectedModel?.value
            if (model != null) {
                val appKeyIndex = model.boundAppKeyIndexes[0]
                val appKey: ApplicationKey = mViewModel.networkLiveData?.meshNetwork?.getAppKey(appKeyIndex)!!
                val node = mViewModel.selectedMeshNode.value
                if (node != null) {
                    val tid = Random().nextInt()
                    val message = LightHslSetUnacknowledged(appKey, (l * 0x7FFF).toInt(), (h/360 * 0xFFFF).toInt() , (s * 0xFFFF).toInt() ,tid)
                    mViewModel.meshManagerApi.createMeshPdu(node.unicastAddress, message)
                }
            }
        }
    }

/*    fun sendH(){
        val element: Element? = mViewModel.selectedElement?.value
        if (element != null) {
            val model = mViewModel.selectedModel?.value
            if (model != null) {
                val appKeyIndex = model.boundAppKeyIndexes[0]
                val appKey: ApplicationKey = mViewModel.networkLiveData?.meshNetwork?.getAppKey(appKeyIndex)!!
                val node = mViewModel.selectedMeshNode.value
                if (node != null) {
                    val tid = Random().nextInt()
                    val message = LightHslSetUnacknowledged(appKey,  (lightness * 0x7FFF),(hue * 0xFFFF) , (saturation * 0xFFFF) ,tid)
                    mViewModel.meshManagerApi.createMeshPdu(node.unicastAddress, message)
                }
            }
        }
    }*/


    /*fun sendCtlLightLightness(lightLightness: Int, lightTemperature: Int){
        val element: Element? = mViewModel.selectedElement?.value
        if (element != null) {
            val model = mViewModel.selectedModel?.value
            if (model != null) {
                val appKeyIndex = model.boundAppKeyIndexes[0]
                val appKey: ApplicationKey = mViewModel.networkLiveData?.meshNetwork?.getAppKey(appKeyIndex)!!
                val node = mViewModel.selectedMeshNode.value
                if (node != null) {
                    val tid = Random().nextInt()
                    Log.e(TAG, "sendCtlLightLightness: 发送" )
                    val message = LightCtlSetUnacknowledged(appKey,  lightLightness, lightTemperature , 0 ,tid)
                    mViewModel.meshManagerApi.createMeshPdu(node.unicastAddress, message)
                }
            }
        }
    }*/



    /*fun sendCtlLightTemperature(lightTemperature: Int){
        val element: Element? = mViewModel.selectedElement?.value
        if (element != null) {
            val model = mViewModel.selectedModel?.value
            if (model != null) {
                val appKeyIndex = model.boundAppKeyIndexes[0]
                val appKey: ApplicationKey = mViewModel.networkLiveData?.meshNetwork?.getAppKey(appKeyIndex)!!
                val node = mViewModel.selectedMeshNode.value
                if (node != null) {
                    val tid = Random().nextInt()
                    val message = LightCtlSetUnacknowledged(appKey,  0xFFFF, lightTemperature , 0 ,tid)
                    mViewModel.meshManagerApi.createMeshPdu(node.unicastAddress, message)
                }
            }
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_connect -> {
                mViewModel.navigateToScannerActivity(this, false, Utils.CONNECT_TO_NETWORK, false)
                true
            }
            R.id.action_disconnect -> {
                mViewModel.disconnect()
                true
            }
            else -> false
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return true
    }
}


