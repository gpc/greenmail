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

import com.icegreen.greenmail.imap.ImapHostManagerImpl
import com.icegreen.greenmail.util.ServerSetup
import com.icegreen.greenmail.util.ServerSetupTest
import com.icegreen.greenmail.util.Service

import javax.mail.internet.MimeMessage

/**
 * GreenMail provides no easy way to delete all messages, this class
 * provides deleteAllMessages() which does that.
 */
class GreenMail extends com.icegreen.greenmail.util.GreenMail {

	GreenMail() {
		super()
	}

	GreenMail(ServerSetup config) {
		super(config)
	}

	GreenMail(ServerSetup[] config) {
		super(config)
	}

    @Override
    synchronized void start() {
		ImapHostManagerImpl.getDeclaredField('store').accessible = true
		super.start()
	}

    @Override
	synchronized void stop() {
		services.each { Service service -> service.stopService(stopTimeout) }
	}

	void deleteAllMessages() {
		((ImapHostManagerImpl)managers.imapHostManager).store.listMailboxes('*')*.deleteAllMessages()
	}

	/** @deprecated use direct messages.size */
	@Deprecated
	int getMessagesCount() {
		messages.size()
    }

	List<MimeMessage> getMessages() {
		receivedMessages.toList()
	}

    /** @deprecated use direct messages[index], but be careful for bounds */
    @Deprecated
	MimeMessage getMessage(int index) {
		return index < messages.size() ? messages[index] : null
    }

    /** @deprecated use direct messages?.last() */
    @Deprecated
	MimeMessage getLatestMessage() {
		return  messages?.last()
	}
}