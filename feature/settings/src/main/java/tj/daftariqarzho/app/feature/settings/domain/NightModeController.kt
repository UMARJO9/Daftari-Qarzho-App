package tj.daftariqarzho.app.feature.settings.domain

import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode

interface NightModeController {
    fun apply(mode: ThemeMode)
}
