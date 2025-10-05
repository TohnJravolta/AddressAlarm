# AddressAlarm Installation Guide

This guide walks you through installing the AddressAlarm Android application from a manually downloaded APK and enabling the accessibility service so alerts can appear over supported apps.

> **Note:** A signed release APK is not yet published. Once available, download it from the project releases page.
>
> **Temporary app label:** The test build currently installs with the launcher name **FlagDrive**. When enabling permissions or accessibility, look for "FlagDrive (AddressAlarm)" until the final branding ships.

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

The steps vary slightly between Android versions and manufacturers. Use the walkthrough below that matches your device. In each flow, the service appears as **FlagDrive** or **FlagDrive (AddressAlarm)** until the production name ships.

### Google Pixel (Android 14 and Android 13)
1. Open the **Settings** app.
2. Tap **Accessibility**.
3. Choose **Downloaded apps** (Android 14) or **Downloaded services** (Android 13).
4. Select **FlagDrive (AddressAlarm)**.
5. Turn on the **Use FlagDrive** toggle.
6. Review the on-screen explanation and tap **Allow** to grant the accessibility permission.
7. When prompted, confirm you understand that the service can view your screen so overlays can appear.

### Google Pixel (Android 12 and earlier)
1. Open **Settings** and scroll to **Accessibility**.
2. Under **Downloaded services**, locate **FlagDrive**.
3. Toggle the switch to **On**.
4. Tap **OK** or **Allow** on the confirmation dialog to grant screen-observation access.

### Samsung Galaxy (One UI 6 on Android 14)
1. Go to **Settings** and open **Accessibility**.
2. Tap **Installed apps** (sometimes labeled **Downloaded apps**).
3. Choose **FlagDrive (AddressAlarm)**.
4. Enable the **On** switch and accept the permission prompts.
5. When Samsung shows the "Allow FlagDrive to have full control of your device" dialog, tap **Allow**.
6. Back out to **Accessibility → Advanced settings → Accessibility button** if you want quick access to disable the service later.

### Samsung Galaxy (One UI 5 and earlier)
1. Open **Settings → Accessibility**.
2. Scroll to **Screen reader & visibility enhancements**, then select **Installed services**.
3. Tap **FlagDrive**.
4. Slide the toggle to **On** and confirm the warning dialog.
5. Some older devices ask for an additional confirmation to allow the service after reboot—choose **Allow**.

### Other devices / troubleshooting tips
- If your OEM nests accessibility under **System** or **Additional settings**, search the Settings app for "Accessibility".
- If the service toggles itself off after a reboot, revisit the screen and re-enable it. Some OEMs require you to lock the app in the recent-apps view to keep the service active.
- Verify battery optimizations are disabled if alerts stop showing. Many devices expose this under **Settings → Apps → FlagDrive → Battery → Unrestricted**.
- After enabling the service, return to the app and confirm the status indicator shows **Service active**.

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
