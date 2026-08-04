# NavHide

NavHide is a lightweight app for disabling the navigation bar trigger area using Shizuku.

## Requirements

- Android 10 or later
- Either:
  - [Shizuku](https://github.com/rikkaapps/shizuku) installed and running
  - ADB access to execute the commands yourself

## How It Works

NavHide uses Shizuku to execute system status bar commands. You can also run these commands with ADB instead of installing the app:

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

## Important

- You need to enable swipe gestures before hiding the navigation bar, otherwise it won't work

## Issues

- The navigation bar will reappear after a device restart
- Empty navigation area in apps persist if you have gesture hint enabled

## Tested On

- One UI 8.5
- LineageOS 21

## License

This project is licensed under the [MIT License](LICENSE).
