# GreenMail Plugin for Grails

[![Maven Central](https://img.shields.io/maven-central/v/io.github.gpc/greenmail.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.gpc/greenmail)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CI](https://github.com/gpc/greenmail/actions/workflows/ci.yml/badge.svg)](https://github.com/gpc/greenmail/actions/workflows/ci.yml)

Runs a mock [GreenMail](https://greenmail-mail-test.github.io/greenmail/) SMTP server inside a Grails
application during development and test, so mail your application sends is captured instead of
delivered. Captured messages can be asserted on from integration tests, or browsed in a running
application at `/greenmail`.

## Installation

```groovy
dependencies {
    testImplementation 'io.github.gpc:greenmail:7.0.1'
}
```

## Documentation

Full documentation — installation, configuration, integration testing, and the web interface — is
published at:

**https://gpc.github.io/greenmail/**

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for how to set up a development environment and submit changes.

## License

Released under the [Apache License, Version 2.0](LICENSE.txt).
