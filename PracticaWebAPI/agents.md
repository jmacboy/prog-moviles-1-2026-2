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
- Manejar errores de red o de persistencia en la capa de datos y convertirlos en estados claros para la UI.

## Coroutines y concurrencia
- Usar coroutines y Flow para operaciones asíncronas y reactivas.
- Ejecutar trabajo de red o base de datos en Dispatchers.IO.
- Usar Dispatchers.Main solo para actualizaciones de UI.
- Evitar bloqueos de hilos y llamadas síncronas dentro de ViewModels o UI.
- Usar structured concurrency con scopes adecuados y cancelar tareas cuando ya no sean necesarias.

## Buenas prácticas generales
- Usar inyección de dependencias para facilitar pruebas y mantener acoplamiento bajo.
- Mantener las clases pequeñas, con responsabilidades claras y nombres descriptivos.
- Hacer que la lógica de negocio esté en los ViewModels/repositories y no en la UI.
- Preferir pruebas unitarias para repositorios y ViewModels siempre que sea posible.
- Documentar decisiones de arquitectura cuando una pantalla o flujo requiera una excepción.

## Regla de oro
- UI -> ViewModel -> Repository -> Data source
- La navegación debe ir por Navigation Component, los datos por repositories y la asincronía por coroutines.
