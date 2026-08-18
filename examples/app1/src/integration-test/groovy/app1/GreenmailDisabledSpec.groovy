package app1

import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * Boots a second application context with the plugin disabled, proving that
 * grails.plugin.greenmail.disabled suppresses the greenMail bean.
 *
 * Deliberately does not inject a GreenMail field: there is no such bean in this
 * context. No SMTP port is bound either, so this context cannot collide with the
 * one the other specifications share.
 *
 * The matching URL mappings are not asserted here. GreenmailUrlMappings reads the
 * disabled flag from its static 'mappings' initialiser, which the JVM evaluates
 * once per class load. By the time this context starts, the enabled context in the
 * same test JVM has already initialised the class, so the mappings remain
 * registered regardless of this context's configuration.
 */
@Integration
@TestPropertySource(properties = ['grails.plugin.greenmail.disabled=true'])
class GreenmailDisabledSpec extends Specification {

    @Autowired
    ApplicationContext applicationContext

    void 'the greenMail bean is not registered when the plugin is disabled'() {
        expect:
        !applicationContext.containsBean('greenMail')
    }
}
