package tj.daftariqarzho.app.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

object DaftarColors {
    val PrimaryLight = Color(0xFF2E7D32)
    val OnPrimaryLight = Color(0xFFFFFFFF)
    val PrimaryContainerLight = Color(0xFFA5D6A7)
    val OnPrimaryContainerLight = Color(0xFF1B5E20)
    val SecondaryLight = Color(0xFF00695C)
    val TertiaryLight = Color(0xFFF9A825)
    val BackgroundLight = Color(0xFFFAFAF7)
    val SurfaceLight = Color(0xFFFFFFFF)
    val SurfaceVariantLight = Color(0xFFECEAE4)
    val OnBackgroundLight = Color(0xFF1C1B17)
    val OnSurfaceVariantLight = Color(0xFF5C5B55)
    val OutlineLight = Color(0xFF918F87)

    val PrimaryDark = Color(0xFF81C784)
    val OnPrimaryDark = Color(0xFF0A2A0C)
    val PrimaryContainerDark = Color(0xFF1B5E20)
    val OnPrimaryContainerDark = Color(0xFFC8E6C9)
    val SecondaryDark = Color(0xFF4DB6AC)
    val TertiaryDark = Color(0xFFFFD54F)
    val BackgroundDark = Color(0xFF121412)
    val SurfaceDark = Color(0xFF1C1F1C)
    val SurfaceVariantDark = Color(0xFF262926)
    val OnBackgroundDark = Color(0xFFE3E2DD)
    val OnSurfaceVariantDark = Color(0xFFA8A79E)
    val OutlineDark = Color(0xFF6F6E66)

    val DebtGivenLight = Color(0xFFE53935)
    val DebtReceivedLight = Color(0xFF43A047)
    val DebtGivenDark = Color(0xFFEF5350)
    val DebtReceivedDark = Color(0xFF66BB6A)
    val OverdueBgLight = Color(0xFFFFF8E1)
    val OverdueBgDark = Color(0xFF332B14)
    val OverdueTextLight = Color(0xFF6B4500)
    val OverdueTextDark = Color(0xFFFFD54F)
}

@Immutable
data class DebtColors(
    val given: Color,
    val received: Color,
    val overdueBg: Color,
    val overdueText: Color,
)

val LightDebtColors = DebtColors(
    given = DaftarColors.DebtGivenLight,
    received = DaftarColors.DebtReceivedLight,
    overdueBg = DaftarColors.OverdueBgLight,
    overdueText = DaftarColors.OverdueTextLight,
)

val DarkDebtColors = DebtColors(
    given = DaftarColors.DebtGivenDark,
    received = DaftarColors.DebtReceivedDark,
    overdueBg = DaftarColors.OverdueBgDark,
    overdueText = DaftarColors.OverdueTextDark,
)

val LocalDebtColors = staticCompositionLocalOf {
    DebtColors(
        given = Color.Unspecified,
        received = Color.Unspecified,
        overdueBg = Color.Unspecified,
        overdueText = Color.Unspecified,
    )
}

val lightScheme = lightColorScheme(
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

val darkScheme = darkColorScheme(
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
