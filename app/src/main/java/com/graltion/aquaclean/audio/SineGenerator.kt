package com.graltion.aquaclean.audio

import com.graltion.aquaclean.utils.Constants
import kotlin.math.PI
import kotlin.math.sin

object SineGenerator {

    fun generate(
        frequency: Float,
        durationSeconds: Int,
        volume: Float,
        sampleRate: Int = Constants.SAMPLE_RATE
    ): ShortArray {
        val totalSamples = sampleRate * durationSeconds
        val buffer = ShortArray(totalSamples)
        val clampedVolume = volume.coerceIn(0f, 1f)

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            val sample = sin(2.0 * PI * frequency * t) * clampedVolume
            buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }

        return buffer
    }
}
