<div align="center">

<img src="branding/logo-512.png" width="120" alt="Daftari Qarzho" />

# Daftari Qarzho

**Simple offline debt tracking for small shops**

Who borrowed how much, who paid back and whose due date has passed — all in one app, with no internet and no sign-up.

[![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-2E7D32.svg)](LICENSE)
![Android 7.0+](https://img.shields.io/badge/Android-7.0%2B-43A047?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![Offline](https://img.shields.io/badge/offline-100%25-1B5E20)

**English** · [Тоҷикӣ](README.tg.md) · [Русский](README.ru.md)

[Features](#features) · [Screenshots](#screenshots) · [Build](#build-and-run) · [Architecture](#architecture) · [Contributing](#contributing) · [License](#license)

</div>

---

## About

In small shops, debts are often written down in a paper notebook — a "daftar". **Daftari Qarzho** ("debt notebook" in Tajik) is that same notebook, only on your phone: it calculates each customer's balance, shows the total amount and highlights overdue debts.

- **Fully offline.** The app has no internet permission; all data stays on the phone.
- **No accounts.** Install it and start using it.
- **Two languages.** Tajik (default) and Russian.
- **Open source.** The project is licensed under Apache 2.0 — you are free to study it, modify it and use it in your own projects.

> **Тоҷикӣ.** «Дафтари қарзҳо» — барномаи офлайн барои ҳисоби қарзҳо дар мағозаҳои хурд. Бе интернет, бе қайд кардан, бо забонҳои тоҷикӣ ва русӣ. Коди барнома кушода аст.

## Features

| | |
|---|---|
| 👥 **Customers** | Searchable list with each customer's balance and last operation date |
| 💸 **Debts and payments** | "Lent" and "Received payment" in two taps, with a note and a due date |
| ⏰ **Overdue** | Shows how many days a debt is overdue |
| 📊 **Summary** | Total balance, amounts lent and received, a "how much has been repaid" progress bar |
| 🗓️ **Operations feed** | All operations grouped by day with a "Today / Week / Month" filter |
| 📈 **Monthly report** | Lent, received and top 5 debtors |
| 🎨 **Design** | Light and dark themes, "liquid glass" menu and buttons, custom icons |
| 🌐 **Languages** | Tajik and Russian, switchable right inside the app |

## Screenshots

<div align="center">

| Home | Operations | Report | Settings |
|:---:|:---:|:---:|:---:|
| <img src="docs/screenshots/clients.png" width="200" alt="Home" /> | <img src="docs/screenshots/transactions.png" width="200" alt="Operations" /> | <img src="docs/screenshots/reports.png" width="200" alt="Report" /> | <img src="docs/screenshots/settings.png" width="200" alt="Settings" /> |

| Dark theme | Customer card | Due date |
|:---:|:---:|:---:|
| <img src="docs/screenshots/clients-dark.png" width="200" alt="Dark theme" /> | <img src="docs/screenshots/debtor-dark.png" width="200" alt="Customer card" /> | <img src="docs/screenshots/due-date.png" width="200" alt="Due date" /> |

</div>

## Build and run

**Requirements:**

- Android Studio (latest stable) or JDK 17+
- Android SDK with platform 36
- A phone or emulator running Android 7.0 (API 24) or newer

**Steps:**

```bash
git clone https://github.com/UMARJO9/Daftari-Qarzho-App.git
cd Daftari-Qarzho-App
./gradlew assembleDebug
```

The APK will appear in `app/build/outputs/apk/debug/`. To install it directly on a connected phone:

```bash
./gradlew installDebug
```

Or just open the project folder in Android Studio and press **Run**.

**Tests:**

```bash
./gradlew testDebugUnitTest
```

## Tech stack

| What | With |
|---|---|
| Language | Kotlin, Coroutines + Flow |
| UI | Jetpack Compose, Material 3, custom UI kit |
| Glass effect | [Haze](https://github.com/chrisbanes/haze) |
| Storage | Room (operations and customers), DataStore (settings) |
| Dependency injection | Koin |
| Navigation | Navigation Compose with type-safe routes |
| Languages | Per-app locale via AppCompat |
| Build | AGP 9, Gradle Kotlin DSL, version catalog |
| Tests | JUnit 4, kotlinx-coroutines-test, Turbine, hand-written fakes |

## Architecture

The project is multi-module, with Clean Architecture inside each feature (`presentation → domain ← data`).

```
app                     entry point: theme, navigation, home screen with tabs
core/common             money and dates: MoneyFormatter, DateFormatter
core/database           Room: tables, DAOs, database
core/designsystem       DaftarTheme, UI kit, icons
feature/debtors         customers, customer card, debts and payments
feature/transactions    operations feed
feature/reports         monthly report
feature/settings        language, theme, "About"
```

**Rules the code follows:**

- Features don't depend on each other — only the `app` module wires them together.
- Money is stored as `Long` in dirams (1 somoni = 100 dirams), never as `Double` or `Float`.
- Every screen is a `UiState` + `Event` + `ViewModel` that receives only use cases.
- Business logic lives in use cases and is covered by unit tests with fakes.

## Contributing

Any help is welcome: bug fixes, new features, translations and ideas.

1. Fork the repository and create a branch from `dev`.
2. Make your changes and make sure the build and tests pass: `./gradlew build`.
3. Open a pull request into `dev` with a clear description.

**Conventions:**

- Commit messages are written in Russian using [Conventional Commits](https://www.conventionalcommits.org/): `feat(debtors): ...`, `fix(reports): ...`.
- No comments in code — names should explain the code.
- New UI strings are added in both languages at once: `values/` and its copy `values-tg/` (Tajik), `values-ru/` (Russian).
- Found a bug or have an idea? Open an [issue](https://github.com/UMARJO9/Daftari-Qarzho-App/issues).

## License

The project is licensed under the **Apache License 2.0** — see the [LICENSE](LICENSE) file for details.

Copyright © 2026 [UMARJO9](https://github.com/UMARJO9)
