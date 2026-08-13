# Programación de Aplicaciones Móviles I

Repositorio de prácticas de la materia **Programación de Aplicaciones Móviles I**, ciclo 2026-2.

## Contenido

El repositorio contiene dos proyectos Android independientes:

| Proyecto | Descripción |
| --- | --- |
| [`PracticaHolaMundo`](./PracticaHolaMundo) | Aplicación introductoria con Jetpack Compose, un mensaje de bienvenida y un botón que muestra un `Toast`. |
| [`PracticaFormLogin`](./PracticaFormLogin) | Formulario de usuario y contraseña con validación local de las credenciales `admin` / `admin`. |

Cada práctica debe abrirse como un proyecto separado en Android Studio. No existe un proyecto Gradle raíz que agrupe ambas aplicaciones.

## Tecnologías

- Kotlin 2.2.10
- Android Gradle Plugin 9.3.1
- Gradle 9.5.0 mediante Gradle Wrapper
- Jetpack Compose y Material 3
- Compile SDK y Target SDK 37
- Min SDK 33 (Android 13 o superior)
- Compatibilidad de Java 11

## Requisitos

- Android Studio actualizado
- Android SDK 37 instalado
- JDK compatible con la configuración de Gradle
- Un emulador o dispositivo Android con API 33 o superior

## Ejecutar desde Android Studio

1. Cloná el repositorio.
2. Abrí `PracticaHolaMundo` o `PracticaFormLogin` como proyecto independiente.
3. Esperá a que Android Studio complete la sincronización de Gradle.
4. Seleccioná un emulador o conectá un dispositivo Android con API 33 o superior.
5. Elegí la configuración `app` en la barra de ejecución.
6. Presioná **Run** para compilar, instalar y ejecutar la aplicación.

Para ejecutar las pruebas, abrí la ventana **Run > Run Tests** y seleccioná la configuración de pruebas del módulo `app`.

## Credenciales de la práctica de login

`PracticaFormLogin` es una práctica educativa y no implementa autenticación real ni un backend. La validación actual utiliza valores definidos en `MainActivity.kt`:

```text
Usuario: admin
Contraseña: admin
```

No utilices esta implementación en un entorno productivo.

## Estructura de cada proyecto

```text
<practica>/
├── app/
│   └── src/
│       ├── androidTest/    # Pruebas instrumentadas
│       ├── main/           # Código Kotlin, recursos y manifest
│       └── test/            # Pruebas unitarias
├── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
└── settings.gradle.kts
```

Las pruebas incluidas actualmente son las generadas por la plantilla inicial de Android Studio.
