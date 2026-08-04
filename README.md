# NavHide

NavHide is a lightweight app for hiding the navigation bar on Samsung One UI devices using Shizuku.

## Requirements

- Android 10 or later
- [Shizuku](https://github.com/rikkaapps/shizuku) installed and running

## How It Works

NavHide uses Shizuku to execute system status bar commands. You can also run these commands with ADB:

### Hide Navigation Bar
```bash
adb shell cmd statusbar disable-for-setup true
adb shell cmd statusbar send-disable-flag home recents
```

### Show Navigation Bar
```bash
adb shell cmd statusbar send-disable-flag none
adb shell cmd statusbar disable-for-setup false
```

## Issues

- The navigation bar will reappear after a device restart

## Tested On

- Samsung Galaxy A54 (Android 16)
- I'm not sure if it will work with older One UI or other Android.

## License

This project is licensed under the [MIT License](LICENSE).
