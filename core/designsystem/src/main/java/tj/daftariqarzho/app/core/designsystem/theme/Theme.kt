package tj.daftariqarzho.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun DaftarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme
    val debtColors = if (darkTheme) DarkDebtColors else LightDebtColors
    val glassColors = if (darkTheme) DarkGlassColors else LightGlassColors
    val controlColors = if (darkTheme) DarkControlColors else LightControlColors
    val heroColors = if (darkTheme) DarkHeroColors else LightHeroColors

    CompositionLocalProvider(
        LocalDebtColors provides debtColors,
        LocalGlassColors provides glassColors,
        LocalControlColors provides controlColors,
        LocalHeroColors provides heroColors,
        LocalMoneyTypography provides DefaultMoneyTypography,
        LocalDaftarSpacing provides DaftarSpacing(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = DaftarTypography,
            shapes = DaftarShapes,
            content = content,
        )
    }
}

object DaftarTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val debtColors: DebtColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtColors.current

    val glassColors: GlassColors
        @Composable
        @ReadOnlyComposable
        get() = LocalGlassColors.current

    val controlColors: ControlColors
        @Composable
        @ReadOnlyComposable
        get() = LocalControlColors.current

    val heroColors: HeroColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHeroColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val moneyTypography: MoneyTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMoneyTypography.current

    val spacing: DaftarSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalDaftarSpacing.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}
