# PracticaRoomConAgentes

App Android de lista de contactos construida con **Jetpack Compose** y
**Room**, siguiendo el patrón **MVVM** con una capa de repositorio. Sirve como
práctica de la materia de Programación de Móviles (2026-2).

## Qué hace la app

Permite gestionar contactos guardados en una base de datos local (SQLite vía Room):

- **Lista de contactos** (`ListScreen`): muestra todos los contactos, permite
  buscar por nombre y eliminar contactos.
- **Formulario** (`FormScreen`): crea contactos nuevos o edita uno existente
  (nombre, teléfono, email y dirección).

Desde la lista, el botón **"+"** abre el formulario para crear un contacto;
tocar un contacto existente lo abre en modo edición. Al guardar, se vuelve a la
lista con los cambios aplicados.

## Cómo funciona (flujo de datos)

La app sigue una arquitectura en capas. Los datos fluyen siempre en una sola
dirección, de la UI hacia la base de datos y de vuelta al estado:

```text
UI (Compose)
   │  eventos del usuario (clicks, texto)
   ▼
ContactListViewModel          ← holder del estado (StateFlow)
   │  llama a funciones suspend
   ▼
ContactRepository             ← única puerta de acceso a la BD
   │
   ▼
AppDatabase / ContactDao      ← Room 3 genera el código SQL
   │
   ▼
tabla "contact" en SQLite
```

### 1. Punto de entrada y navegación — `MainActivity.kt`

`MainActivity` carga el tema y llama a `NavigationApp()`, un composable que
define el `NavHost` con dos rutas:

| Ruta      | Pantalla      | Qué muestra              |
| --------- | ------------- | ------------------------ |
| `"home"`  | `ListScreen`  | lista de contactos        |
| `"second"`| `FormScreen`  | formulario crear/editar   |

El `ContactListViewModel` se crea **una sola vez** en `NavigationApp` y se
pasa a ambas pantallas. Esto es clave: ambas pantallas comparten la misma
instancia del ViewModel, por eso el contacto seleccionado en la lista llega
al formulario sin tener que pasarlo por argumentos de navegación.

### 2. Estado — `ContactListViewModel.kt`

El ViewModel es el corazón de la lógica. Expone un único objeto de estado
inmutable:

```kotlin
data class ContactUiState(
    val contactList: ArrayList<Contact>,   // contactos a mostrar
    val selectedContact: Contact,          // contacto en edición (id=0 = nuevo)
    val searchText: String                 // texto del filtro de búsqueda
)
```

Las pantallas solo **leen** ese estado con `collectAsStateWithLifecycle()` y
**envían eventos** al ViewModel (`loadContacts()`, `saveContact()`,
`deleteContact()`, `setSearchText()`, etc.). El ViewModel nunca expone
mutablemente el estado hacia afuera: usa un `MutableStateFlow` privado y lo
actualiza con `_uiState.update { ... }`.

Reglas importantes del ViewModel:

- `selectedContact.id == 0` → se está creando un contacto nuevo.
  Cualquier otro id → se está editando uno existente. El formulario usa esa
  convención para decidir si llama a `addContact` o `updateContact`.
- `saveContact()` solo guarda si nombre y teléfono no están vacíos.
- Tras cada operación de escritura (guardar/borrar), se llama a
  `loadContacts()` para refrescar la lista.
- `setSearchText()` dispara la búsqueda cada vez que cambia el texto.
- Todas las operaciones con la base de datos se lanzan con
  `viewModelScope.launch { ... }` porque son funciones `suspend`.

### 3. Repositorio — `ContactRepository.kt`

El repositorio es el único punto que habla con la base de datos. Recibe el
`Context` (necesario para construir la base) y expone funciones `suspend` que
delegan en el DAO:

- `getContacts()` / `searchContacts(query)` — lecturas
- `addContact(...)` / `updateContact(...)` / `deleteContact(...)` — escrituras

La búsqueda envuelve el texto en comodines: `searchByName("%$query%")`, que
se traduce a un `WHERE name LIKE '%texto%'` en SQL.

### 4. Base de datos — `db/`

Room genera en tiempo de compilación (con KSP) el código SQL a partir de tres
piezas:

- **`entities/Contact.kt`** — la tabla. `@Entity` marca la data class como
  tabla; `id` es la clave primaria autoincremental; `phoneNumber` se mapea a
  la columna `phone_number`.
- **`dao/ContactDao.kt`** — las consultas. Cada método `suspend` lleva una
  anotación (`@Query`, `@Insert`, `@Update`, `@Delete`) con el SQL o la
  operación correspondiente.
- **`AppDatabase.kt`** — une ambas cosas. `getDatabase(context)` construye la
  instancia de Room con el nombre `"app-db"` y expone `contactDao()`.

## Estructura de archivos

```text
PracticaRoomConAgentes/
├── settings.gradle.kts          # rootProject.name = "PracticaRoom", módulo :app
├── build.gradle.kts
├── gradle/libs.versions.toml    # catálogo de versiones
├── AGENTS.md
└── app/
    ├── build.gradle.kts         # SDKs, Compose, KSP, Room 3.0.3, coroutines
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/practicaroom/
        │   │   ├── MainActivity.kt                  # entry point + NavHost
        │   │   ├── db/
        │   │   │   ├── AppDatabase.kt               # @Database Room
        │   │   │   ├── dao/ContactDao.kt            # consultas suspend
        │   │   │   └── entities/Contact.kt          # @Entity (tabla contact)
        │   │   ├── repositories/
        │   │   │   └── ContactRepository.kt         # acceso a la BD
        │   │   └── ui/
        │   │       ├── vms/ContactListViewModel.kt  # lógica + ContactUiState
        │   │       ├── screens/
        │   │       │   ├── ListScreen.kt            # lista, búsqueda, borrado
        │   │       │   └── FormScreen.kt            # crear / editar contacto
        │   │       └── theme/                       # Color.kt, Theme.kt, Type.kt
        │   └── res/                                 # values, drawable, mipmap, xml
        ├── test/java/com/example/practicaroom/        # tests unitarios JVM
        └── androidTest/java/com/example/practicaroom/ # tests instrumentados
```

## Configuración y build

| Aspecto        | Valor                                    |
| -------------- | ---------------------------------------- |
| Paquete        | `com.example.practicaroom`               |
| Módulos        | `:app` (único)                           |
| compileSdk     | 37                                       |
| minSdk / targetSdk | 33 / 37                              |
| Java           | 11                                       |
| UI             | Jetpack Compose + Material 3 (BOM)       |
| Persistencia   | Room 3 (`androidx.room3` 3.0.3) con KSP  |
| Async          | Coroutines                               |
| Navegación     | Navigation Compose                       |

Comandos (desde la raíz del proyecto):

```text
gradlew.bat assembleDebug          # compila el APK debug
gradlew.bat testDebugUnitTest      # tests unitarios JVM
gradlew.bat connectedAndroidTest   # tests instrumentados (requiere dispositivo/emulador)
```

## Convenciones del proyecto

- Un `@Composable` reutilizable por archivo, con el nombre del archivo igual al
  del composable.
- Separación por responsabilidad: pantallas en `ui/screens`, lógica/estado en
  `ui/vms`, acceso a datos en `repositories`, SQL en `db/dao`, tablas en
  `db/entities`.
- Los textos de la UI están en español.
- Al agregar un campo a `Contact`, hay que mantener sincronizados la entity, el
  DAO, el repositorio, el ViewModel (si afecta al estado) y el formulario.

## Detalles a tener en cuenta

Estas son decisiones/deudas de la práctica, útiles para saber qué esperar:

- `ContactRepository` convierte el `List<Contact>` de Room a
  `ArrayList<Contact>` con `as ArrayList<Contact>`. Funciona porque la
  implementación generada por Room devuelve un `ArrayList`, pero lo correcto
  sería mapear con `ArrayList(dao.getAll())`.
- `AppDatabase.getDatabase()` crea una instancia nueva en cada llamada (sin la
  guardia clásica de singleton con `@Volatile` + `INSTANCE`) y no define
  migraciones (`version = 1`), así que cambiar el esquema rompe datos
  existentes.
- Las coroutines están fijas a `kotlinx-coroutines-android:1.3.9` directamente
  en `app/build.gradle.kts`, en lugar de usar el catálogo de versiones.
- La búsqueda se ejecuta en el ViewModel al escribir, sin debounce; con pocos
  contactos no se nota, pero no es ideal para listas grandes.
