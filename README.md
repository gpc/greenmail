Greenmail Plugin for Grails 5
=============================

This is a fork of grails greenmail plugin for grails 5.

INSTALL
-------

Add a dependency for the plugin in `build.gradle`:

```groovy
dependencies {
    testImplementation 'io.github.gpc:greenmail:5.0.0'
    
}
```

If you need to type your `MimeMessages` you also need to depend on:
```groovy
    testImplementation 'jakarta.mail:jakarta.mail-api:1.6.7'
```

USAGE
-------

Provides a wrapper around [GreenMail](https://greenmail-mail-test.github.io/greenmail/) and provides a view that displays `sent` messages - useful for testing application in the `development` or `test` environments.



The plugin assumes that you have some sort of Java mail provider installed (for instance the Grails mail plugin). You need to define an SMTP port for the mock Greenmail SMTP server to start with. Using the Grails Mail plugin, this is as simple as defining the `grails.mail.port` property in `application.yml`, like this (see the first line in the `development` and `test` blocks):

You can completely disable the plugin by using the config setting `grails.plugin.greenmail.disabled = true`.

If you need to change the default listening port (3025) you can use the `grails.plugin.greenmail.ports.smtp`
configuration variable.

#### Example configuration:

```yaml
--- # Mail and GreenMail configurations
environments:
    development:
        grails:
            mail:
                port: 3025 # Use default GreenMail port
    test:
        grails:
            plugin:
                greenmail:
                    ports:
                        smtp: 2525 # Specify GreenMail port
            mail:
                port: "${grails.plugin.greenmail.ports.smtp}"
    production:
        grails:
            plugin:
                greenmail:
                    disabled: true # Will not run the GreenMail plugin
            mail: # For your production SMTP server. See mail plugin for configuration options
                server: smtp.example.com
                port: 25
```

### Usage in Integration Tests

The plugin can be used to capture email messages during integration tests. For example:

```groovy

import com.icegreen.greenmail.util.GreenMailUtil
import grails.plugin.greenmail.GreenMail
import grails.plugins.mail.MailService
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import javax.mail.internet.MimeMessage

@Integration
class GreenmailExampleSpec extends Specification {

    MailService mailService
    GreenMail greenMail

    void cleanup() {
        greenMail.deleteAllMessages()
    }

    void "send a test mail"() {
        given:
        Map mail = [message: 'hello world', from: 'from@piragua.com', to: 'to@piragua.com', subject: 'subject']

        when:
        mailService.sendMail {
            to mail.to
            from mail.from
            subject mail.subject
            body mail.message
        }

        then:
        greenMail.receivedMessages.length == 1

        with(greenMail.receivedMessages[0]) { MimeMessage message ->
            GreenMailUtil.getBody(message) == mail.message
            GreenMailUtil.getAddressList(message.from) == mail.from
            message.subject == mail.subject
        }
    }

}
```

#### Test example

The above code snippets can be found here: https://github.com/sbglasius/greenmail-example

### Additional methods 

`grails.plugin.greenmail.GreenMail` extends from `com.icegreen.greenmail.util.GreenMail` and adds a few extra methods. See [GreenMail.groovy](src/main/groovy/grails/plugin/greenmail/GreenMail.groovy) for reference.

### Usage in running application 

The plugin provides a controller and view to show messages that are `sent` from the application.  Simply browse to http://localhost:8080/greenmail, and it will show a list of messages sent.  You can click on the `show` link to view the raw message.


### Roadmap
This is a fully functional plugin, though there are some features that I think would be worth adding.  Contributions and patches are welcome!  

* Messages sent by the Grails Mail plugin have duplicate _TO:_ fields in the raw message and in the address list, for instance if the recipient is _spam@piragua.com_, then that email address is listed twice when you retrieve the address list for `RecipientType.TO` (e.g. `GreenMailUtil.getAddressList(message.getRecipients(javax.mail.Message.RecipientType.TO))`
* Ability to view HTML email messages as they appear in a mail client rather than as RAW message.
