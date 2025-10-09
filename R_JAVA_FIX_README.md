# R.java Generation Fix - README

## What Was Wrong?
A manual `R.java` file was created in `app/src/main/java/org/addressalarm/R.java`, which should NEVER exist in the source directory. The Android build system automatically generates this file.

## What Was Fixed?
1. **Moved the manual R.java file** to `R.java.backup` at the project root
2. **Updated .gitignore** to prevent R.java files from being committed to git
3. **Added rules** to ignore all auto-generated Android files

## How R.java Works
- R.java is **automatically generated** by the Android build system
- It's created in: `app/build/generated/source/r/debug/org/addressalarm/R.java`
- It contains resource IDs from your XML files (layouts, strings, ids, etc.)
- It should **NEVER** be manually edited or committed to version control

## To Rebuild Your Project
1. In Android Studio: **Build** → **Clean Project**
2. Then: **Build** → **Rebuild Project**
3. The R.java file will be automatically generated with all correct resource IDs

## If Errors Persist
If you still see "Unresolved reference" errors after rebuilding:
1. **File** → **Invalidate Caches** → **Invalidate and Restart**
2. Delete the `.gradle` and `build` folders
3. Sync project with Gradle files
4. Rebuild project

## Important Notes
- ✅ R.java is now properly ignored by git
- ✅ It will auto-generate on every build
- ✅ All resource IDs will be automatically included
- ❌ NEVER manually create or edit R.java files
- ❌ NEVER commit R.java files to version control

---
Generated: 2025-01-08
