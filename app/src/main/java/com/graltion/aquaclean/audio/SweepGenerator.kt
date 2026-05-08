package com.graltion.aquaclean.audio

import com.graltion.aquaclean.utils.Constants
import kotlin.math.PI
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin

object SweepGenerator {

    fun generate(
        minFreq: Float = Constants.SWEEP_MIN_FREQ,
        maxFreq: Float = Constants.SWEEP_MAX_FREQ,
        durationSeconds: Int = Constants.SWEEP_DURATION_SECONDS,
        sampleRate: Int = Constants.SAMPLE_RATE
    ): ShortArray {
        val totalSamples = sampleRate * durationSeconds
        val buffer = ShortArray(totalSamples)

        val logMin = ln(minFreq.toDouble())
        val logMax = ln(maxFreq.toDouble())

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            val progress = t / durationSeconds
            val freq = Math.E.pow(logMin + (logMax - logMin) * progress)
            val phase = 2.0 * PI * freq * t
            val sample = sin(phase)
            buffer[i] = (sample * Short.MAX_VALUE * 0.8).toInt().toShort()
        }

        return buffer
    }
}
