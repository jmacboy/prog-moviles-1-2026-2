# PracticaRoomConAgentes — Estructura del proyecto

App Android (Jetpack Compose + Room) que gestiona una lista de contactos,
estructurada en torno a MVVM con una capa de repositorio. Módulo Gradle único: `:app`.

## Build y configuración

- `settings.gradle.kts` → `rootProject.name = "PracticaRoom"`, incluye solo `:app`.
- Paquete / `applicationId`: `com.example.practicaroom`
- `compileSdk` 37, `minSdk` 33, `targetSdk` 37, Java 11.
- Plugins: Android Application, Kotlin Compose, KSP (compilador de Room).
- Dependencias: Compose BOM + Material 3, Navigation Compose,
  Lifecycle/ViewModel Compose, Room 3 (`androidx.room3`) 3.0.3, Coroutines.
- Catálogo de versiones: `gradle/libs.versions.toml`.

Comandos (ejecutar desde la raíz del proyecto):

```text
gradlew.bat assembleDebug          # compila el APK debug
gradlew.bat testDebugUnitTest      # tests unitarios JVM
gradlew.bat connectedAndroidTest   # tests instrumentados (requiere dispositivo/emulador)
```

## Estructura de fuentes

```text
PracticaRoomConAgentes/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle/libs.versions.toml
├── AGENTS.md
└── app/
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/practicaroom/
        │   │   ├── MainActivity.kt              # punto de entrada + NavHost
        │   │   ├── db/
        │   │   │   ├── AppDatabase.kt           # base de datos Room (singleton)
        │   │   │   ├── dao/ContactDao.kt        # consultas (suspend)
        │   │   │   └── entities/Contact.kt      # data class @Entity
        │   │   ├── repositories/
        │   │   │   └── ContactRepository.kt     # acceso a la BD para el VM
        │   │   └── ui/
        │   │       ├── vms/ContactListViewModel.kt  # holder de estado (StateFlow)
        │   │       ├── screens/
        │   │       │   ├── ListScreen.kt        # lista + búsqueda + borrado
        │   │       │   └── FormScreen.kt        # formulario crear / editar
        │   │       └── theme/                   # Color.kt, Theme.kt, Type.kt
        │   └── res/                             # values, drawable, mipmap, xml
        ├── test/java/com/example/practicaroom/        # tests unitarios JVM
        └── androidTest/java/com/example/practicaroom/ # tests instrumentados
```

## Arquitectura y flujo de datos

```text
MainActivity → NavigationApp (NavHost)
    └─ composable("home")   → ListScreen(navController, vm)
    └─ composable("second") → FormScreen(navController, vm)
                ↓
ContactListViewModel  (expone uiState: StateFlow<ContactUiState>)
                ↓
ContactRepository(context)   // funciones suspend
                ↓
AppDatabase.getDatabase(context).contactDao()   // Room 3
                ↓
tabla contact
```

- `ContactUiState` contiene `contactList`, `selectedContact` y `searchText`.
  `selectedContact.id == 0` significa "contacto nuevo"; cualquier otro id significa "editando".
- La misma instancia de `ContactListViewModel` se crea en `NavigationApp` y se
  pasa a ambas pantallas, de modo que el estado se comparte entre navegaciones.
- Todas las funciones del DAO y del repositorio son `suspend`; el ViewModel las
  llama dentro de `viewModelScope.launch`.
- Las rutas de navegación son strings simples: `"home"` (lista) y `"second"` (formulario).

## Convenciones

- Un `@Composable` reutilizable por archivo, con el nombre del archivo igual al
  del composable.
- Agregar al menos un `@Preview` para cada componente personalizado con
  `@Composable` que se cree; mantener el preview junto al componente cuando sea
  posible.
- Las pantallas viven en `ui/screens`, los holders de estado en `ui/vms`, las
  data classes en `db/entities`, el acceso a datos en `db/dao` y el punto de
  entrada de persistencia en `repositories`.
- Crear todas las entidades de Room y los modelos que representen relaciones
  (`@Relation`) dentro de `db/entities`; no definirlos en `dao` ni en
  `repositories`.
- Mantener separados el modelo de estado de pantalla y la entidad de base de
  datos. Los modelos de UI van en `ui`; las entidades Room contienen solo datos
  persistidos. Mapear entre ambos en el límite del repositorio y no agregar
  propiedades de presentación a entidades Room.
- Importar los símbolos de otros paquetes y referenciarlos por su nombre simple;
  no usar nombres de paquete totalmente cualificados en los puntos de uso. Esto
  también aplica a las anotaciones (por ejemplo, `@Ignore`).
- Mantener las llamadas a funciones anidadas en un máximo de dos niveles; si una
  llamada supera ese nivel, extraer el resultado intermedio a una variable local.
- Los textos y etiquetas de la UI están en español (copia existente de la app).
- Mantener entidades, DAO y repositorio de Room sincronizados al agregar un campo.

## Detalles a tener en cuenta

- `ContactRepository` convierte el `List<Contact>` de Room a `ArrayList<Contact>`
  con `as ArrayList<Contact>`. Esto funciona solo porque la implementación
  generada de Room devuelve un `ArrayList`; conviene mapear con
  `ArrayList(dao.getAll())` si se modifica ese código.
- `AppDatabase.getDatabase()` crea una instancia nueva en cada llamada (sin
  guardia de singleton/volatile) y no tiene migraciones (`version = 1`).
- Las coroutines están fijas a `kotlinx-coroutines-android:1.3.9` directamente en
  `app/build.gradle.kts` en lugar del catálogo de versiones.
