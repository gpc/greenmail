package app1

import grails.plugin.greenmail.GreenMail
import grails.testing.mixin.integration.Integration
import groovy.json.JsonSlurper
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import spock.lang.Specification

/**
 * Exercises the controller, URL mappings and GSP view the plugin ships. This is the
 * regression path behind issue #42: without the grails-gsp Gradle plugin the view is
 * not precompiled into the plugin jar and the HTML response fails at runtime.
 */
@Integration
class GreenmailControllerSpec extends Specification {

    GreenMail greenMail
    JavaMailSender mailSender

    @Value('${local.server.port}')
    Integer serverPort

    void cleanup() {
        greenMail.deleteAllMessages()
    }

    private void sendOne(String subject) {
        MimeMessage message = mailSender.createMimeMessage()
        def helper = new MimeMessageHelper(message, false, 'UTF-8')
        helper.from = 'from@example.com'
        helper.setTo('to@example.com')
        helper.subject = subject
        helper.setText('body text')
        mailSender.send(message)
        assert greenMail.waitForIncomingEmail(5000, 1)
    }

    private Map get(String path) {
        HttpURLConnection connection = new URL("http://localhost:$serverPort$path").openConnection() as HttpURLConnection
        connection.requestMethod = 'GET'
        connection.instanceFollowRedirects = true
        int status = connection.responseCode
        String body = status < 400 ? connection.inputStream.getText('UTF-8') : connection.errorStream?.getText('UTF-8')
        String contentType = connection.getHeaderField('Content-Type')
        connection.disconnect()
        return [status: status, body: body, text: decodeEntities(body), contentType: contentType]
    }

    /** The GSP html codec escapes '@' to '&#64;', so decode before asserting on addresses. */
    private static String decodeEntities(String input) {
        input?.replaceAll(/&#(\d+);/) { _, String code -> Integer.parseInt(code) as char }
                ?.replace('&amp;', '&')
    }

    void 'GET /greenmail/list renders the precompiled GSP view'() {
        given:
        sendOne('a captured subject')

        when:
        Map response = get('/greenmail/list')

        then: 'the GSP rendered rather than failing to resolve'
        response.status == 200
        response.contentType?.contains('text/html')

        and: 'the captured message appears in the table'
        response.text.contains('a captured subject')
        response.text.contains('to@example.com')

        and: 'sitemesh processed the layout tags rather than emitting them literally'
        !response.body.contains('grailsLayout:')
    }

    void 'GET /greenmail/list.json returns the captured messages as JSON'() {
        given:
        sendOne('json subject')

        when:
        Map response = get('/greenmail/list.json')

        then:
        response.status == 200
        response.contentType?.contains('application/json')

        and: 'the whole body parses, so nothing is appended after the JSON document'
        List parsed = new JsonSlurper().parseText(response.body) as List
        parsed.size() == 1
        parsed[0].subject == 'json subject'
        parsed[0].to == 'to@example.com'
        parsed[0].id == 0
    }

    void 'GET /greenmail/list.js returns the same JSON as .json'() {
        given: 'the .js extension predates .json support and has to keep working'
        sendOne('js subject')

        when:
        Map response = get('/greenmail/list.js')

        then:
        response.status == 200
        response.contentType?.contains('application/json')

        and:
        List parsed = new JsonSlurper().parseText(response.body) as List
        parsed.size() == 1
        parsed[0].subject == 'js subject'
    }

    void 'GET /greenmail/show/0 renders the raw message'() {
        given:
        sendOne('shown subject')

        when:
        Map response = get('/greenmail/show/0')

        then:
        response.status == 200
        response.text.contains('shown subject')

        and: 'the headers are listed, and not prefixed with an uninitialised null'
        !response.body.startsWith('null')
        response.body.contains('Subject: shown subject<br/>')
    }

    void 'GET /greenmail/show/0.json returns the message as JSON'() {
        given:
        sendOne('shown json subject')

        when:
        Map response = get('/greenmail/show/0.json')

        then:
        response.status == 200
        response.contentType?.contains('application/json')

        and:
        Map parsed = new JsonSlurper().parseText(response.body) as Map
        parsed.subject == 'shown json subject'
        parsed.to == 'to@example.com'

        and: 'id is the numeric index, as it is in list.json, not the String path parameter'
        parsed.id == 0
    }

    void 'GET /greenmail/clear empties the mailbox'() {
        given:
        sendOne('to be cleared')

        expect:
        greenMail.messagesCount == 1

        when:
        Map response = get('/greenmail/clear')

        then:
        response.status == 200

        and:
        greenMail.messagesCount == 0
    }
}
