# AddressAlarm Privacy Policy

_Last updated: February 19, 2025_

AddressAlarm is an open-source Android application that operates entirely on your device. This policy explains what data the app accesses and how it is handled.

## Data collection and processing
- **No cloud services:** AddressAlarm does not transmit, store, or process your data on external servers.
- **On-device storage:** Flagged addresses, notes, and tags are stored in the app’s local database. You control when to export or delete this data.
- **Accessibility access:** The app reads screen content via Android’s Accessibility Service solely to detect address strings. This information is processed transiently and not retained unless it matches an entry you created.
- **Diagnostics:** The open-source build includes no analytics or crash reporting SDKs. If you sideload the app from this repository, no telemetry is collected.

## Permissions
AddressAlarm requests the following Android permissions:
- **Accessibility Service permission:** Required to observe text on screen for address matching.
- **Overlay/Draw over other apps (if enabled):** Used to display warning banners on top of supported apps.
- **File access (optional):** Used when you import or export address lists.

## Your choices
- You can disable the Accessibility Service at any time from Android settings.
- You can delete your address list from within the app or by clearing the app’s storage.
- Exports are stored wherever you choose on your device or cloud drive; manage them according to your privacy needs.

## Open-source transparency
The full source code is available at <https://github.com/TohnJravolta/AAT>. You may audit the implementation or build the app yourself to confirm these privacy characteristics.

## Contact
For questions about this policy or to report a privacy concern, open an issue at <https://github.com/TohnJravolta/AAT/issues> or email the maintainers listed in `SUPPORT.md`.

By using AddressAlarm you agree to this privacy policy. Continued use after updates constitutes acceptance of the revised policy.
