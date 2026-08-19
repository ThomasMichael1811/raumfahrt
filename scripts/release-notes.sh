#!/usr/bin/env bash
set -euo pipefail

prev_tag=$(git describe --tags --abbrev=0 2>/dev/null || true)

if [ -n "$prev_tag" ]; then
    range="${prev_tag}..HEAD"
else
    range="HEAD"
fi

out="RELEASE_NOTES.md"
: > "$out"

{
    echo "# Release Notes"
    echo
    if [ -n "$prev_tag" ]; then
        echo "Änderungen seit ${prev_tag}:"
    else
        echo "Erste Release Notes:"
    fi
    echo
    for section in "feat:Neue Features" "fix:Bugfixes" "refactor:Refactoring" "docs:Dokumentation" "test:Tests" "chore:Wartung"; do
        prefix="${section%%:*}"
        heading="${section#*:}"
        matches=$(git log --pretty=format:"- %s" "$range" | grep -E "^\- ${prefix}(\([^)]*\))?[!]?:" | sed "s/^- ${prefix}(\([^)]*\))?[!]?://" || true)
        if [ -n "$matches" ]; then
            echo "## ${heading}"
            echo
            echo "$matches"
            echo
        fi
    done
} >> "$out"

echo "Release Notes geschrieben: ${out}"