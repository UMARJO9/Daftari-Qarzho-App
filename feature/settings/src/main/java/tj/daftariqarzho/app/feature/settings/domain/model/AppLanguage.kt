package tj.daftariqarzho.app.feature.settings.domain.model

enum class AppLanguage(val tag: String) {
    TAJIK("tg"),
    RUSSIAN("ru"),
    ;

    companion object {
        fun fromTag(tag: String?): AppLanguage =
            entries.firstOrNull { tag != null && tag.startsWith(it.tag) } ?: TAJIK

        fun firstSupported(languages: List<String>): AppLanguage =
            languages.firstNotNullOfOrNull { language -> entries.firstOrNull { it.tag == language } } ?: TAJIK
    }
}
