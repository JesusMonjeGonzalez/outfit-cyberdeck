# Outfit Cyberdeck

Armario virtual local construido con Kotlin Multiplatform y Compose
Multiplatform. Permite catalogar prendas, conservar sus fotos en el dispositivo
y generar combinaciones mediante reglas de estilo y temporada.

## Características

- Aplicación Android con target de escritorio JVM.
- Persistencia local con SQLDelight, sin backend ni cuentas de usuario.
- Clasificación manual por categoría, ajuste, temporada, estilo y color.
- Generador determinista de outfits.
- Interfaz pixel-art propia con marco de madera y paleta cálida.

## Compilar Android

Requiere JDK 17 y un Android SDK configurado mediante `ANDROID_HOME` o un
`local.properties` local.

```bash
./gradlew :composeApp:assembleDebug
```

El APK debug se genera bajo `composeApp/build/outputs/apk/debug/`.

## Estructura

```text
composeApp/src/
  commonMain/   lógica, SQLDelight e interfaz compartida
  androidMain/  actividad, almacenamiento de imágenes y driver Android
  desktopMain/  entrada de escritorio y driver JVM
```

## Estado

El target Android compila correctamente. El proyecto sigue en desarrollo y
todavía necesita pruebas automatizadas de repositorios, persistencia y
generación de outfits.
