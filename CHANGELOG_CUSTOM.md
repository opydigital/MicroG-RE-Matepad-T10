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

## 4. Lightweight Proxy Module (`:gms-proxy` → `com.google.android.gms`)
- Kept the main MicroG package as `app.revanced.android.gms` (compatible with Morphe/ReVanced).
- Added the pure-Java `:gms-proxy` (`gms-proxy/`) module with `minifyEnabled true` and `shrinkResources true`:
  - `ProxyBrokerService`: forwards Binder calls (`com.google.*` → `app.revanced.*`) to `app.revanced.android.gms`.
  - `ProxyContentProvider`: forwards queries for `com.google.android.gsf.gservices`, `com.google.settings`, `com.google.android.gms.microg.settings`, and `com.google.android.gms.chimera`.
  - `ProxyBroadcastReceiver`: forwards GCM/C2DM broadcasts.
  - `ProxyAccountAuthenticatorService` + `authenticator.xml`: registers `accountType="com.google"` and delegates sign-in to `app.revanced.android.gms`.

## 5. Build & Verification
- Added `android.overridePathCheck=true` to `gradle.properties` to support non-ASCII Windows paths.
- Verified build with `./gradlew :play-services-core:assembleDefaultDebug :gms-proxy:assembleDebug`:
  - `app.revanced.android.gms-255070317.apk` (`120.13 MB`)
  - `gms-proxy-debug.apk` (`15.2 KB`)
