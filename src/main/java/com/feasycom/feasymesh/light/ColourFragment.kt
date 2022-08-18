package com.feasycom.feasymesh.light

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.feasycom.feasymesh.R


import kotlinx.android.synthetic.main.fragment_colour.*


class ColourFragment : Fragment(){

    private val lightActivity : LightActivity by lazy {
        requireActivity() as LightActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_colour, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picker.setOnColorSelectedListener {
            Log.e(TAG, "onViewCreated: $it" )
            var valueOf = Color.valueOf(it)
            Log.e(TAG, "onViewCreated: ${valueOf.alpha()}" )


            val hsv = FloatArray(3)
            Color.colorToHSV(it, hsv)
            Log.e(TAG, "h: ${hsv[0]} s: ${hsv[1]} v: ${hsv[2]}" )
           val hsl = hsv.hsvToHsl()
            Log.e(TAG, "h: ${hsl[0]} s: ${hsl[1]} l: ${hsl[2]}" )
            lightActivity.sendHsl(hsl[0].toDouble(), hsl[1].toDouble(), hsl[2].toDouble())
        }
        picker.showOldCenterColor = false;

        saturationbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                lightActivity.sendHsl(s = seekBar.progress / 100.0)
            }

        })

        valuebar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                lightActivity.sendHsl(l = seekBar.progress / 100.0)
            }

        })


    }

    private fun send(hue: Int){
        // lightActivity.sendHsl(hue, saturationbar.progress / 100.0,valuebar.progress / 100.0)

    }

    companion object{
        private const val TAG = "ColourFragment"
    }

}

/**
 * HSV转换为HSL
 * @param {float} h Hue分量，0~359
 * @param {float} s Saturation分量，0~1
 * @param {float} v Value分量，0~1
 * @returns {object} HSL色值，小数
 */
fun FloatArray.hsvToHsl(): FloatArray{
    val hsl = FloatArray(3)
    hsl[0] = get(0)

    when{
        get(2) == 0F -> {
            hsl[1] = 0f
            hsl[2] = 0f
        }
        get(2)<=1/(2 - get(2)) -> {
            hsl[1] = get(1) / (2 - get(1));
            hsl[2] = (2 * get(2) - get(2) * get(1)) / 2;
        }
        else -> {
            hsl[1] = get(2) * get(1) / (2 - 2 * get(2) + get(2) * get(1));
            hsl[2] = (2 * get(2) - get(2) * get(1)) / 2
        }
    }
    return hsl
}