package com.graltion.aquaclean.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.graltion.aquaclean.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioPlayer {

    private var audioTrack: AudioTrack? = null
    private var isPlaying = false

    suspend fun playSine(
        frequency: Float,
        durationSeconds: Int,
        volume: Float
    ) = withContext(Dispatchers.IO) {
        stop()
        val buffer = SineGenerator.generate(frequency, durationSeconds, volume)
        play(buffer)
    }

    suspend fun playSweep() = withContext(Dispatchers.IO) {
        stop()
        val buffer = SweepGenerator.generate()
        play(buffer)
    }

    private fun play(buffer: ShortArray) {
        val minBufferSize = AudioTrack.getMinBufferSize(
            Constants.SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(Constants.SAMPLE_RATE)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack?.play()
        isPlaying = true

        val chunkSize = Constants.SAMPLE_RATE
        var offset = 0

        while (offset < buffer.size && isPlaying) {
            val end = minOf(offset + chunkSize, buffer.size)
            audioTrack?.write(buffer, offset, end - offset)
            offset = end
        }

        stop()
    }

    fun stop() {
        isPlaying = false
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}
