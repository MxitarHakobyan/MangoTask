import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mango.task.ui.theme.DarkBackground
import com.mango.task.ui.theme.DarkError
import com.mango.task.ui.theme.DarkPrimary
import com.mango.task.ui.theme.DarkSecondary
import com.mango.task.ui.theme.DarkSurface
import com.mango.task.ui.theme.LightBackground
import com.mango.task.ui.theme.LightError
import com.mango.task.ui.theme.LightPrimary
import com.mango.task.ui.theme.LightSecondary
import com.mango.task.ui.theme.LightSurface
import com.mango.task.ui.theme.typography

private val LightColorPalette = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface,
    error = LightError,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkColorPalette = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    error = DarkError,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)


@Composable
fun MangoTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}