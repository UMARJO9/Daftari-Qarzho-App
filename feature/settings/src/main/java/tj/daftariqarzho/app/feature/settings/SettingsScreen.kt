package tj.daftariqarzho.app.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tj.daftariqarzho.app.core.designsystem.components.DaftarListSection
import tj.daftariqarzho.app.core.designsystem.components.DaftarSelectableRow
import tj.daftariqarzho.app.core.designsystem.components.LocalGlassBarInset
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.presentation.SettingsEvent
import tj.daftariqarzho.app.feature.settings.presentation.SettingsUiState
import tj.daftariqarzho.app.feature.settings.presentation.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsContent(state = state, onEvent = viewModel::onEvent)
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(DaftarTheme.spacing.screenPadding)
            .padding(bottom = LocalGlassBarInset.current),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.xl),
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = DaftarTheme.typography.headlineSmall,
            color = DaftarTheme.colors.onBackground,
        )

        DaftarListSection(title = stringResource(R.string.settings_language)) {
            val languages = AppLanguage.entries
            languages.forEachIndexed { index, language ->
                DaftarSelectableRow(
                    text = stringResource(language.labelRes()),
                    selected = language == state.language,
                    onClick = { onEvent(SettingsEvent.LanguageSelected(language)) },
                    showDivider = index != languages.lastIndex,
                )
            }
        }

        DaftarListSection(title = stringResource(R.string.settings_theme)) {
            val modes = ThemeMode.entries
            modes.forEachIndexed { index, mode ->
                DaftarSelectableRow(
                    text = stringResource(mode.labelRes()),
                    selected = mode == state.themeMode,
                    onClick = { onEvent(SettingsEvent.ThemeSelected(mode)) },
                    showDivider = index != modes.lastIndex,
                )
            }
        }

        AboutSection()
    }
}

@Composable
private fun AboutSection() {
    val context = LocalContext.current
    val versionName = remember(context) {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull().orEmpty()
    }

    DaftarListSection(title = stringResource(R.string.settings_about)) {
        Column(
            modifier = Modifier.padding(DaftarTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.xs),
        ) {
            Text(
                text = stringResource(R.string.settings_about_description),
                style = DaftarTheme.typography.bodyMedium,
                color = DaftarTheme.colors.onSurface,
            )
            Text(
                text = stringResource(R.string.settings_about_version, versionName),
                style = DaftarTheme.typography.bodySmall,
                color = DaftarTheme.colors.onSurfaceVariant,
            )
        }
    }
}

private fun AppLanguage.labelRes(): Int = when (this) {
    AppLanguage.TAJIK -> R.string.settings_language_tajik
    AppLanguage.RUSSIAN -> R.string.settings_language_russian
}

private fun ThemeMode.labelRes(): Int = when (this) {
    ThemeMode.SYSTEM -> R.string.settings_theme_system
    ThemeMode.LIGHT -> R.string.settings_theme_light
    ThemeMode.DARK -> R.string.settings_theme_dark
}
