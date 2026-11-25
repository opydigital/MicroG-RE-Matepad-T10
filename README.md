# MicroG-RE-Matepad-T10

Fork do [MorpheApp/MicroG-RE](https://github.com/MorpheApp/MicroG-RE) otimizado para **dispositivos Huawei com 2 GB de RAM** e suporte parcial ou ausente ao Google Mobile Services (GMS), com foco em substituir o **ReVanced / Morphe** e fornecer uma ponte leve para o **GMS padrão**.

> **Dispositivo de teste atual:** Huawei MatePad T10s (versão Latin America)

---

## Principais alterações deste fork

1. **Perfil padrão Pixel XL (Android 10):**
   - Adicionado [profile_marlin_29.xml](play-services-core/src/main/res/xml/profile_marlin_29.xml) (`marlin`, SDK 29) e definido como padrão em [SettingsProvider.kt](play-services-base/core/src/main/kotlin/org/microg/gms/settings/SettingsProvider.kt).
2. **Privacidade ativada por padrão:**
   - Todas as opções de privacidade vêm ativadas por padrão, exceto **Verificação em duas etapas** (`TWO_STEP_VERIFICATION`) e **Localizador de dispositivos** (`FIND_DEVICES`).
3. **Otimização para 2 GB de RAM (sem HMS):**
   - Removido o módulo `:play-services-maps:core:hms` da compilação padrão (via `modules.maps_hms=false`), mantendo apenas **Mapbox/MapLibre** e **VTM**.
4. **Arquitetura Principal + Proxy Leve (`:gms-proxy`):**
   - **APK Principal (`app.revanced.android.gms`):** MicroG completo para apps **Morphe / ReVanced**.
   - **APK Proxy (`com.google.android.gms`):** módulo [gms-proxy](gms-proxy/) ultraleve (~15 KB) que encaminha chamadas Binder, `ContentProvider`s, broadcasts GCM/C2DM e fluxo de login para o `app.revanced.android.gms`, permitindo que aplicativos detectem `com.google.android.gms` sem duplicar o processo completo do MicroG na memória.

---

## Estrutura dos APKs

- `app.revanced.android.gms-255070317.apk` — MicroG principal (`play-services-core`, flavor `default`)
- `gms-proxy-debug.apk` — Proxy leve (`com.google.android.gms`)

Para mais detalhes das modificações, veja [CHANGELOG_CUSTOM.md](CHANGELOG_CUSTOM.md).

---

## Compilação local

```bash
./gradlew :play-services-core:assembleDefaultDebug :gms-proxy:assembleDebug
```

## Licença

Licensed under the Apache License, Version 2.0 (baseado em microG Project Team / MorpheApp/MicroG-RE).
