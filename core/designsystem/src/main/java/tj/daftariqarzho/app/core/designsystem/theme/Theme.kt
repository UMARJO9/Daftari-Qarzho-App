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

    CompositionLocalProvider(
        LocalDebtColors provides debtColors,
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
