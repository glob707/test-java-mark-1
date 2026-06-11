#!/bin/bash
# Usage:
#   ./scripts/init-project.sh ../my-new-project com.mycompany.myapp
#
# First argument — target project path
# Second (optional) — new package name, defaults to com.test.mark1

set -euo pipefail

TEMPLATE_DIR="$(cd "$(dirname "$0")/.." && pwd)"
TARGET_DIR="${1:?Specify target project path}"
NEW_PACKAGE="${2:-com.test.mark1}"

OLD_ARTIFACT="test-java-mark-1"
NEW_ARTIFACT="$(basename "$TARGET_DIR")"
OLD_PACKAGE_DIR="com/test/mark1"
NEW_PACKAGE_DIR="${NEW_PACKAGE//.//}"

echo "→ Copying template to $TARGET_DIR"
mkdir -p "$TARGET_DIR"
rsync -a --exclude='target' --exclude='.git' --exclude='.allure' --exclude='scripts' "$TEMPLATE_DIR/" "$TARGET_DIR/"

echo "→ Replacing artifactId: $OLD_ARTIFACT → $NEW_ARTIFACT"

if [[ "$(uname)" == "Darwin" ]]; then
    find "$TARGET_DIR" -type f \( -name "*.xml" -o -name "*.md" -o -name "*.yml" -o -name "Dockerfile" -o -name "*.json" \) \
        -exec sed -i '' "s/$OLD_ARTIFACT/$NEW_ARTIFACT/g" {} +
else
    find "$TARGET_DIR" -type f \( -name "*.xml" -o -name "*.md" -o -name "*.yml" -o -name "Dockerfile" -o -name "*.json" \) \
        -exec sed -i "s/$OLD_ARTIFACT/$NEW_ARTIFACT/g" {} +
fi

if [[ "$NEW_PACKAGE" != "com.test.mark1" ]]; then
    echo "→ Replacing package: com.test.mark1 → $NEW_PACKAGE"

    for module in core ui api; do
        for dir in src/main/java src/test/java; do
            SRC="$TARGET_DIR/$module/$dir/com/test/mark1"
            if [ -d "$SRC" ]; then
                DST="$TARGET_DIR/$module/$dir/$NEW_PACKAGE_DIR"
                mkdir -p "$(dirname "$DST")"
                mv "$SRC" "$DST"
                rmdir -p "$TARGET_DIR/$module/$dir/com/test" 2>/dev/null || true
                rmdir -p "$TARGET_DIR/$module/$dir/com" 2>/dev/null || true
            fi
        done
    done

    if [[ "$(uname)" == "Darwin" ]]; then
        find "$TARGET_DIR" -type f -name "*.java" -exec sed -i '' "s/package com\.test\.mark1/package $NEW_PACKAGE/g" {} +
        find "$TARGET_DIR" -type f -name "*.java" -exec sed -i '' "s/import com\.test\.mark1/import $NEW_PACKAGE/g" {} +
    else
        find "$TARGET_DIR" -type f -name "*.java" -exec sed -i "s/package com\.test\.mark1/package $NEW_PACKAGE/g" {} +
        find "$TARGET_DIR" -type f -name "*.java" -exec sed -i "s/import com\.test\.mark1/import $NEW_PACKAGE/g" {} +
    fi

    if [[ "$(uname)" == "Darwin" ]]; then
        find "$TARGET_DIR" -name "testng.xml" -exec sed -i '' "s/com\.test\.mark1/$NEW_PACKAGE/g" {} +
    else
        find "$TARGET_DIR" -name "testng.xml" -exec sed -i "s/com\.test\.mark1/$NEW_PACKAGE/g" {} +
    fi
fi

echo "✓ Done: $TARGET_DIR"
echo ""
echo "  cd $TARGET_DIR"
echo "  mvn test -pl ui -am -Dheadless=true"
