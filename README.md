# AddressAlarm

AddressAlarm is an open-source Android app that helps anyone avoid risky, curious, or problematic addresses. The app runs entirely on-device, watching for locations through Android accessibility services and alerting you whenever a monitored app shows an address on your personal watchlist.

Originally designed with gig workers in mind, AddressAlarm now supports law enforcement, social workers, real estate agents, contractors, utility workers, and the public at large.

**Latest build:** https://github.com/TohnJravolta/AddressAlarm/releases/tag/v0.2.42

---

## Highlights in v0.2.42

- Per-app monitoring controls let you choose exactly which navigation, gig, or messaging apps AddressAlarm watches.
- SQLCipher-backed storage keeps the watchlist encrypted with a key that only the device can unlock.
- Delete confirmations (single and bulk) prevent accidental data loss.
- The in-app support dialog links directly to the maintainer's Ko-fi for voluntary support.
- A first-run license screen bundles the full LICENSE text with a plain-language summary so acceptance is stored locally.

---

## 🎥 Setup walkthrough & live demo

[Watch the initial configuration walkthrough and live demo on YouTube](https://www.youtube-nocookie.com/embed/EpV65R8QoEg?si=93ZitphvSeRuii4y).
The video covers downloading the APK, granting accessibility permissions, enabling notifications, tuning alerts, and shows AddressAlarm catching a flagged location in real time so you can follow along from a fresh install.

---

## 📱 Screenshots (Coming Soon)
*(Add screenshots here once you have a stable build to show)*
## Core features

- On-device matching: no data or addresses ever leave your phone.
- Custom flags and tags: note why a location matters so you can act with context.
- Safety-first alerts: debounced overlays and gig-app warnings reduce noise without hiding risk.
- Import and export: back up your data or move it between devices.

---

## Getting started

### Requirements
- Android Studio (latest stable)
- JDK 17 or newer
- Gradle (bundled with Android Studio)

### Build and run
1. Clone the repository:
   ```bash
   git clone https://github.com/TohnJravolta/AddressAlarm.git
   cd AddressAlarm
   ```
2. Open the project in Android Studio and let Gradle sync.
3. Build and run on an Android device or emulator.

See the [`docs/`](docs) directory for more help:
- [Overview](docs/OVERVIEW.md) - Project background and audience.
- [Installation](docs/INSTALLATION.md) - Sideloading and accessibility walkthroughs for popular devices.

---

## Privacy and safety

- No auto-decline or accept: you always decide what happens next.
- No server logging: everything remains local to the device.
- Assistive only: AddressAlarm surfaces reminders; it never acts on your behalf.

---

## Roadmap

See [ROADMAP.md](ROADMAP.md) for upcoming milestones and planned enhancements.

---

## Contributing

Review [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines, coding standards, and the pull-request process. Need help? Visit [SUPPORT.md](SUPPORT.md) for support options, including responsible disclosure steps.

---

## License

Licensed under the GNU AGPL-3.0.  
Copyright (C) 2025 TohnJravolta and AddressAlarm contributors.

See the [LICENSE](LICENSE) file for details.
