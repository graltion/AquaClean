package com.graltion.aquaclean.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.graltion.aquaclean.R
import com.graltion.aquaclean.audio.AudioPlayer
import com.graltion.aquaclean.ui.components.WaveAnimation
import com.graltion.aquaclean.utils.CleaningMode
import com.graltion.aquaclean.utils.DeviceType
import com.graltion.aquaclean.utils.getFrequency
import com.graltion.aquaclean.utils.getVolume
import kotlinx.coroutines.launch

@Composable
fun CleaningScreen(
    mode: CleaningMode,
    duration: Int,
    deviceType: DeviceType,
    onCompleted: () -> Unit,
    onStop: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(duration) }
    val audioPlayer = remember { AudioPlayer() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            audioPlayer.playSine(
                frequency = mode.getFrequency(deviceType),
                durationSeconds = duration,
                volume = mode.getVolume(deviceType)
            )
        }

        repeat(duration) {
            kotlinx.coroutines.delay(1000)
            secondsLeft--
        }

        onCompleted()
    }

    DisposableEffect(Unit) {
        onDispose { audioPlayer.stop() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.cleaning_in_progress),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(48.dp))

        WaveAnimation(isPlaying = true)

        Spacer(modifier = Modifier.height(48.dp))

        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60
        Text(
            text = "%02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {
                audioPlayer.stop()
                onStop()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = stringResource(R.string.stop),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
