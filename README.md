# MicroG-RE-Matepad-T10

A fork of [MorpheApp/MicroG-RE](https://github.com/MorpheApp/MicroG-RE) optimized for **Huawei devices with 2 GB of RAM** and partial or missing Google Mobile Services (GMS) support, designed to replace **ReVanced / Morphe** and provide a lightweight bridge for **standard GMS**.

> **Current test device:** Huawei MatePad T10s (Latin America version)

---

## Key Changes in This Fork

1. **Default Pixel XL Profile (Android 10):**
   - Added [profile_marlin_29.xml](play-services-core/src/main/res/xml/profile_marlin_29.xml) (`marlin`, SDK 29) and set it as the default profile in [SettingsProvider.kt](play-services-base/core/src/main/kotlin/org/microg/gms/settings/SettingsProvider.kt).
2. **Privacy Options Enabled by Default:**
   - All privacy toggles are enabled by default, except **Two-step verification** (`TWO_STEP_VERIFICATION`) and **Find Devices** (`FIND_DEVICES`).
3. **2 GB RAM Optimization (No HMS):**
   - Excluded `:play-services-maps:core:hms` from the default build (via `modules.maps_hms=false`), keeping only **Mapbox/MapLibre** and **VTM** to reduce APK size and memory footprint.
4. **Main App + Lightweight Proxy Architecture (`:gms-proxy`):**
   - **Main APK (`app.revanced.android.gms`):** Full MicroG implementation for **Morphe / ReVanced** apps.
   - **Proxy APK (`com.google.android.gms`):** Ultra-lightweight [gms-proxy](gms-proxy/) module (~15 KB) that forwards Binder calls, `ContentProvider`s, GCM/C2DM broadcasts, and account login flows to `app.revanced.android.gms`, allowing apps to detect `com.google.android.gms` without running a second full MicroG process in memory.

---

## Build Artifacts

- `app.revanced.android.gms-255070317.apk` — Main MicroG (`play-services-core`, `default` flavor)
- `gms-proxy-debug.apk` — Lightweight proxy (`com.google.android.gms`)

For detailed modification notes, see [CHANGELOG_CUSTOM.md](CHANGELOG_CUSTOM.md).

---

## Local Build

```bash
./gradlew :play-services-core:assembleDefaultDebug :gms-proxy:assembleDebug
```

## License

Licensed under the Apache License, Version 2.0 (based on microG Project Team / MorpheApp/MicroG-RE).
