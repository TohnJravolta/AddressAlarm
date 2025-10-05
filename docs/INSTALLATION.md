# AddressAlarm Installation Guide

This guide walks you through installing the AddressAlarm Android application from a manually downloaded APK and enabling the accessibility service so alerts can appear over supported apps.

> **Note:** A signed release APK is not yet published. Once available, download it from the project releases page.

## 1. Prepare your device
1. Use an Android device running Android 8.0 (Oreo) or newer.
2. Ensure you can install apps from unknown sources:
   - Go to **Settings → Apps & notifications → Special app access → Install unknown apps**.
   - Grant permission to the browser or file manager you will use to open the APK.

## 2. Download the APK
1. Visit the AddressAlarm GitHub Releases page: <https://github.com/TohnJravolta/AAT/releases>.
2. Download the latest `addressalarm-<version>.apk` asset to your device.
3. (Optional) Verify the checksum against the value listed in `RELEASE.md` for additional assurance.

## 3. Install the app
1. Open the downloaded APK from your notifications or file manager.
2. Confirm the installation prompts.
3. After installation, tap **Open** to launch AddressAlarm.

## 4. Enable the accessibility service
1. In AddressAlarm, complete any initial onboarding prompts.
2. Navigate to **Settings → Accessibility → Installed services** on your device.
3. Select **AddressAlarm** and toggle it on.
4. Approve the permissions request so the service can observe screen content for addresses.

## 5. Configure your watchlist
1. From the app’s main screen, add addresses you want to flag, including optional notes or tags.
2. Use the import option if you already maintain a CSV or JSON export from another device.

## 6. Test alerts
1. Open a navigation or gig app you enabled in AddressAlarm’s visibility settings.
2. Search for or open a flagged address.
3. Confirm that AddressAlarm shows a warning overlay. Adjust cooldown timers or visibility settings as needed.

## Troubleshooting
- **No overlay appears:** Revisit the accessibility settings to confirm the service is still enabled. Some OEMs disable services after updates or restarts.
- **Too many alerts:** Increase the debounce/cooldown interval in AddressAlarm settings.
- **Need help?** See [SUPPORT.md](../SUPPORT.md) for support options.
