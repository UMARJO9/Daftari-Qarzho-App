package tj.daftariqarzho.app.feature.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

@Composable
fun ReportsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.reports_title),
            style = DaftarTheme.typography.headlineSmall,
            color = DaftarTheme.colors.onBackground,
        )
    }
}
