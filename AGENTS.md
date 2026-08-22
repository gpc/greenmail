# AGENTS.md - greenmail

## Project Overview

The GreenMail plugin runs a mock [GreenMail](https://greenmail-mail-test.github.io/greenmail/) SMTP
server inside a Grails application during development and test, so mail the application sends is
captured instead of delivered. It also ships a controller and GSP view that list captured messages at
`/greenmail`.

- **Language:** Groovy 5.0.8 on Java 21
- **Framework:** Grails 8.x
- **Build System:** Gradle 9.6.0 (with wrapper)
- **Published artifact:** `io.github.gpc:greenmail`
- **Current Version:** 8.0.0-SNAPSHOT
- **License:** Apache 2.0

## Skill Files (Best Practices)

Detailed best practices are documented in `.agents/skills/` (`.claude` is a symlink to `.agents`):

| Skill File                                                                     | Purpose                                              |
|--------------------------------------------------------------------------------|------------------------------------------------------|
| [`repository-structure`](.agents/skills/repository-structure/SKILL.md)         | Canonical directory layout and architectural rules   |
| [`gradle-best-practices`](.agents/skills/gradle-best-practices/SKILL.md)       | Convention plugins, lazy APIs, build structure rules |
| [`plugin-project`](.agents/skills/plugin-project/SKILL.md)                     | Plugin project scope: source code + unit tests only  |
| [`example-apps`](.agents/skills/example-apps/SKILL.md)                         | Example app patterns: integration & functional tests |

**Read these skill files before making structural changes to the repository.**

## Critical Rules

1. **NEVER add code to the root `build.gradle` to configure subprojects.** No `subprojects {}`,
   `allprojects {}`, or `configure()` blocks. All shared configuration goes through convention plugins
   in `build-logic/`.
2. **`build-logic/`, `.agents/`, `.github/workflows/`, `.github/scripts/`, `CONTRIBUTING.md` and
   `docs/src/docs/index.tmpl` are synced from `grails-plugin-template`.** Edits there are overwritten
   by sync PRs. Put repository-specific guidance in this file instead.
3. **The plugin project contains ONLY plugin code and unit tests.** No integration tests, no
   functional tests.
4. **`examples/app1` hosts all integration and functional tests.** It depends on the plugin via
   `implementation project(':greenmail')` and tests it as a real consumer would.
5. **Always use lazy Gradle APIs** (`tasks.register()`, `tasks.named()`, `configureEach`, `provider {}`).

## Repository Structure

```
greenmail/
├── plugin/              # Grails plugin (artifact: greenmail)
│   ├── grails-app/      #   Controller, URL mappings, GSP view, plugin conf
│   └── src/             #   Plugin source code and unit tests
├── examples/app1/       # Example Grails app hosting the integration tests
├── docs/                # Asciidoctor documentation
├── build-logic/         # Gradle convention plugins (composite build)
├── code-coverage/       # Aggregated JaCoCo report
├── .github/workflows/   # CI, release, release-notes, contributor and version updates
├── build.gradle         # Root build file (docs + root-publish ONLY)
├── settings.gradle      # Multi-project settings
├── gradle.properties    # Version properties
└── project.yml          # Single source of project metadata (POM, docs, version index)
```

## Build and Test Commands

```bash
# Full build (compile + unit tests + example-app integration tests)
./gradlew build

# Unit tests (plugin module)
./gradlew :greenmail:test

# Integration tests (example app)
./gradlew :app1:integrationTest

# Run the example app, then browse http://localhost:8080/greenmail
./gradlew :app1:bootRun

# Documentation -> build/docs
./gradlew docs

# Code style checks only
./gradlew codeStyle

# Skips
./gradlew build -PskipTests
./gradlew build -PskipCodeStyle
```

## SDK Requirements

Tool versions are pinned in `.sdkmanrc` and are **build-critical**: `config.compile` reads the Java
major version from it and fails if it is missing. Run `sdk env install` before building — building on a
newer JDK makes the Groovy compiler emit bytecode that JaCoCo cannot analyse.

- Java: `21.0.7-librca`
- Gradle: `9.6.0`
- Groovy: `5.0.8`

## Architecture

1. **`GreenmailGrailsPlugin`** registers a `greenMail` bean when the plugin is enabled, starts the
   server in `doWithApplicationContext()` and stops it in `onShutdown()`. It reads
   `grails.plugin.greenmail.disabled` and `grails.plugin.greenmail.ports.smtp` (default 3025, from
   GreenMail's `ServerSetupTest.SMTP.port`).
2. **`GreenMail`** subclasses `com.icegreen.greenmail.util.GreenMail` with Groovy-friendly accessors.
   It is the plugin's **public API**, so `com.icegreen:greenmail` is an `api` dependency.
3. **`MimeMessageExtension`** is a Groovy extension module adding `to`/`tos`/`cc`/`ccs`/`bcc`/`bccs`
   to `jakarta.mail.internet.MimeMessage`, registered via
   `META-INF/services/org.codehaus.groovy.runtime.ExtensionModule`.
4. **`GreenmailController`** + `GreenmailUrlMappings` + `views/greenmail/list.gsp` serve the message
   list. `withFormat` handles the `html` and `js` formats, so JSON is served from `.js` — a
   `.json` extension falls back to the HTML block. See the docs caveat.

### Core Classes

| Class                     | Location                                                        | Purpose                       |
|---------------------------|-----------------------------------------------------------------|-------------------------------|
| `GreenmailGrailsPlugin`   | `plugin/src/main/groovy/grails/plugin/greenmail/`               | Plugin descriptor and lifecycle |
| `GreenMail`               | `plugin/src/main/groovy/grails/plugin/greenmail/`               | Public API over GreenMail     |
| `MimeMessageExtension`    | `plugin/src/main/groovy/org/grails/plugin/greenmail/`           | `MimeMessage` recipient properties |
| `GreenmailController`     | `plugin/grails-app/controllers/com/piragua/greenmail/`          | `/greenmail` list, show, clear |
| `GreenmailUrlMappings`    | `plugin/grails-app/controllers/greenmail/`                      | `/greenmail/...` URL mappings |

## Repository-Specific Notes

- **The plugin ships a GSP**, so `plugin/build.gradle` applies
  `org.apache.grails.gradle.grails-gsp` on top of `config.grails-plugin`. Neither
  `config.grails-plugin` nor `config.grails-web-plugin` applies it. Removing it breaks the
  `/greenmail` view in consuming applications (issue #42). The plugin jar must contain
  `gsp_greenmail_greenmaillist_gsp.class` and `gsp/views.properties`.
- **`config.grails-web-plugin` is deliberately not used** — the GSP has inline CSS and no
  `<asset:>` tags, so the asset pipeline is not needed.
- **`plugin/grails-app/conf/application.yml` sets the SMTP port to 1025.** That applies only when
  running the plugin standalone; a plugin's `application.yml` is not loaded into consuming
  applications, where the default remains 3025. The two are not in conflict.
- **`GreenmailController` lives in the legacy `com.piragua.greenmail` package**, which differs from
  the `greenmail` default package. Renaming it is a deliberate breaking change, not cleanup.

## CI/CD

- **CI** (`.github/workflows/ci.yml`): builds and tests on push/PR; publishes snapshots and docs on
  push to release branches.
- **Release** (`.github/workflows/release.yml`): staged release to Maven Central on GitHub release
  publish, then docs to GitHub Pages and the gh-pages version index. Requires the `release`, `docs`
  and `close` GitHub environments.
- **`project.yml`** drives POM metadata, the docs index and release notes. Its `contributors` and
  `versions` blocks are maintained automatically by the update-contributors and update-versions
  workflows — do not hand-edit them after the initial seed.

## Code Conventions

- Groovy source files follow standard Grails conventions: artefacts in `grails-app/`, everything else
  in `src/main/groovy/`.
- **Use `def` for local variables** where the type is inferable from the right-hand side. Explicit
  types only when the type cannot be inferred or `@CompileStatic` requires it.
- CodeNarc runs at zero tolerance for all three priorities. Notably: single quotes for
  non-interpolated strings (`UnnecessaryGString`), no space before a map-entry colon
  (`SpaceAroundMapEntryColon`), no wildcard imports, and a blank line after a class's opening brace.
- Checkstyle only scans Java sources, so it is `NO-SOURCE` in this Groovy-only repository.
