# NavHide

NavHide is a lightweight app for hiding the navigation bar on Samsung One UI devices using Shizuku.

## Requirements

- Android 10 or later
- [Shizuku](https://github.com/rikkaapps/shizuku) installed and running

## How It Works

NavHide uses Shizuku to execute system status bar commands:

### Hide Navigation Bar
```bash
cmd statusbar disable-for-setup true
cmd statusbar send-disable-flag home recents
```

### Show Navigation Bar
```bash
cmd statusbar send-disable-flag none
cmd statusbar disable-for-setup false
```

## Issues

- The navigation bar will reappear after a device restart

## License

This project is licensed under the [MIT License](LICENSE).
