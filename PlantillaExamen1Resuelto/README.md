# PlantillaExamen1

Plantilla Android con Jetpack Compose para la materia de Programación Móviles.

## Requisitos previos

| Componente | Versión mínima |
|------------|----------------|
| Android Studio | Quail o superior |
| JDK | 11 o superior |
| SDK Android | API 37 (compileSdk / targetSdk) |
| Dispositivo / emulador | Android 13 (API 33) como mínimo |

> **Nota:** El proyecto usa AGP 9.3.2, Kotlin 2.2.10 y Gradle 9.5.0. Android Studio descarga el wrapper automáticamente, no es necesario instalar Gradle por separado.

## Abrir el proyecto en Android Studio

1. Abrir Android Studio.
2. Seleccionar **File > Open** y navegar hasta la carpeta raíz del proyecto (`PlantillaExamen1`).
3. Esperar a que Gradle sincronice las dependencias (barra de progreso inferior derecha).

> Si Android Studio pregunta sobre el Gradle wrapper, aceptar. No modificar la configuración de Gradle que trae el proyecto.

## Ejecutar la app

1. Conectar un dispositivo Android por USB con depuración activada, o iniciar un emulador (**Tools > Device Manager**).
2. Hacer clic en el botón **Run** (triángulo verde) o presionar `Shift + F10`.
3. La app `PlantillaExamen1` se instalará y abrirá automáticamente en el dispositivo seleccionado.

## Dispositivo con SDK menor al mínimo

Si el dispositivo o emulador tiene una versión de Android inferior a API 33, se puede reducir el `minSdk` en `app/build.gradle.kts`:

```kotlin
defaultConfig {
    minSdk = 24  // o la versión que necesites
}
```

Después de cambiar, sincronizar el proyecto con **File > Sync Project with Gradle Files**.

> **Nota:** Al reducir el `minSdk`, algunas funciones de Compose o Jetpack pueden no estar disponibles. Revisar la compatibilidad de las APIs usadas.

## Solución de problemas comunes

**"Could not resolve..." durante la sincronization:**
Verificar conexión a internet. Las dependencias se descargan de Google y Maven Central.

**"Minimum supported Gradle version is X":**
Actualizar Android Studio a la última versión estable desde **Help > Check for Updates**.

**"SDK not found":**
Ir a **File > Project Structure > SDK Location** y verificar que la ruta del Android SDK sea correcta (por defecto: `C:\Users\<usuario>\AppData\Local\Android\Sdk`).
