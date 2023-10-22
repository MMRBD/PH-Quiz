package com.mmrbd.quiz.ui.quiz

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.mmrbd.quiz.R

/**
 *
 * @param duration duration of the indicator
 * @param onComplete complete Call back
 */
@Composable
fun LinearProgressTimerIndicator(
    duration: Int,
    onComplete: () -> Unit
) {
    var currentProgress by remember { mutableStateOf(0f) }
    val currentPercentage by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = duration, easing = LinearEasing),
        label = "progress",
        finishedListener = { _ ->
            onComplete()
        }
    )

    LaunchedEffect(key1 = Unit) {
        currentProgress = 1f
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            progress = currentPercentage,
            color = colorResource(R.color.appBGColor)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "${duration / 1000} Second")
    }
}
