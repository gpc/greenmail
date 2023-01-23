/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piragua.greenmail

import com.icegreen.greenmail.util.GreenMailUtil
import grails.web.mime.MimeType
import groovy.json.JsonBuilder

class GreenmailController {

    def greenMail

    def index() {redirect action: 'list'}

    def list() {
        withFormat {
            html {
                return [list: greenMail.getReceivedMessages().sort({it.sentDate}).reverse()]
            }
            js {
                List jsonMessages = []
                List messages = greenMail.getReceivedMessages().sort({it.sentDate}).reverse()
                messages.eachWithIndex {message, index ->
                    jsonMessages << createMessageMap(message, index)
                }
                render(contentType: MimeType.JSON.name, text: new JsonBuilder(jsonMessages).toString())
            }
        }
    }

    def show() {
        def messages = Arrays.asList(greenMail.getReceivedMessages().sort({it.sentDate}).reverse())
        def specificMessage = messages[Integer.valueOf(params.id).intValue()]
        withFormat {
            html {
                render(contentType: MimeType.HTML.name) { pre(GreenMailUtil.getWholeMessage(specificMessage)) }
            }
            js {
                render(contentType: MimeType.JSON.name, text: new JsonBuilder(createMessageMap(specificMessage, params.id)).toString())
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

    def clear() {
        greenMail.deleteAllMessages()
        render(contentType: MimeType.HTML.name) { div('Email messages have been cleared') }
    }
}