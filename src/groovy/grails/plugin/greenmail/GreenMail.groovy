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
	
	void deleteAllMessages() {
		managers.imapHostManager.store.listMailboxes('*')*.deleteAllMessages()
	}
	
	int getMessagesCount() {
		getMessages().size()
	}
	
	Collection<MimeMessage> getMessages() {
		getReceivedMessages().toList()
	}
	
	MimeMessage getMessage(int index) {
		def messages = getMessages()
		if (index < messages.size()) {
			messages[index]
		} else {
			null
		}
	}
	
	MimeMessage getLatestMessage() {
		def messages = getMessages()
		if (messages) {
			messages.last()
		} else {
			null
		}
	}
}