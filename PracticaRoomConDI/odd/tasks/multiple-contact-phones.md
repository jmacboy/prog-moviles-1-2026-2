# Multiple phone numbers per contact

## Objective
Allow each contact to have multiple phone numbers, keep Room entities free of UI-only state, and manage editable contact fields through a dedicated `ContactUi` model.

## Problem and why
The current `Contact` model stores one `phoneNumber` string, so a contact cannot retain multiple numbers. The requested feature needs a one-to-many storage model and updates across persistence, state, and Compose UI.

## Scope
- Replace the single stored phone field with a Room child entity linked to its contact.
- Preserve the existing version-1 phone value through a database migration.
- Support adding/removing phone inputs while creating or editing a contact.
- Display the contact's phone numbers in the list.
- Keep the Room `Contact` entity limited to persisted fields; represent all contact form/list state in `ContactUi`.
- Keep changes inside `PracticaRoomConAgentes/`.

## Constraints
- Do not move or alter repository structure, other projects, or unrelated files.
- No branch, commit, or other repository-history operation; user explicitly asked not to move anything in the repository.
- UI copy follows the existing Spanish app language; technical identifiers and docs remain English.
- No skill registry is present at `.atl/skill-registry.md`; proceed without project-specific skill standards.
- Forecast: approximately 150 authored lines across implementation and focused tests; delivery strategy `exception-ok` because repository history operations are out of scope.

## Tasks
- [x] M1 — Implement multi-phone persistence, version-1 migration, editing state, and Compose form/list.
- [x] M2 — Separate UI contact state into `ContactUi`, remove the ignored phone list from the Room entity, and update mapping and screens.

## Implementation route
- M2 route: delegated direct, because the model change crosses persistence mapping, ViewModel state, and multiple Compose screens.
- Trigger evidence: several non-trivial Kotlin files must change together; implementation context and consistency should stay in one writer handoff.

## Authorized scope
- `app/src/main/java/com/example/practicaroom/db/entities/Contact.kt`
- `app/src/main/java/com/example/practicaroom/db/entities/ContactPhone.kt`
- `app/src/main/java/com/example/practicaroom/db/entities/ContactWithPhones.kt`
- `app/src/main/java/com/example/practicaroom/db/dao/ContactDao.kt`
- `app/src/main/java/com/example/practicaroom/db/AppDatabase.kt`
- `app/src/main/java/com/example/practicaroom/repositories/ContactRepository.kt`
- `app/src/main/java/com/example/practicaroom/ui/vms/ContactListViewModel.kt`
- `app/src/main/java/com/example/practicaroom/ui/vms/ContactUi.kt`
- `app/src/main/java/com/example/practicaroom/ui/screens/FormScreen.kt`
- `app/src/main/java/com/example/practicaroom/ui/screens/ListScreen.kt`
- `app/src/androidTest/java/com/example/practicaroom/db/` (new focused migration/persistence tests if feasible)
- `app/src/test/java/com/example/practicaroom/` (new focused unit tests if applicable)

## Acceptance criteria
- A contact can have multiple independently editable phone numbers.
- The Room `Contact` entity contains only persisted contact fields and has no UI-only phone-list field or `@Ignore` annotation.
- Contact list/edit UI state uses `ContactUi`, including all contact fields and phone numbers.
- Adding and removing phone inputs works for both new and existing contacts.
- Existing version-1 phone numbers survive migration as the first phone number.
- Contact list displays all numbers for a contact.
- Empty phone rows do not create blank persisted numbers; existing required-field behavior is preserved sensibly.
- Build and applicable tests pass, or environmental limitations are recorded.

## Checks
- Test-first when a meaningful runnable deterministic test exists: observe RED, implement GREEN, refactor while green.
- `gradlew.bat testDebugUnitTest`
- `gradlew.bat assembleDebug`
- Structural readback and scope check confirming no paths outside this project changed.

## Progress and evidence
- M1 implemented with a normalized `contact_phone` table, a Room 3.0.3 version 1-to-2 migration, repository mapping, editable form rows, and list rendering.
- `gradlew.bat testDebugUnitTest` — passed (no unit test cases currently exist in the module).
- `gradlew.bat assembleDebug` — passed.
- Test-first behavior test was not added: this module has no existing test cases or Room test database dependency; migration/device-level verification would require instrumentation.
- Follow-up crash fix: migration foreign key now references `Contact(id)` with the exact table-name casing Room validates; `testDebugUnitTest` and `assembleDebug` passed again.
- Follow-up convention fix: moved the `@Relation` projection `ContactWithPhones` from the DAO file into `db/entities`.
- M2 implemented: `ContactUi` owns all displayed/editable contact fields; repository maps Room projections to/from it, and ViewModel plus both screens no longer use Room `Contact` as UI state. Persistence filters blank phone entries and refuses inserts/updates without valid phones.
- `& .\gradlew.bat testDebugUnitTest` — passed; module currently has no unit test cases. Room schema export warning remains unchanged.
- `& .\gradlew.bat assembleDebug` — passed.
- Native review remains pending: selectorless STATUS requires an intended-untracked selection because this project is untracked inside the parent repository; the runtime exposes no compatible structured capture UI for that schema-bound selection.

## Next step
Resolve the native review's intended-untracked selection before considering review complete; instrumented migration verification remains a possible follow-up.
