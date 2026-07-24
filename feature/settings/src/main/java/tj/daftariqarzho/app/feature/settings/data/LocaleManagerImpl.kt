package tj.daftariqarzho.app.feature.settings.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import tj.daftariqarzho.app.feature.settings.domain.LocaleManager
import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage

class LocaleManagerImpl : LocaleManager {

    override fun currentLanguage(): AppLanguage {
        val locales = AppCompatDelegate.getApplicationLocales()
        val tag = if (locales.isEmpty) null else locales[0]?.language
        return AppLanguage.fromTag(tag)
    }

    override fun setLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.tag),
        )
    }
}
