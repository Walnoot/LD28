import os
import sys

if sys.platform == 'win32':
	os.system("java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.imagepacker.TexturePacker2 textures LD28-android/assets/")
else:
	os.system("java -cp gdx.jar:gdx-tools.jar com.badlogic.gdx.tools.imagepacker.TexturePacker2 textures LD28-android/assets/")