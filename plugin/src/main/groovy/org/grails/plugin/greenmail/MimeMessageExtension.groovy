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

import jakarta.mail.Message
import jakarta.mail.internet.MimeMessage

/**
 * @since 2.0.0
 */
class MimeMessageExtension {

    /**
     * Get the first TO recipient
     * @param instance the message
     * @return first TO recipient or null if there are none
     */
    static String getTo(MimeMessage instance) {
        instance.tos[0]
    }

    /**
     * Get all TO recipients. {@link MimeMessage#getRecipients} returns null when the message has no
     * recipient of that type, so the null is normalised to an empty list here.
     * @param instance the message
     * @return TO recipients or an empty list if there are none
     */
    static List<String> getTos(MimeMessage instance) {
        instance.getRecipients(Message.RecipientType.TO)*.toString() ?: []
    }

    /**
     * Get the first CC recipient
     * @param instance the message
     * @return first CC recipient or null if there are none
     */
    static String getCc(MimeMessage instance) {
        instance.ccs[0]
    }

    /**
     * Get all CC recipients. {@link MimeMessage#getRecipients} returns null when the message has no
     * recipient of that type, so the null is normalised to an empty list here.
     * @param instance the message
     * @return CC recipients or an empty list if there are none
     */
    static List<String> getCcs(MimeMessage instance) {
        instance.getRecipients(Message.RecipientType.CC)*.toString() ?: []
    }

    /**
     * Get the first BCC recipient
     * @param instance the message
     * @return first BCC recipient or null if there are none
     */
    static String getBcc(MimeMessage instance) {
        instance.bccs[0]
    }

    /**
     * Get all BCC recipients. {@link MimeMessage#getRecipients} returns null when the message has no
     * recipient of that type, so the null is normalised to an empty list here.
     * @param instance the message
     * @return BCC recipients or an empty list if there are none
     */
    static List<String> getBccs(MimeMessage instance) {
        instance.getRecipients(Message.RecipientType.BCC)*.toString() ?: []
    }
}
