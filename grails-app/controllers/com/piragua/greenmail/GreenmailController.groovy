package com.piragua.greenmail

import com.icegreen.greenmail.util.GreenMailUtil
import grails.converters.*

class GreenmailController {

    def greenMail

    def index = {redirect action: 'list'}

    def list = {
        withFormat {
            js {
                List jsonMessages = []
                List messages = greenMail.getReceivedMessages().sort({it.sentDate}).reverse()
                messages.eachWithIndex {message, index ->
                    jsonMessages << createMessageMap(message, index)
                }
                render jsonMessages as JSON
            }
            html {
                return [list: greenMail.getReceivedMessages().sort({it.sentDate}).reverse()]
            }
        }
    }

    def show = {
        def messages = Arrays.asList(greenMail.getReceivedMessages().sort({it.sentDate}).reverse())
        def specificMessage = messages[Integer.valueOf(params.id).intValue()]
        withFormat {
            js {
                render createMessageMap(specificMessage, params.id) as JSON
            }
            html {
                render "<pre>${GreenMailUtil.getWholeMessage(specificMessage)}</pre>"
            }
        }
    }

    private createMessageMap(message, index) {
        Map messageMap = [
                id: index,
                sent: message.sentDate,
                subject: message.subject,
                to: GreenMailUtil.getAddressList(message.getRecipients(javax.mail.Message.RecipientType.TO)),
                body: GreenMailUtil.getWholeMessage(message)
        ]
        return messageMap
    }

    def clear = {
        greenMail.stop()
        greenMail.start()
        render 'Email messages have been cleared'
    }

}