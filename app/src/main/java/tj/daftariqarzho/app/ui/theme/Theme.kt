package tj.daftariqarzho.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = DaftarColors.PrimaryLight,
    onPrimary = DaftarColors.OnPrimaryLight,
    primaryContainer = DaftarColors.PrimaryContainerLight,
    onPrimaryContainer = DaftarColors.OnPrimaryContainerLight,
    secondary = DaftarColors.SecondaryLight,
    tertiary = DaftarColors.TertiaryLight,
    background = DaftarColors.BackgroundLight,
    onBackground = DaftarColors.OnBackgroundLight,
    surface = DaftarColors.SurfaceLight,
    surfaceVariant = DaftarColors.SurfaceVariantLight,
    onSurfaceVariant = DaftarColors.OnSurfaceVariantLight,
    outline = DaftarColors.OutlineLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = DaftarColors.PrimaryDark,
    onPrimary = DaftarColors.OnPrimaryDark,
    primaryContainer = DaftarColors.PrimaryContainerDark,
    onPrimaryContainer = DaftarColors.OnPrimaryContainerDark,
    secondary = DaftarColors.SecondaryDark,
    tertiary = DaftarColors.TertiaryDark,
    background = DaftarColors.BackgroundDark,
    onBackground = DaftarColors.OnBackgroundDark,
    surface = DaftarColors.SurfaceDark,
    surfaceVariant = DaftarColors.SurfaceVariantDark,
    onSurfaceVariant = DaftarColors.OnSurfaceVariantDark,
    outline = DaftarColors.OutlineDark,
)

@Immutable
data class DebtColors(
    val given: Color,
    val received: Color,
    val overdueBg: Color,
)

val LocalDebtColors = staticCompositionLocalOf {
    DebtColors(Color.Unspecified, Color.Unspecified, Color.Unspecified)
}

@Composable
fun DaftariQarzhoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val debtColors = if (darkTheme) {
        DebtColors(
            given = DaftarColors.DebtGivenDark,
            received = DaftarColors.DebtReceivedDark,
            overdueBg = DaftarColors.OverdueBgDark,
        )
    } else {
        DebtColors(
            given = DaftarColors.DebtGivenLight,
            received = DaftarColors.DebtReceivedLight,
            overdueBg = DaftarColors.OverdueBgLight,
        )
    }

    CompositionLocalProvider(
        LocalDebtColors provides debtColors,
        LocalMoneyTypography provides MoneyTypographyDefaults,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object DaftariQarzho {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val debtColors: DebtColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val moneyTypography: MoneyTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMoneyTypography.current
}
