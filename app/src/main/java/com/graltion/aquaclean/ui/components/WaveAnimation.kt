package com.graltion.aquaclean.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WaveAnimation(
    modifier: Modifier = Modifier,
    waveColor: Color = MaterialTheme.colorScheme.primary,
    isPlaying: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        val path = Path()
        val amplitude = if (isPlaying) height * 0.35f else height * 0.05f
        val frequency = 2.5f

        path.moveTo(0f, centerY)

        for (x in 0..width.toInt()) {
            val angle = (x / width) * frequency * 2 * PI + phase
            val y = centerY + amplitude * sin(angle).toFloat()
            if (x == 0) path.moveTo(x.toFloat(), y)
            else path.lineTo(x.toFloat(), y)
        }

        drawPath(
            path = path,
            color = waveColor,
            style = Stroke(width = 3.dp.toPx())
        )

        val path2 = Path()
        for (x in 0..width.toInt()) {
            val angle = (x / width) * frequency * 2 * PI + phase + PI / 3
            val y = centerY + (amplitude * 0.5f) * sin(angle).toFloat()
            if (x == 0) path2.moveTo(x.toFloat(), y)
            else path2.lineTo(x.toFloat(), y)
        }

        drawPath(
            path = path2,
            color = waveColor.copy(alpha = 0.4f),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
