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
package grails.plugin.greenmail

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import javax.mail.internet.MimeMessage

/**
 * This class provides convenience methods added to the original GreenMail class
 */
@InheritConstructors
@CompileStatic
class GreenMail extends com.icegreen.greenmail.util.GreenMail {

    /**
     * GreenMail supports removing all messages, but by a different method
     */
    void deleteAllMessages() {
        super.purgeEmailFromAllMailboxes()
    }

    /**
     * Counts messages
     * @return message count
     */
    int getMessagesCount() {
        messages.size()
    }

    /**
     * Get list of messages (instead of Array)
     * @return list
     */
    Collection<MimeMessage> getMessages() {
        receivedMessages as List<MimeMessage>
    }

    /**
     * Get a specific message
     * @param index
     * @return specific message or null if not found
     */
    MimeMessage getMessage(int index) {
        return index < messagesCount ? messages[index] : null
    }

    /**
     * Get latest message
     * @return latest message or null if not found
     */
    MimeMessage getLatestMessage() {
        return messages ? messages.last() : null
    }

}
