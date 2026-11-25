# MicroG-RE-Matepad-T10

A fork of [MorpheApp/MicroG-RE](https://github.com/MorpheApp/MicroG-RE) optimized for **Huawei devices with 2 GB of RAM** and partial or missing Google Mobile Services (GMS) support, compiled as **standard GMS (`com.google.android.gms`)** for native apps and notifications (with ReVanced/Morphe apps patched directly via ReVanced).

> **Current test device:** Huawei MatePad T10s (Latin America version)

---

## Key Changes in This Fork

1. **Standard GMS Package (`com.google.android.gms`):**
   - Configured `basePackageName = "com.google"` in [build.gradle](build.gradle) so native apps, push notifications, and system integrations work directly without a proxy APK.
2. **MicroG Companion / Play Store Signature Fix (Huawei / No ROM Spoofing):**
   - Added `SPOOFED_PACKAGE_SIGNATURE` meta-data to [vending-app/src/main/AndroidManifest.xml](vending-app/src/main/AndroidManifest.xml) and `VENDING_PACKAGE_NAME` handling in [PackageUtils.java](play-services-base/core/src/main/java/org/microg/gms/common/PackageUtils.java) so **"MicroG Companion/Play Store has correct signature"** passes without ROM-level signature spoofing.
3. **Default Pixel XL Profile (Android 10):**
   - Added [profile_marlin_29.xml](play-services-core/src/main/res/xml/profile_marlin_29.xml) (`marlin`, SDK 29) and set it as the default profile in [SettingsProvider.kt](play-services-base/core/src/main/kotlin/org/microg/gms/settings/SettingsProvider.kt).
4. **Privacy Options Enabled by Default:**
   - All privacy toggles are enabled by default, except **Two-step verification** (`TWO_STEP_VERIFICATION`) and **Find Devices** (`FIND_DEVICES`).
5. **2 GB RAM Optimization (No HMS):**
   - Excluded `:play-services-maps:core:hms` from the default build (via `modules.maps_hms=false`), keeping only **Mapbox/MapLibre** and **VTM** to reduce APK size and memory footprint.

---

## Build Artifacts

- `com.google.android.gms-255070317.apk` — Main MicroG (`play-services-core`, `default` flavor)
- `vending-app-default-debug.apk` — MicroG Companion (`com.android.vending`)

For detailed modification notes, see [CHANGELOG_CUSTOM.md](CHANGELOG_CUSTOM.md).

---

## Local Build

```bash
./gradlew :play-services-core:assembleDefaultDebug :vending-app:assembleDefaultDebug
```

## License

Licensed under the Apache License, Version 2.0 (based on microG Project Team / MorpheApp/MicroG-RE).
