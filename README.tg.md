<div align="center">

<img src="branding/logo-512.png" width="120" alt="Дафтари қарзҳо" />

# Дафтари қарзҳо

**Ҳисоби оддии офлайни қарзҳо барои мағозаҳои хурд**

Кӣ чӣ қадар гирифт, кӣ чӣ қадар баргардонд ва мӯҳлати кӣ гузашт — ҳамааш дар як барнома, бе интернет ва бе қайд кардан.

[![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-2E7D32.svg)](LICENSE)
![Android 7.0+](https://img.shields.io/badge/Android-7.0%2B-43A047?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![Offline](https://img.shields.io/badge/offline-100%25-1B5E20)

**Тоҷикӣ** · [Русский](README.md) · [English](README.en.md)

[Имкониятҳо](#имкониятҳо) · [Скриншотҳо](#скриншотҳо) · [Сохтан](#сохтан-ва-оғоз) · [Меъморӣ](#меъморӣ) · [Кӯмак ба лоиҳа](#кӯмак-ба-лоиҳа) · [Иҷозатнома](#иҷозатнома)

</div>

---

## Дар бораи лоиҳа

Дар мағозаҳои хурд қарзҳоро аксар вақт дар дафтари коғазӣ менависанд. **Дафтари қарзҳо** — ҳамон дафтар аст, танҳо дар телефон: худаш ҳисоби ҳар як мизоҷро мешуморад, маблағи умумиро нишон медиҳад ва қарзҳои мӯҳлаташон гузаштаро қайд мекунад.

- **Пурра офлайн.** Барнома иҷозати интернет надорад, ҳамаи маълумот танҳо дар телефон нигоҳ дошта мешавад.
- **Бе аккаунт.** Насб кардед — истифода баред.
- **Ду забон.** Тоҷикӣ (пешфарз) ва русӣ.
- **Коди кушода.** Лоиҳа таҳти иҷозатномаи Apache 2.0 паҳн мешавад — онро омӯхтан, тағйир додан ва дар лоиҳаҳои худ истифода бурдан мумкин аст.

## Имкониятҳо

| | |
|---|---|
| 👥 **Мизоҷон** | Рӯйхат бо ҷустуҷӯ, ҳисоб ва санаи амалиёти охирини ҳар як мизоҷ |
| 💸 **Қарз ва пардохт** | «Қарз додам» ва «Пул гирифтам» бо ду ламс, бо эзоҳ ва мӯҳлати бозгашт |
| ⏰ **Мӯҳлати гузашта** | Нишон медиҳад, ки мӯҳлати қарз чанд рӯз гузаштааст |
| 📊 **Хулоса** | Ҳисоби умумӣ, чӣ қадар дода ва гирифта шуд, хати «чӣ қадар баргашт» |
| 🗓️ **Амалиёт** | Ҳамаи амалиёт аз рӯи рӯзҳо бо филтри «Имрӯз / Ҳафта / Моҳ» |
| 📈 **Ҳисоботи моҳона** | Қарзи додашуда, пули гирифташуда ва 5 қарздори калон |
| 🎨 **Намуди зоҳирӣ** | Мавзӯи равшан ва торик, меню ва тугмаҳо бо услуби «шишаи моеъ», нишонаҳои худӣ |
| 🌐 **Забонҳо** | Тоҷикӣ ва русӣ, мустақиман дар дохили барнома иваз мешаванд |

## Скриншотҳо

<div align="center">

| Экрани асосӣ | Амалиёт | Ҳисобот | Танзимот |
|:---:|:---:|:---:|:---:|
| <img src="docs/screenshots/clients.png" width="200" alt="Экрани асосӣ" /> | <img src="docs/screenshots/transactions.png" width="200" alt="Амалиёт" /> | <img src="docs/screenshots/reports.png" width="200" alt="Ҳисобот" /> | <img src="docs/screenshots/settings.png" width="200" alt="Танзимот" /> |

| Мавзӯи торик | Саҳифаи мизоҷ | Мӯҳлати бозгашт |
|:---:|:---:|:---:|
| <img src="docs/screenshots/clients-dark.png" width="200" alt="Мавзӯи торик" /> | <img src="docs/screenshots/debtor-dark.png" width="200" alt="Саҳифаи мизоҷ" /> | <img src="docs/screenshots/due-date.png" width="200" alt="Мӯҳлати бозгашт" /> |

</div>

## Сохтан ва оғоз

**Чӣ лозим аст:**

- Android Studio (охирин версияи устувор) ё JDK 17+
- Android SDK бо платформаи 36
- Телефон ё эмулятор бо Android 7.0 (API 24) ва навтар

**Қадамҳо:**

```bash
git clone https://github.com/UMARJO9/Daftari-Qarzho-App.git
cd Daftari-Qarzho-App
./gradlew assembleDebug
```

Файли тайёри APK дар `app/build/outputs/apk/debug/` пайдо мешавад. Барои фавран насб кардан ба телефони пайвастшуда:

```bash
./gradlew installDebug
```

Ё танҳо папкаи лоиҳаро дар Android Studio кушоед ва **Run**-ро пахш кунед.

**Тестҳо:**

```bash
./gradlew testDebugUnitTest
```

## Технологияҳо

| Чӣ | Бо чӣ |
|---|---|
| Забон | Kotlin, Coroutines + Flow |
| Интерфейс | Jetpack Compose, Material 3, UI kit-и худӣ |
| Эффекти шиша | [Haze](https://github.com/chrisbanes/haze) |
| Нигоҳдорӣ | Room (амалиёт ва мизоҷон), DataStore (танзимот) |
| Ворид кардани вобастагиҳо | Koin |
| Навигатсия | Navigation Compose бо маршрутҳои type-safe |
| Забонҳо | Per-app locale тавассути AppCompat |
| Сохтан | AGP 9, Gradle Kotlin DSL, version catalog |
| Тестҳо | JUnit 4, kotlinx-coroutines-test, Turbine, фейкҳои дастнавис |

## Меъморӣ

Лоиҳа бисёрмодулӣ аст, дар дохили ҳар як фича — Clean Architecture (`presentation → domain ← data`).

```
app                     нуқтаи вуруд: мавзӯъ, навигатсия, экрани асосӣ бо ҷадвалҳо
core/common             пул ва сана: MoneyFormatter, DateFormatter
core/database           Room: ҷадвалҳо, DAO, пойгоҳи додаҳо
core/designsystem       мавзӯи DaftarTheme, UI kit, нишонаҳо
feature/debtors         мизоҷон, саҳифаи мизоҷ, қарзҳо ва пардохтҳо
feature/transactions    рӯйхати амалиёт
feature/reports         ҳисоботи моҳона
feature/settings        забон, мавзӯъ, «Дар бораи барнома»
```

**Қоидаҳое, ки код риоя мекунад:**

- Фичаҳо ба якдигар вобаста нестанд — онҳоро танҳо модули `app` пайваст мекунад.
- Пул ҳамчун `Long` бо дирам нигоҳ дошта мешавад (1 сомонӣ = 100 дирам), бе `Double` ва `Float`.
- Ҳар як экран — ин `UiState` + `Event` + `ViewModel` аст, ки танҳо use case-ҳоро мегирад.
- Мантиқи тиҷоратӣ дар use case-ҳо ҷойгир аст ва бо unit-тестҳо дар фейкҳо фаро гирифта шудааст.

## Кӯмак ба лоиҳа

Аз ҳар гуна кӯмак шод мешавем: ислоҳи хатоҳо, имкониятҳои нав, тарҷумаҳо ва ғояҳо.

1. Fork кунед ва аз `dev` шохаи нав созед.
2. Тағйиротро ворид кунед ва санҷед, ки сохтан ва тестҳо мегузаранд: `./gradlew build`.
3. Ба шохаи `dev` pull request бо тавсифи фаҳмо кушоед.

**Созишномаҳо:**

- Паёмҳои коммит — бо забони русӣ дар формати [Conventional Commits](https://www.conventionalcommits.org/ru/): `feat(debtors): ...`, `fix(reports): ...`.
- Дар код шарҳ наменависем — код бояд бо номҳо фаҳмо бошад.
- Сатрҳои нави интерфейс якбора бо ду забон илова мешаванд: `values/` ва нусхаи он `values-tg/` (тоҷикӣ), `values-ru/` (русӣ).
- Хато ёфтед ё ғоя доред — [issue](https://github.com/UMARJO9/Daftari-Qarzho-App/issues) созед.

## Иҷозатнома

Лоиҳа таҳти иҷозатномаи **Apache License 2.0** паҳн мешавад — тафсилот дар файли [LICENSE](LICENSE).

Copyright © 2026 [UMARJO9](https://github.com/UMARJO9)
