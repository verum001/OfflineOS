# Little Calculator

A small, independent Android calculator for OfflineOS. Android 8.0 or newer. Apache-2.0.

- Basic arithmetic, parentheses, decimals, and percent (15% means 0.15).
- Swipe down on the calculator or tap the history hint to see the latest 100 calculations. Tap an entry to reuse its result. History stays on the device and can be cleared.
- Scrollable tabs: Calc, Currency, Length, Weight, Temperature, Area, Volume, Speed, Time.
- Editable currency rates, stored as RUB per one unit of USD, EUR, CNY or GBP. No preset or downloaded rates; enter your own before converting. The last manual save time is displayed. Saved rates can become outdated.
- Instant conversion, swap button, remembered units and amounts.
- System light/dark theme; selectable results for copying.
- No permissions, accounts, telemetry, external libraries, or network access. Android backup is disabled.

## Test APK

[Download LittleCalculator 0.1.0 test APK](downloads/LittleCalculator-0.1.0-test.apk?raw=true) · [SHA-256 checksum](downloads/SHA256SUMS)

This is an early test build. Device testing is still pending.

## Build

With JDK 21+ and Android SDK 37 installed:

```sh
export ANDROID_HOME=/path/to/Android/Sdk
./gradlew :app:assembleDebug
```

Gradle 9.6.0 and Android Gradle Plugin 9.4.1 are pinned. Gradle downloads build tooling when missing; the installed application remains offline.

For a restricted environment with existing SDK tools and a JDK:

```sh
export JAVA_HOME=/path/to/jdk
export ANDROID_HOME=/path/to/Android/Sdk
./tools/test.sh
./tools/build-sdk.sh
```

The fallback uses SDK build-tools 36.0.0 and platform android-37.0 by default. Output: `build/LittleCalculator-0.1.0-test.apk`. This is signed with a generated local test certificate, not a production release identity. Preserve the test keystore under `build/sdk` if you need to update a test installation without uninstalling. Keep production signing material outside this project.

## Validation

211 arithmetic and conversion checks pass, including operator precedence, invalid input, division by zero, temperature limits, unit round trips, and US/Imperial gallon differences. The SDK-only test APK builds, passes signature verification, and declares no permissions. Gradle execution was blocked by the environment's read-only cache. Device/emulator testing was blocked by inability to start ADB's local socket.

Before release, test swiping and tapping history, process restart, light/dark themes, small screens, landscape, keyboard resizing, currency editing, TalkBack and font scaling on a device. No runtime or accessibility certification is claimed. Floating-point arithmetic is rounded to 12 significant digits for display; this is not a financial accounting tool.
