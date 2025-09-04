# AddressAlarm 🚨

**AddressAlarm** is an open-source Android app that helps gig drivers (DoorDash, Spark, etc.) flag and avoid risky or problematic addresses.  
It runs **100% on-device**, watching order screens through Accessibility, and alerts you when a customer, address, or location matches your personal "do-not-serve" or "needs caution" list.

---

## ✨ Features

- **On-device matching** – No data leaves your phone  
- **Custom flags & tags** – Dangerous, sketchy, loose pets, tip baiting, etc.  
- **Debounced alerts** – No notification spam  
- **Per-app visibility** – Works only on apps you enable  
- **Driver-friendly UI** – Large text, simple menus  
- **Import/Export** – Save and restore your flagged addresses  

---

## 📱 Screenshots (Coming Soon)
*(Add screenshots here once you have a stable build to show)*

---

## 🚀 Getting Started

### Requirements
- Android Studio (latest stable)  
- JDK 17+  
- Gradle (bundled in Android Studio)  

### Build & Run
1. Clone this repo:
   ```bash
   git clone https://github.com/TohnJravolta/AAT.git
   cd AAT
   ```
2. Open the project in Android Studio  
3. Let Gradle sync  
4. Build & Run on an Android device or emulator  

---

## 🛡️ Privacy & Safety

- **No auto-decline/accept**: The driver always makes the final choice.  
- **No server logging**: Data stays private, local-only.  
- **Assistive only**: This tool is meant as a memory aid, not automation.  

---

## 🛠 Roadmap

- [x] Basic address/name flagging  
- [x] Debounce & cooldown alerts  
- [ ] Fuzzy matching (e.g., Apt # variations)  
- [ ] Encrypted database storage  
- [ ] Voice/TTS alerts  
- [ ] F-Droid release  

---

## 🤝 Contributing

Contributions are welcome! Please:  
1. Fork the repo  
2. Create a feature branch  
3. Submit a Pull Request with a clear description/screenshots  

Issues and suggestions can be filed in the [Issues tab](../../issues).  

---

## 📜 License

Licensed under the **GNU AGPL-3.0**.  
Copyright (C) 2025 **TohnJravolta and AddressAlarm Contributors**.

See the [LICENSE](LICENSE) file for details.
