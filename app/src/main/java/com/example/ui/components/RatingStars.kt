package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ItlaGoldStar

@Composable
fun RatingStars(
    rating: Double,
    maxStars: Int = 5,
    starSize: Dp = 18.dp,
    tint: Color = ItlaGoldStar,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (i in 1..maxStars) {
            val starValue = i.toDouble()
            when {
                rating >= starValue -> {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Estrella llena",
                        tint = tint,
                        modifier = Modifier.size(starSize)
                    )
                }
                rating >= starValue - 0.5 -> {
                    Icon(
                        imageVector = Icons.Filled.StarHalf,
                        contentDescription = "Media estrella",
                        tint = tint,
                        modifier = Modifier.size(starSize)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = "Estrella vacía",
                        tint = tint.copy(alpha = 0.5f),
                        modifier = Modifier.size(starSize)
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveRatingPicker(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    maxStars: Int = 5,
    starSize: Dp = 36.dp,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating
            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "$i estrellas",
                tint = if (isFilled) ItlaGoldStar else Color.LightGray,
                modifier = Modifier
                    .size(starSize)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onRatingChanged(i.toFloat())
                    }
            )
        }
    }
}
