package app1

import com.icegreen.greenmail.util.GreenMailUtil
import grails.plugin.greenmail.GreenMail
import grails.testing.mixin.integration.Integration
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import spock.lang.Specification

/**
 * Proves the plugin registers a running GreenMail server that captures the mail the
 * application sends, and that the Groovy extension properties work on real messages.
 */
@Integration
class GreenmailPluginSpec extends Specification {

    GreenMail greenMail
    JavaMailSender mailSender

    void cleanup() {
        greenMail.deleteAllMessages()
    }

    private void send(Map args) {
        MimeMessage message = mailSender.createMimeMessage()
        def helper = new MimeMessageHelper(message, false, 'UTF-8')
        helper.from = args.from as String
        helper.setTo(args.to as String[])
        if (args.cc) {
            helper.setCc(args.cc as String[])
        }
        helper.subject = args.subject as String
        helper.setText(args.body as String)
        mailSender.send(message)
    }

    void 'the plugin registers a running GreenMail server on the configured port'() {
        expect:
        greenMail.running

        and: 'the port comes from grails.plugin.greenmail.ports.smtp, not the 3025 default'
        greenMail.smtp.port == 2525
    }

    void 'mail sent by the application is captured instead of delivered'() {
        given:
        greenMail.messagesCount == 0

        when:
        send(from: 'from@example.com', to: ['to@example.com'], subject: 'a subject', body: 'hello world')

        then:
        greenMail.waitForIncomingEmail(5000, 1)

        and:
        greenMail.messagesCount == 1

        when:
        MimeMessage message = greenMail.latestMessage

        then:
        message.subject == 'a subject'
        GreenMailUtil.getBody(message).trim() == 'hello world'
        GreenMailUtil.getAddressList(message.from) == 'from@example.com'
    }

    void 'MimeMessage extension properties expose the recipients'() {
        when:
        send(
                from: 'from@example.com',
                to: ['first@example.com', 'second@example.com'],
                cc: ['copied@example.com'],
                subject: 'recipients',
                body: 'body'
        )

        then:
        greenMail.waitForIncomingEmail(5000, 1)

        when:
        MimeMessage message = greenMail.latestMessage

        then:
        message.to == 'first@example.com'
        message.tos == ['first@example.com', 'second@example.com']
        message.cc == 'copied@example.com'
        message.ccs == ['copied@example.com']
    }

    void 'deleteAllMessages empties the mailbox'() {
        given:
        send(from: 'from@example.com', to: ['to@example.com'], subject: 'to be deleted', body: 'body')
        greenMail.waitForIncomingEmail(5000, 1)

        expect:
        greenMail.messagesCount == 1

        when:
        greenMail.deleteAllMessages()

        then:
        greenMail.messagesCount == 0
        greenMail.latestMessage == null
    }
}
