package com.feasycom.feasymesh.light

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feasycom.feasymesh.R
import com.feasycom.feasymesh.di.Injectable
import kotlinx.android.synthetic.main.fragment_white.*
import me.tankery.lib.circularseekbar.CircularSeekBar
import javax.inject.Inject


class WhiteFragment: Fragment(){

    private val lightActivity : LightActivity by lazy {
        requireActivity() as LightActivity
    }


    // private val mViewModel by viewModels<ModelConfigurationViewModel>()

    // private val mViewModel =  ViewModelProvider(this, mViewModelFactory)[ModelConfigurationViewModel::class.java]


    var lightness: Int = 0
    var temperature: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_white, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        circular_seek_bar.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener{
            override fun onProgressChanged(circularSeekBar: CircularSeekBar?, progress: Float, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar) {
            }

            override fun onStopTrackingTouch(p0: CircularSeekBar) {
                temperature = (800 + 192 * p0.progress).toInt()
                // lightActivity.sendCtlLightTemperature(temperature)
                // send()
                lightActivity.sendCtl(temperature)
            }

        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar) {
                lightness = 0xffff * (p0.progress / 100)
                // lightActivity.sendCtlLightLightness(lightness, temperature)
                // send()
                lightActivity.sendCtl(temperature, lightness)
                // circular_seek_bar.progress = p0.progress.toFloat()
                // send()
                /*lightness = 0xffff * (p0.progress / 100)
                lightActivity.sendCtlLightLightness(lightness, temperature)*/
            }
        })


    }

    companion object{
        private const val TAG = "WhiteFragment"
    }
}


