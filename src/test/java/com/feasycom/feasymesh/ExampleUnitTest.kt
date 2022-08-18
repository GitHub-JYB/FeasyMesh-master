/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.feasycom.feasymesh

import android.os.Handler
import com.feasycom.feasymesh.library.utils.MeshParserUtils
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }

    @Test
    fun test() {
        val str = "$001-199E$"
        val substring = str.substring(5, 9)
        println(substring)
    }

    @Test
    fun test1() {
        var a = 0xFF00
        println(a)
    }

    private val time = 1000
    private var timer: Timer? = null

    private var timerTask: TimerTask? = null
    private val mHandler: Handler? = null
    var sequenceNumber: Int = 0

    @Test
    open fun testSequence() {

        var a = "840397C9370E2F"
        var toByteArray = MeshParserUtils.toByteArray(a)
        toByteArray.forEach {
            println(it.toInt())
        }
    }

    @Test
    fun c(){
        println(autoGenericCode("10", 32))
    }

    @Test
    fun bbb(){
        val num = 10
        println(String.format("%0" + num + "d", 1))
    }


    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    private fun autoGenericCode(code: String, num: Int): String? {
        var result = ""
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", code.toInt())
        return result
    }
}