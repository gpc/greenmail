package grails.plugin.greenmail

import com.icegreen.greenmail.imap.ImapHostManagerImpl
import com.icegreen.greenmail.util.ServerSetup
import com.icegreen.greenmail.util.ServerSetupTest

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
	
	void start() {
		ImapHostManagerImpl.getDeclaredField('store').accessible = true
		super.start()
	}
	
	void deleteAllMessages() {
		managers.imapHostManager.store.listMailboxes('*')*.deleteAllMessages()
	}
}