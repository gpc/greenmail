package org.grails.plugin.greenmail

import jakarta.mail.Address
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import spock.lang.Specification

class MimeMessageExtensionSpec extends Specification {
    
    Session session = Session.getDefaultInstance(System.getProperties())

    void setupSpec() {
        MimeMessage.metaClass.mixin MimeMessageExtension
    }

    void "getTo returns the first TO recipient"() {
        given:
        def mimeMessage = new MimeMessage(session)
        def toAddress = new InternetAddress("to1@example.com")
        mimeMessage.setRecipients(Message.RecipientType.TO, [toAddress] as Address[])

        when:
        def result = mimeMessage.to

        then:
        result == "to1@example.com"
    }

    void "getTos returns all TO recipients"() {
        given:
        def mimeMessage = new MimeMessage(session)
        def address1 = new InternetAddress("to1@example.com")
        def address2 = new InternetAddress("to2@example.com")
        mimeMessage.setRecipients(Message.RecipientType.TO, [address1, address2] as Address[])

        when:
        def result = mimeMessage.tos

        then:
        result == ["to1@example.com", "to2@example.com"]
    }

    void "getCc returns the first CC recipient"() {
        given:
        def mimeMessage = new MimeMessage(session)
        def ccAddress = new InternetAddress("cc1@example.com")
        mimeMessage.setRecipients(Message.RecipientType.CC, [ccAddress] as Address[])

        when:
        def result = mimeMessage.cc

        then:
        result == "cc1@example.com"
    }

    void "getCcs returns all CC recipients"() {
        given:
        def mimeMessage = new MimeMessage(session)
        def address1 = new InternetAddress("cc1@example.com")
        def address2 = new InternetAddress("cc2@example.com")
        mimeMessage.setRecipients(Message.RecipientType.CC, [address1, address2] as Address[])

        when:
        def result = mimeMessage.ccs

        then:
        result == ["cc1@example.com", "cc2@example.com"]
    }

    void "getBcc returns the first BCC recipient"() {
        given:
        def mimeMessage = new MimeMessage(session)
        def bccAddress = new InternetAddress("bcc1@example.com")
        mimeMessage.setRecipients(Message.RecipientType.BCC, [bccAddress] as Address[])

        when:
        def result = mimeMessage.bcc

        then:
        result == "bcc1@example.com"
    }

    void "getBccs returns all BCC recipients"() {
        given:
        def mimeMessage = new MimeMessage(session)
        def address1 = new InternetAddress("bcc1@example.com")
        def address2 = new InternetAddress("bcc2@example.com")
        mimeMessage.setRecipients(Message.RecipientType.BCC, [address1, address2] as Address[])

        when:
        def result = mimeMessage.bccs

        then:
        result == ["bcc1@example.com", "bcc2@example.com"]
    }
}
