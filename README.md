# OtherAnimations

A utility mod for Minecraft 1.8.9 (Forge) that changes your item animations with customizable modes and viewmodel positioning.

## Showcase

<img width="1600" height="900" alt="showcase" src="https://github.com/user-attachments/assets/fd277177-13eb-4d54-ab02-88551edbbd60" />

## Features

* **10 Animation Modes:** Vanilla, 1.7, Exhibition, ETB, Sigma, Spin, Slide, Swong, Swang, Punch
* **Viewmodel Customization:** Adjust position (X, Y, Z) and rotation (X, Y, Z) in real-time
* **Item Switch Styles:** Disabled, 1.7, 1.8
* **Scale Control:** Adjust item render size
* **Swing Speed:** Control how fast your arm swings
* **Config:** All settings save automatically to `.minecraft/config/otheranimation.cfg`

## How to Build (Compile)

This project requires JDK 8.

### Windows:
```bat
set "JAVA_HOME=C:\Program Files\BellSoft\LibericaJDK-8-Full"
gradlew clean build
```

### Linux:
```bash
export JAVA_HOME=/path/to/jdk8
chmod +x gradlew
./gradlew clean build
```

The compiled .jar will be in `build/libs/OtherAnimations-1.0.jar`.

## How to Install

1. Download the mod.
2. Drop the .jar into your `.minecraft/mods` folder.
3. Run Minecraft 1.8.9 (Forge).
4. Open the GUI with `/otheranimations` in chat.

## Legal Notice

This project was developed for educational purposes. The author is not responsible for any bans or punishments incurred on servers resulting from the use of modifications. Use at your own risk.

https://help.minecraft.net/hc/en-us/articles/4409139065613-Mods-for-Minecraft-Java-Edition
