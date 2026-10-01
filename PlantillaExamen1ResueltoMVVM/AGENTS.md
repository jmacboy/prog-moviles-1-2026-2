# Android Project Guidelines

## Compose Components

Follow a component-oriented structure when working with Jetpack Compose.

- Each reusable `@Composable` should be declared in its own Kotlin source file.
- Keep the file name consistent with the composable name.
- Prefer small, focused composables over large UI functions.
- A screen may compose multiple smaller components.
- Keep UI-specific state close to the component that owns it unless it is required by the parent.

For example:

```text
ui/
├── OrderScreen.kt
├── DrinkNameField.kt
├── SizeSelector.kt
├── MilkSelector.kt
├── ExtrasSelector.kt
└── OrderSummary.kt
```

## Compose Diagnostics

The project uses lightweight Logcat diagnostics for Compose components.

Every `@Composable` introduced or modified in the project should emit a
debug message when it enters composition.

Use the `ComposeDebug` Logcat tag and the following naming convention:

```text
<ComposableName>_loaded
```

Example:

```kotlin
@Composable
fun OrderSummary() {
    Log.d("ComposeDebug", "OrderSummary_loaded")

    // ...
}
```

Use the composable's exact function name in the log message.

Keep these diagnostics close to the beginning of the composable function.
