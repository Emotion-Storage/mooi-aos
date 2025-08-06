package com.emotionstorage.tutorial.ui.onBoarding

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.buildHighlightAnnotatedString

private const val ON_BOARDING_STEP_COUNT = 4

@Composable
fun OnBoardingTitle(
    title: String,
    modifier: Modifier = Modifier,
    titleHighlights: List<String> = emptyList(),
    showSteps: Boolean = true,
    currentStep: Int = 0,
    totalStep: Int = ON_BOARDING_STEP_COUNT,
) {
    Box(
        modifier = modifier,
    ) {
        Text(
            style = MooiTheme.typography.head1,
            color = Color.White,
            text = buildHighlightAnnotatedString(
                title,
                titleHighlights,
                SpanStyle(color = MooiTheme.colorScheme.primary)
            ),
            modifier = Modifier.align(Alignment.TopStart)
        )
        if(showSteps) {
            OnBoardingStep(
                currentStep = currentStep, totalStep = totalStep,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun OnBoardingStep(
    modifier: Modifier = Modifier,
    currentStep: Int = 0,
    totalStep: Int = ON_BOARDING_STEP_COUNT,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalStep) { step ->
            OnBoardingStepItem(
                isCurrentStep = step == currentStep,
                isLastStep = step == totalStep - 1,
                modifier = Modifier.padding(start = if (step == 0) 5.dp else 0.dp)
            )
        }
    }
}

@Composable
private fun RowScope.OnBoardingStepItem(
    isCurrentStep: Boolean = false,
    isLastStep: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(shape = RoundedCornerShape(50))
                    .background(if (isCurrentStep) MooiTheme.colorScheme.tertiary else MooiTheme.colorScheme.gray600)

            )
            if (!isLastStep) Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(12.dp)
                    .background(MooiTheme.colorScheme.gray600)
            )
        }
        if (isCurrentStep) Box(
            modifier = Modifier
                .zIndex(-1f)
                .offset(-5.dp)
                .size(20.dp)
                .clip(shape = RoundedCornerShape(50))
                .background(MooiTheme.colorScheme.tertiary.copy(alpha = 0.4f))
                .align(Alignment.CenterStart)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun OnBoardingStepPreview() {
    MooiTheme {
        Column {
            OnBoardingStep(currentStep = 0)
            OnBoardingStep(currentStep = 1)
            OnBoardingStep(currentStep = 2)
            OnBoardingStep(currentStep = 3)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingTitlePreview() {
    MooiTheme {
        OnBoardingTitle(
            title = "어떤 이름으로\n불러드릴까요?",
            currentStep = 0,
            titleHighlights = listOf("어떤", "이름"),
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxWidth()
        )
    }
}