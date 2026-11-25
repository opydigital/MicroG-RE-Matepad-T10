# Changelog — MicroG-RE-Matepad-T10

Summary of changes applied on top of `MorpheApp/MicroG-RE` for older Huawei devices (2 GB RAM, no HMS):

## 1. Default Device Profile (Pixel XL — Android 10)
- Added `play-services-core/src/main/res/xml/profile_marlin_29.xml` for **Google Pixel XL (`marlin`, Android 10 / SDK 29)**.
- Updated the default profile in `play-services-base/core/src/main/kotlin/org/microg/gms/settings/SettingsProvider.kt` (`queryProfile`) from `"auto"` to `"marlin_29"`.

## 2. Privacy Defaults
- Updated `SettingsProvider.kt` (`queryAuth`) and `play-services-core/src/main/res/xml/preferences_privacy.xml`:
  - Enabled by default (`true`): `TRUST_GOOGLE`, `VISIBLE`, `INCLUDE_ANDROID_ID`, `STRIP_DEVICE_NAME`.
  - Disabled by default (`false`): `TWO_STEP_VERIFICATION` and `FIND_DEVICES`.

## 3. Maps Optimization (No HMS)
- Made `:play-services-maps:core:hms` optional and disabled by default via `hasModule("maps_hms", false)` in `settings.gradle` and `play-services-core/build.gradle`, keeping **Mapbox/MapLibre** and **VTM** to reduce APK size and RAM usage.

## 4. Standard GMS (`com.google.android.gms`) & Companion Signature Fix
- Set `basePackageName = "com.google"` in `build.gradle` so the main APK builds as `com.google.android.gms` for full native app & notification support on Huawei devices (ReVanced/Morphe apps are patched directly via ReVanced, no `:gms-proxy` module needed).
- Added `<meta-data android:name="${basePackageName}.android.gms.SPOOFED_PACKAGE_SIGNATURE" android:value="38918a453d07199354f8b19af05ec6562ced5788" />` to `vending-app/src/main/AndroidManifest.xml` and `VENDING_PACKAGE_NAME` signature spoofing in `PackageUtils.firstSignatureDigest` so **"MicroG Companion/Play Store has correct signature"** passes on Huawei devices without ROM-level signature spoofing.

## 5. Build & Verification
- Added `android.overridePathCheck=true` to `gradle.properties` to support non-ASCII Windows paths.
- Verified build with `./gradlew :play-services-core:assembleDefaultDebug :vending-app:assembleDefaultDebug`:
  - `com.google.android.gms-255070317.apk`
  - `vending-app-default-debug.apk` (`com.android.vending`)
