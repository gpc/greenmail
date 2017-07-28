/*
 * Copyright 2016 the original author or authors.
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
package org.grails.plugin.greenmail

import javax.mail.Message
import javax.mail.internet.MimeMessage

/**
 * @since 2.0.0
 */
class MimeMessageExtension {

    static String getTo(MimeMessage instance) {
        instance.tos[0]
    }

    static List<String> getTos(MimeMessage instance) {
        instance.getRecipients(Message.RecipientType.TO)*.toString()
    }

    static String getCc(MimeMessage instance) {
        instance.ccs[0]
    }

    static List<String> getCcs(MimeMessage instance) {
        instance.getRecipients(Message.RecipientType.CC)*.toString()
    }

    static String getBcc(MimeMessage instance) {
        instance.bccs[0]
    }

    static List<String> getBccs(MimeMessage instance) {
        instance.getRecipients(Message.RecipientType.BCC)*.toString()
    }
}
