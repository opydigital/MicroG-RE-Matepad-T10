# Changelog — MicroG-RE-Matepad-T10

Resumo das alterações aplicadas sobre `MorpheApp/MicroG-RE` para uso em dispositivo Huawei antigo (2 GB de RAM, sem HMS):

## 1. Perfil de Dispositivo Padrão (Pixel XL — Android 10)
- Adicionado `play-services-core/src/main/res/xml/profile_marlin_29.xml` com os dados do **Google Pixel XL (`marlin`, Android 10 / SDK 29)**.
- Alterado o perfil padrão em `play-services-base/core/src/main/kotlin/org/microg/gms/settings/SettingsProvider.kt` (`queryProfile`) de `"auto"` para `"marlin_29"`.

## 2. Opções de Privacidade por Padrão
- Atualizados `SettingsProvider.kt` (`queryAuth`) e `play-services-core/src/main/res/xml/preferences_privacy.xml`:
  - Ativadas por padrão (`true`): `TRUST_GOOGLE`, `VISIBLE`, `INCLUDE_ANDROID_ID`, `STRIP_DEVICE_NAME`.
  - Desativadas por padrão (`false`): `TWO_STEP_VERIFICATION` (solicitação de verificação em duas etapas) e `FIND_DEVICES` (localizador de dispositivos).

## 3. Otimização de Mapas (Sem HMS)
- Módulo `:play-services-maps:core:hms` tornado opcional e desativado por padrão via `hasModule("maps_hms", false)` em `settings.gradle` e `play-services-core/build.gradle`, mantendo **Mapbox/MapLibre** e **VTM** para reduzir tamanho do APK e consumo de RAM.

## 4. Módulo Proxy Leve (`:gms-proxy` → `com.google.android.gms`)
- Mantido o MicroG principal como `app.revanced.android.gms` (compatível com Morphe/ReVanced).
- Criado o módulo `:gms-proxy` (`gms-proxy/`) em Java puro com `minifyEnabled true` e `shrinkResources true`:
  - `ProxyBrokerService`: encaminha chamadas Binder (`com.google.*` → `app.revanced.*`) para `app.revanced.android.gms`.
  - `ProxyContentProvider`: redireciona queries de `com.google.android.gsf.gservices`, `com.google.settings`, `com.google.android.gms.microg.settings` e `com.google.android.gms.chimera`.
  - `ProxyBroadcastReceiver`: encaminha broadcasts GCM/C2DM.
  - `ProxyAccountAuthenticatorService` + `authenticator.xml`: registra `accountType="com.google"` delegando login para `app.revanced.android.gms`.

## 5. Build e Compilação
- Adicionado `android.overridePathCheck=true` em `gradle.properties` para suportar caminhos Windows com caracteres não-ASCII.
- Compilação validada com `./gradlew :play-services-core:assembleDefaultDebug :gms-proxy:assembleDebug`:
  - `app.revanced.android.gms-255070317.apk` (`120.13 MB`)
  - `gms-proxy-debug.apk` (`15.2 KB`)
