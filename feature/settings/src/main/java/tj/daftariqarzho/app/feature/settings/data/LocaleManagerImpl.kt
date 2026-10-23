package tj.daftariqarzho.app.feature.settings.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import tj.daftariqarzho.app.feature.settings.domain.LocaleManager
import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage

class LocaleManagerImpl : LocaleManager {

    override fun currentLanguage(): AppLanguage {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val locales = if (appLocales.isEmpty) LocaleListCompat.getAdjustedDefault() else appLocales
        val languages = (0 until locales.size()).mapNotNull { locales[it]?.language }
        return AppLanguage.firstSupported(languages)
    }

    override fun setLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.tag),
        )
    }
}
