package com.graltion.aquaclean.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.graltion.aquaclean.R
import com.graltion.aquaclean.ui.components.WaveAnimation
import com.graltion.aquaclean.utils.CleaningMode
import com.graltion.aquaclean.utils.DeviceType
import com.graltion.aquaclean.utils.getDefaultDuration
import com.graltion.aquaclean.utils.getDurationRange

@Composable
fun MainScreen(
    onStartCleaning: (CleaningMode, Int, DeviceType) -> Unit
) {
    var selectedDevice by remember { mutableStateOf(DeviceType.PHONE) }
    var selectedMode by remember { mutableStateOf(CleaningMode.NORMAL) }
    var duration by remember { mutableFloatStateOf(selectedMode.getDefaultDuration().toFloat()) }
    var showWarning by remember { mutableStateOf(false) }

    val durationRange = selectedMode.getDurationRange()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DeviceType.entries.forEach { device ->
                FilterChip(
                    selected = selectedDevice == device,
                    onClick = { selectedDevice = device },
                    label = {
                        Text(
                            text = stringResource(
                                if (device == DeviceType.PHONE) R.string.device_phone
                                else R.string.device_headphones
                            ),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        WaveAnimation(isPlaying = false)

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.select_mode),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CleaningMode.entries.forEach { mode ->
                FilterChip(
                    selected = selectedMode == mode,
                    onClick = {
                        selectedMode = mode
                        duration = mode.getDefaultDuration().toFloat()
                    },
                    label = {
                        Text(
                            text = stringResource(
                                when (mode) {
                                    CleaningMode.LIGHT -> R.string.mode_light
                                    CleaningMode.NORMAL -> R.string.mode_normal
                                    CleaningMode.DEEP -> R.string.mode_deep
                                }
                            ),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.duration),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${duration.toInt()} ${stringResource(R.string.seconds)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Slider(
            value = duration,
            onValueChange = { duration = it },
            valueRange = durationRange.first.toFloat()..durationRange.last.toFloat(),
            steps = durationRange.last - durationRange.first - 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showWarning = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.start),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showWarning) {
        WarningDialog(
            onConfirm = {
                showWarning = false
                onStartCleaning(selectedMode, duration.toInt(), selectedDevice)
            },
            onDismiss = { showWarning = false }
        )
    }
}
