# Reglas básicas para el proyecto Android

## Arquitectura y capas
- Usar una arquitectura basada en capas: UI, ViewModel, Repository y Data sources.
- La capa de UI solo debe encargarse de mostrar estado y enviar eventos; no debe contener lógica de negocio compleja.
- Los ViewModels deben manejar el estado de la pantalla y exponer estados observables al UI.
- Los repositories deben ser la única fuente de acceso a datos, encapsulando APIs, BD, caché y lógica de persistencia.
- Evitar lógica de datos directamente en Activities, Fragments o composables.

## Navegación
- Usar Navigation Component para la navegación entre pantallas.
- Inicializar la navegación con `NavHostController` usando `rememberNavController()` y un solo `NavHost` en la pantalla raíz.
- La `MainActivity` solo debe declarar una sola pantalla vacía como contenedor del `NavHost`.
- No crear pantallas adicionales si el proyecto todavía está en una primera etapa; comenzar con un único destino vacío y expandir luego el grafo.
- Definir todas las rutas en un grafo de navegación centralizado.
- Preferir argumentos tipados y evitar pasar objetos complejos en el bundle cuando sea posible.
- Mantener la navegación declarativa y reutilizable; no hacer navegación manual desde cada vista sin seguir el grafo.

## ViewModels y estado
- Cada pantalla principal debe tener un ViewModel asociado.
- El ViewModel no debe depender directamente de la vista ni de referencias de Activity/Fragment.
- Los ViewModels deben usar StateFlow como mecanismo principal para exponer el estado de la pantalla a la UI.
- Exponer estados reactivos con StateFlow para que la UI observe cambios y reaccione de forma segura.
- Separar eventos de UI (acciones del usuario) y estados de pantalla (loading, success, error, empty).
- Mantener el ViewModel libre de llamadas a la red o a la base de datos directas.

## Repositories y acceso a datos
- Crear repositories para cada dominio o fuente de datos que necesite acceso.
- Centralizar la lógica de obtención, caché y transformación de datos en el repository.
- Los repositories deben devolver modelos de dominio o UI, no entidades de la capa de infraestructura directamente.
- Los métodos del repository son `suspend` y devuelven el dato directamente (por ejemplo `suspend fun getBookList(): BookList`). No envolver la respuesta en `Result`, `runCatching` ni tipos similares.
- El repository no captura excepciones: los errores de red o de persistencia se propagan hacia el ViewModel.
- Crear el servicio de Retrofit una sola vez por repository (`private val service by lazy { ... }`), no en cada llamada.

## Manejo de errores en el ViewModel
- El ViewModel llama al repository dentro de `viewModelScope.launch` y envuelve la llamada en `try/catch (e: Exception)`.
- En el `try`, asignar el estado de éxito (o `Empty` si la lista viene vacía).
- En el `catch`, asignar el estado `Error` con un mensaje claro para el usuario (por ejemplo "Error al traer los libros. Intente nuevamente") y registrar la excepción con `e.printStackTrace()`.
- No mostrar en la UI el mensaje técnico de la excepción (`e.message`).
- Asignar el estado `Loading` antes de lanzar la corrutina.

## Coroutines y concurrencia
- Usar coroutines y Flow para operaciones asíncronas y reactivas.
- Las funciones `suspend` de Retrofit y Room ya son main-safe; no envolverlas en `withContext(Dispatchers.IO)`.
- Usar `Dispatchers.IO` solo para trabajo bloqueante que no sea main-safe (lectura de archivos, APIs síncronas).
- Usar Dispatchers.Main solo para actualizaciones de UI.
- Evitar bloqueos de hilos y llamadas síncronas dentro de ViewModels o UI.
- Usar structured concurrency con scopes adecuados y cancelar tareas cuando ya no sean necesarias.

## Previews de Compose
- Crear `@Preview` solo para los siguientes componentes:
  - Las pantallas principales (los destinos del grafo de navegación).
  - Los paneles que representan un solo ítem de una lista.
  - Los paneles que contienen más de dos elementos.
- No crear previews para componentes simples o con dos elementos o menos.
- Las previews deben usar datos de ejemplo y no depender de ViewModels, repositories ni llamadas de red.

## Buenas prácticas generales
- Usar inyección de dependencias para facilitar pruebas y mantener acoplamiento bajo.
- Mantener las clases pequeñas, con responsabilidades claras y nombres descriptivos.
- Hacer que la lógica de negocio esté en los ViewModels/repositories y no en la UI.
- Preferir pruebas unitarias para repositorios y ViewModels siempre que sea posible.
- Documentar decisiones de arquitectura cuando una pantalla o flujo requiera una excepción.

## Regla de oro
- UI -> ViewModel -> Repository -> Data source
- La navegación debe ir por Navigation Component, los datos por repositories y la asincronía por coroutines.
