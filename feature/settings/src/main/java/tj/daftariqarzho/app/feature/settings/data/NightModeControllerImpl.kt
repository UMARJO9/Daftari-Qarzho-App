package tj.daftariqarzho.app.feature.settings.data

import androidx.appcompat.app.AppCompatDelegate
import tj.daftariqarzho.app.feature.settings.domain.NightModeController
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode

class NightModeControllerImpl : NightModeController {

    override fun apply(mode: ThemeMode) {
        AppCompatDelegate.setDefaultNightMode(
            when (mode) {
                ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            },
        )
    }
}
