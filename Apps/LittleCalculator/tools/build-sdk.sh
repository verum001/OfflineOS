#!/usr/bin/env bash
# Small SDK-only build for environments where Gradle cannot start a daemon.
set -euo pipefail
cd "$(dirname "$0")/.."
: "${ANDROID_HOME:?Set ANDROID_HOME to your Android SDK}"
: "${JAVA_HOME:?Set JAVA_HOME to a JDK}"
BUILD_TOOLS_VERSION="${BUILD_TOOLS_VERSION:-36.0.0}"
ANDROID_PLATFORM="${ANDROID_PLATFORM:-android-37.0}"
tools="$ANDROID_HOME/build-tools/$BUILD_TOOLS_VERSION"
platform="$ANDROID_HOME/platforms/$ANDROID_PLATFORM/android.jar"
mkdir -p build/sdk/{classes,dex,gen}
"$tools/aapt2" compile --dir app/src/main/res -o build/sdk/resources.zip
python3 - <<'PYTHON'
from pathlib import Path
s=Path('app/src/main/AndroidManifest.xml').read_text()
Path('build/sdk/AndroidManifest.xml').write_text(s.replace('<manifest ', '<manifest package="org.offlineos.calculator" ', 1))
PYTHON
"$tools/aapt2" link -o build/sdk/resources.apk -I "$platform" --manifest build/sdk/AndroidManifest.xml --java build/sdk/gen --min-sdk-version 26 --target-sdk-version 37 --version-code 1 --version-name 0.1.0 --rename-manifest-package org.offlineos.calculator build/sdk/resources.zip
find app/src/main/java build/sdk/gen -name '*.java' > build/sdk/sources.txt
"$JAVA_HOME/bin/javac" -source 8 -target 8 -bootclasspath "$platform:$tools/core-lambda-stubs.jar" -d build/sdk/classes @build/sdk/sources.txt
"$JAVA_HOME/bin/jar" cf build/sdk/classes.jar -C build/sdk/classes .
"$tools/d8" --release --min-api 26 --lib "$platform" --output build/sdk/dex build/sdk/classes.jar
python3 - <<'PY'
import zipfile
from pathlib import Path
with zipfile.ZipFile('build/sdk/resources.apk') as src, zipfile.ZipFile('build/sdk/unsigned.apk','w',zipfile.ZIP_DEFLATED) as dst:
    for item in src.infolist():
        dst.writestr(item,src.read(item.filename))
    for path in Path('build/sdk/dex').glob('*.dex'):
        dst.write(path,path.name)
PY
"$tools/zipalign" -f 4 build/sdk/unsigned.apk build/sdk/aligned.apk
# This is a local test certificate, never a release identity.
if [ ! -f build/sdk/debug.keystore ]; then
    "$JAVA_HOME/bin/keytool" -genkeypair -keystore build/sdk/debug.keystore -storepass android -keypass android -alias androiddebugkey -dname 'CN=Android Debug,O=Android,C=US' -keyalg RSA -keysize 2048 -validity 10000
fi
"$tools/apksigner" sign --ks build/sdk/debug.keystore --ks-pass pass:android --key-pass pass:android --out build/LittleCalculator-0.1.0-test.apk build/sdk/aligned.apk
"$tools/apksigner" verify build/LittleCalculator-0.1.0-test.apk
