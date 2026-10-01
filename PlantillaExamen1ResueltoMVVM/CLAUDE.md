# Project Development Conventions

## UI Structure

Jetpack Compose UI should follow a simple, modular structure.

When introducing a new composable:

1. Create a dedicated Kotlin file for the composable.
2. Name the file after the composable.
3. Keep the composable focused on a single UI responsibility.
4. Compose larger screens from these smaller UI elements where appropriate.

Avoid placing multiple independent composable components in the same file.

Example:

```text
ui/
    OrderScreen.kt
    DrinkNameField.kt
    SizeSelector.kt
    MilkSelector.kt
    ExtrasSelector.kt
    OrderSummary.kt
```

## Compose Logging

Use lightweight Logcat messages to make UI composition easier to inspect
during development.

Each composable should log a message when it enters composition.

Use:

```kotlin
Log.d("ComposeDebug", "<ComposableName>_loaded")
```

The placeholder must be replaced with the exact composable function name.

For example:

```kotlin
@Composable
fun SizeSelector() {
    Log.d("ComposeDebug", "SizeSelector_loaded")

    // ...
}
```

Apply this convention consistently to newly created or modified composables.
Do not use a different tag or naming format for these messages.
