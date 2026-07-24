package tj.daftariqarzho.app.feature.settings.domain

import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage

interface LocaleManager {
    fun currentLanguage(): AppLanguage
    fun setLanguage(language: AppLanguage)
}
