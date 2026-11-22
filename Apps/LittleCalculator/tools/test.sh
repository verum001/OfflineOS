#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
: "${JAVA_HOME:?Set JAVA_HOME to a JDK}"
mkdir -p build/tests
"$JAVA_HOME/bin/javac" --release 8 -d build/tests app/src/main/java/org/offlineos/calculator/{Calculator,Conversions}.java tests/EngineTest.java
"$JAVA_HOME/bin/java" -cp build/tests EngineTest
