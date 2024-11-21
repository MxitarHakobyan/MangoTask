package com.mango.task.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)