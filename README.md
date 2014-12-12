SLDT's GameEngine
==============

Simple game engine to use with any 2D game. Code entirely developped by Yuri6037. This engine has been made for BrickBroken as a private engine.

Now, it's a public engine, that everyone can download.


Liscence :
- You may not redistribute this engine's code;
- You can download the code and modify it, but only for personnal use;
- You may download compiled versions of this engine and publish your free games everywhere on the net;
- You can redistribute this engine, as a compiled unmodified version only, in a download package, and use it in any of your games.


Riquierements :
- lwjgl.jar (can be downloaded at LWJGL.ORG),
- lwjgl_util.jar (can be downloaded at LWJGL.ORG),
- jinput.jar (can be downloaded at LWJGL.ORG),
- LWJGL natives (can be downloaded at LWJGL.ORG),
- No CPU requierements,
- OpenGL 1.2 Compatible GPU,
- OpenGL 1.2 Drivers installed,
- Windows OR Mac OR Linux System.


To start developping with this engine, you need first to download the example zip assets file.
To make assets, 2 ways :
- Use SLDT's GameEngine SDK (Recommended).
- Use WinRar Archiver or any other archiver software that reads zip or jar (Use at your own risks, no longer supported).

Don't forget to add the sldtBG.png in backgrounds/sldtBG.png in you assets file, otherwise you can't launch your game.


Example assets file explication :
- backgrounds/mainBG.png : Background that displays in any screen, you can edit, you can't remove it;
- backgrounds/sldtBG.png : Powered by background, you can't edit it, you can't remove it;
- buttons/* : Optionnal, used by buttons pre-made screen's components;
- components/* : Optionnal, used by the console screen component;
- fonts/* : All your fonts goes here, "normal.png" is the default loaded font;
- message/* : Optionnal, used by integrated message box system.


Why do the example assets file no longer downloadable ?
Simple, now SLDT's AssetsManager provides you, when you create a new project, a set of default assets !
It generates everything you need to start developing your first game using SLDT's GameEngine !