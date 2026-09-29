# AGENTS.md

Context for AI coding tools working on this repository. Read this fully before making changes. Keep it updated as the project evolves (see "Maintaining this file" at the bottom).

## Project

**Soundtracked** is an Android app: "Letterboxd for music". Users rate, review and log albums they listen to.

- Package: `io.github.salman7236.soundtracked`
- Solo project by Salman, an experienced backend developer (MERN, FastAPI, .NET) who is learning Android.
- Name chosen over "Spun" (collision with the Spun It vinyl app). Trademark and domain check still to do.

### Goal

A social-style music diary: rate albums and tracks, write reviews, favorite albums, and keep a repeatable listening diary. RYM and AOTY have no public APIs, so **MusicBrainz is the data backbone** (IDs and metadata), with Cover Art Archive, Discogs and Last.fm added on demand.

## How to work with the owner (important)

- **No vibe coding.** Salman is here to learn. Explain concepts first and let him write the code. Give full code only when he explicitly asks for it.
- Keep explanations concise. Avoid heavy bullet-point formatting.
- Point out real errors and bad ideas directly. Do not just agree.
- Do not silently reverse settled decisions (see "Product decisions" and "Rejected" below). If you think one is wrong, say so and explain why.
- Do not add dependencies, modules or architecture layers that are not listed here without asking.
- Python tooling (backend, when it exists): use `uv`, not `pip`.
- Environment: Windows 11 and Arch Linux, Android Studio, Pixel 7 via wireless debugging.
- Git: small commits in dependency order (dependencies, then data, then state/ViewModel, then UI, then wiring), so every commit builds on its own.

## Tech stack

Settled:

- Kotlin + Jetpack Compose (Material 3), single Gradle module `:app`
- minSdk 26, targetSdk 37, compileSdk 37
- MVVM, Coroutines/Flow (coroutines arrive transitively through the lifecycle artifacts, not declared directly)
- Planned client libraries: Room (offline-first cache), Retrofit, Coil, Navigation Compose
- **No Hilt yet.** Wire dependencies manually for now (see "Architecture and data flow").
- Domain model, network DTO and Room entity are **three separate classes** with mapper functions between them. Never share one class across layers.

Backend (planned, not in the repo yet):

- FastAPI + PostgreSQL + OpenAPI, modular monolith
- User data (ratings, reviews, favorites, diary, accounts) lives on our backend
- Catalog is a cache-aside proxy, e.g. `GET /api/v1/albums/{mbid}`: check Postgres, else fetch MusicBrainz + Cover Art Archive, map to our model, store. No ingestion pipeline.
- Android talks only to our backend, never to MusicBrainz directly.

Later, not decided yet:

- Website (Next.js) as another client of the same API, built last
- iOS via KMP or Compose Multiplatform

### Data provider rules

- **MusicBrainz**: data is CC0. Soft refresh every 30-90 days. The API is free for non-commercial use only, limited to 1 request/sec, and requires a proper `User-Agent`. Monetization would need a commercial plan.
- **Discogs**: data must be under 6 hours old and needs the attribution "Data provided by Discogs".

## Build and tooling versions

Defined in `gradle/libs.versions.toml` (version catalog). Add all new dependencies there, not inline in `build.gradle.kts`.

- Android Gradle Plugin 9.4.1, Kotlin 2.2.10, Compose BOM 2026.02.01
- Gradle wrapper 9.7.1
- JDK 25 toolchain for the Gradle daemon (`gradle/gradle-daemon-jvm.properties`); Java/Kotlin source and target compatibility is 11
- Configuration cache is enabled (`gradle.properties`)
- Release build has optimization disabled for now

Current `:app` dependencies: Compose BOM, `activity-compose`, `material3`, `ui`, `ui-graphics`, `ui-tooling-preview`, `core-ktx`, `lifecycle-runtime-ktx`, `lifecycle-viewmodel-compose`, `lifecycle-runtime-compose`. The lifecycle artifacts all share the version ref `lifecycleRuntimeKtx` (2.6.1, which is old; a bump is worth considering later).

Line endings: LF everywhere. `.gitattributes` has `* text=auto eol=lf` plus `*.bat text eol=crlf` (Windows batch files need CRLF), and the project code style scheme (`.idea/codeStyles/Project.xml`) sets LF for new files. `git ls-files --eol` shows the state; only `gradlew.bat` should be `w/crlf`.

Known issue: downloads sometimes fail without a VPN (Warp).

Commands:

```
./gradlew assembleDebug        # build
./gradlew test                 # local unit tests
./gradlew connectedAndroidTest # instrumented tests (needs a device)
```

## Repository structure

```
Soundtracked/
├── AGENTS.md
├── .gitattributes                # LF everywhere, CRLF for *.bat
├── build.gradle.kts              # top-level plugins
├── settings.gradle.kts           # repos, module include, foojay toolchain resolver
├── gradle.properties
├── gradle/
│   ├── libs.versions.toml        # version catalog (all dependency versions)
│   ├── gradle-daemon-jvm.properties
│   └── wrapper/
└── app/
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── keepRules/rules.keep
        │   ├── res/              # values (strings, colors, themes), xml (backup rules), mipmap, drawable
        │   └── java/io/github/salman7236/soundtracked/
        │       ├── MainActivity.kt
        │       ├── data/
        │       │   └── repository/
        │       │       ├── AlbumRepository.kt
        │       │       └── FakeAlbumRepository.kt
        │       ├── domain/
        │       └── ui/
        │           ├── album/
        │           │   ├── AlbumList.kt
        │           │   ├── AlbumListItem.kt
        │           │   ├── AlbumListScreen.kt
        │           │   ├── AlbumListUiState.kt
        │           │   ├── AlbumListViewModel.kt
        │           │   └── AlbumRow.kt
        │           └── theme/
        ├── test/                 # local unit tests (currently only the template example)
        └── androidTest/          # instrumented tests (currently only the template example)
```

### Source packages

Base path: `app/src/main/java/io/github/salman7236/soundtracked/`

| Package | Purpose |
|---|---|
| `domain/` | Pure Kotlin domain classes. No Android or framework dependencies. |
| `data/repository/` | Repository interfaces returning domain classes, plus implementations. Only the interface and a fake exist so far. |
| `data/local/`, `data/remote/` (planned) | `local/` (Room), `remote/` (Retrofit + DTOs). Not created yet. |
| `ui/album/` | Album list feature: composables, UI state, UI models, ViewModel. |
| `ui/theme/` | Material 3 theme (`Theme.kt`, `Color.kt`, `Type.kt`). Still the Studio template defaults; dynamic color enabled on Android 12+. |

Files today:

- `MainActivity.kt`: creates `AlbumListViewModel` with `FakeAlbumRepository`, collects its state, and hosts `AlbumListScreen` inside a `Scaffold`. `onAlbumClick` is still a no-op. Will become the navigation host.
- `domain/`: `Artist`, `Album`, `PrimaryType`, `SecondaryType`, `Release`, `Format`, `Track`, `AlbumRating`, `TrackRating`, `Review`, `AlbumFavorite`, `DiaryEntry`
- `data/repository/AlbumRepository.kt`: interface with `suspend fun getAlbums(): List<Album>` and `suspend fun getArtists(): List<Artist>`.
- `data/repository/FakeAlbumRepository.kt`: temporary hard-coded data (six artists, fourteen albums, one with a long title and no date) with an 800 ms `delay` so the Loading state is visible. To be replaced by a real implementation behind the same interface.
- `ui/album/AlbumListItem.kt`: UI model `AlbumListItem(album: Album, artistName: String)`, built by the ViewModel by joining album and artist data. Lives in `ui`, not `domain`.
- `ui/album/AlbumListUiState.kt`: sealed interface with `Loading` (data object), `Success(items: List<AlbumListItem>)` and `Error(message: String)`.
- `ui/album/AlbumListViewModel.kt`: takes an `AlbumRepository`, exposes `uiState: StateFlow<AlbumListUiState>`, loads in `init`, and has a public `load()` used by Retry. Joins albums and artists with `associateBy`; a missing artist becomes "Unknown artist".
- `ui/album/AlbumRow.kt`: stateless row composable (`item`, `onClick`, `modifier`) with a placeholder cover `Box`, title, artist, and a "Type · Secondary · Year" subtitle. Includes private label helpers for the enums and a `@Preview`.
- `ui/album/AlbumList.kt`: stateless `LazyColumn` of `AlbumRow` keyed by `album.id`, with a scrolling `@Preview` of eight sample albums.
- `ui/album/AlbumListScreen.kt`: stateless screen that switches on `AlbumListUiState` (spinner, `AlbumList`, or error message with Retry button). Has Loading and Error previews.

## Architecture and data flow

```
MainActivity (creates the ViewModel, collects uiState)
  -> AlbumListViewModel(repository: AlbumRepository)
       -> AlbumRepository (FakeAlbumRepository for now)
  -> AlbumListScreen(state, onAlbumClick, onRetry)
       -> AlbumList(items, onAlbumClick) -> AlbumRow(item, onClick)
```

- State flows down, events flow up. Screens and rows are stateless: they take state and lambdas.
- The ViewModel is created in the Activity with `by viewModels { viewModelFactory { initializer { ... } } }`, which is the manual dependency wiring while there is no Hilt. Swapping the fake repository for a real one is a one-line change there.
- `collectAsStateWithLifecycle()` turns the `StateFlow` into Compose state and stops collecting while the app is in the background.
- Repositories return domain classes only. Mapping from DTOs and Room entities happens inside the repository implementation, never above it.

## Domain model

All IDs are `String`. Timestamps are `java.time.Instant`. Server sets `createdAt`.

- **Artist**: `id`, `musicBrainzId`, `name`, `imageUrl?`
- **Album** (a MusicBrainz release group): `id`, `musicBrainzId`, `title`, `artistId`, `primaryType?`, `secondaryTypes: List<SecondaryType>`, `releaseDate?`, `imageUrl?`
- **PrimaryType**: `ALBUM, SINGLE, EP, BROADCAST, OTHER`
- **SecondaryType**: `COMPILATION, SOUNDTRACK, SPOKENWORD, INTERVIEW, AUDIOBOOK, AUDIO_DRAMA, LIVE, REMIX, DJ_MIX, MIXTAPE, DEMO, FIELD_RECORDING`
- **Release** (a specific edition of an album): `id`, `musicBrainzId`, `editionName?`, `albumId`, `format?`, `country?`, `releaseDate?`, `imageUrl?`
- **Format**: `CD, VINYL, DIGITAL, CASSETTE, OTHER`
- **Track**: `id`, `musicBrainzId`, `recordingMusicBrainzId`, `releaseId`, `artistId`, `title`, `discNumber`, `position`, `durationMs?`
- **AlbumRating**: `id`, `userId`, `albumId`, `value: Int?`, `createdAt`
- **TrackRating**: `id`, `userId`, `recordingMusicBrainzId`, `value: Int?`, `createdAt` (points at the recording MBID, not a Track row)
- **Review**: `id`, `userId`, `albumId`, `body: String?`, `createdAt`
- **AlbumFavorite**: `id`, `userId`, `albumId`, `createdAt`
- **DiaryEntry**: `id`, `albumId`, `releaseId?`, `note?`, `listenedDate: LocalDate`, `listenedTime: LocalTime?`, `createdAt`, `userId`

## Product decisions (settled)

- Rate and review at **Album** (MusicBrainz release group) level. Singles, EPs, mixtapes and compilations are all Albums, distinguished by `primaryType` plus the `secondaryTypes` list.
- Rating: 5 stars in half-steps, stored as `Int` 1-10. `null` means the rating was cleared. Album and track ratings both exist. All ratings are optional and independent of reviews, favorites and diary entries.
- Ratings and reviews are **append-only history**: a new row per edit, the latest row is current, the server sets `createdAt`, and there is no unique constraint on `(userId, albumId)`.
- **Favorite** is current state (a row exists or it does not). Albums only, no track favorites.
- **DiaryEntry** is repeatable and meant for one-tap logging. Date required, time optional, note optional, optional edition (`Release`) tag. Never auto-created.

## Rejected (do not reintroduce without discussion)

React Native / Flutter, Android calling MusicBrainz directly, a full ingestion pipeline, multi-module Gradle, microservices, a single `Rating` class with nullable links, a `Boolean` on Favorite, track favorites, sharing one class across Room/DTO/domain, building the website now.

## Code conventions

- Kotlin official code style (`kotlin.code.style=official`).
- Composables: PascalCase functions, `modifier: Modifier = Modifier` as the first optional parameter, stateless where possible (data in, lambdas out). Every screen-level or reusable composable gets a `@Preview` with hard-coded data, wrapped in `SoundtrackedTheme`.
- Use `MaterialTheme.colorScheme` and `MaterialTheme.typography` tokens, never hard-coded colors or text styles.
- Enum-to-display-string mapping lives in the UI layer (private `label()` helpers for now, string resources when localizing). The domain layer knows nothing about display text.
- Lists use `LazyColumn` with a stable `key`.
- UI models (like `AlbumListItem`) and UI state live in `ui`; domain classes stay in `domain`.
- Screen state is a `sealed interface` (one state at a time, exhaustive `when`, no `else`).
- ViewModels expose a read-only `StateFlow` backed by a private `MutableStateFlow` (`_uiState`), launch work in `viewModelScope`, and rethrow `CancellationException` before catching `Exception`.

## Current status and roadmap

Done: domain classes, theme scaffolding (template), `AlbumListItem`, `AlbumRow`, `AlbumList`, `AlbumListUiState`, `AlbumListViewModel`, `AlbumListScreen`, `AlbumRepository` with `FakeAlbumRepository`, MainActivity wired end to end (verified on a Pixel 7: spinner, then a scrolling list of fourteen albums).

Next, in order:

1. FastAPI backend and the cached catalog endpoint
2. Room and Retrofit, replacing `FakeAlbumRepository` behind `AlbumRepository`; then Coil (`AsyncImage` replaces the placeholder cover `Box`)

Open questions: whether to build the detail screen and Navigation Compose before the backend (`onAlbumClick` is still a no-op); how track ratings display relative to album ratings; monetization would require a MusicBrainz commercial plan.

## Maintaining this file

Update this file whenever something structural changes: a new package or module, a new dependency, a changed or reversed decision, a new domain field, a finished roadmap step. Keep it accurate over keeping it long.
