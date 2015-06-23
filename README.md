Greenmail Plugin for Grails 3
=============================

This is a fork of grails greenmail plugin for grails 3.

INSTALL
-------

Add a dependency for the plugin in build.gradle:

```

dependencies {    
    compile 'org.grails.plugins:grails-greenmail:2.0.0'    
}

```


USAGE
-------

Provides a wrapper around [GreenMail](http://www.icegreen.com/greenmail/) and provides a view that displays 'sent' messages - useful for testing application in the 'development' or 'test' environments.


### Installation

    grails install-plugin greenmail


The plugin assumes that you have some sort of Java mail provider installed (for instance the Grails mail plugin).  You need to define a SMTP port for the mock Greenmail SMTP server to start with.  Using the Grails Mail plugin, this is as simple as defining the grails.mail.port property in Config.groovy, like this (see the first line in the 'development' and 'test' blocks):

	environments {
	    production {
	        grails.serverURL = "http://www.changeme.com"
	    }
	    development {
		grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
	        grails.serverURL = "http://localhost:8080/${appName}"
	    }
	    test {
		grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
	        grails.serverURL = "http://localhost:8080/${appName}"
	    }
	}

You can also completely disable the plugin by using the config setting greenmail.disabled = true.  For example, to disable greenmail in production:

	environments {
	    production {
	       greenmail.disabled=true
	    }
	}


### Usage in Integration Tests

The plugin can be used to capture email messages during integration tests.  For example:

	import com.icegreen.greenmail.util.*

	class GreenmailTests extends GroovyTestCase {
	    def mailService
	    def greenMail

	    void testSendMail() {
	        Map mail = [message:'hello world', from:'from@piragua.com', to:'to@piragua.com', subject:'subject']

	        mailService.sendMail {
	            to mail.to
	            from mail.from
	            subject mail.subject
	            body mail.message
	        }
	        
	        assertEquals(1, greenMail.getReceivedMessages().length)
		
	        def message = greenMail.getReceivedMessages()[0]
			
	        assertEquals(mail.message, GreenMailUtil.getBody(message))
	        assertEquals(mail.from, GreenMailUtil.getAddressList(message.from))
	        assertEquals(mail.subject, message.subject)
	    }

	    void tearDown() {
	        greenMail.deleteAllMessages()
	    }
	}


The plugin adds a `deleteAllMessages()` convenience method to the `greenMail` bean that deletes all received messages.

h2. Usage in running application 

The plugin provides a controller and view to show messages that are 'sent' from the application.  Simply browse to http://localhost:8080//greenmail and it will show a list of messages sent.  You can click on the "show" link to view the raw message.


Roadmap:
This is a fully functional plugin, though there are some features that I think would be worth adding.  Contributions and patches are welcome!  

* Messages sent by the Grails Mail plugin have duplicate "TO:" fields in the raw message and in the address list, for instance if the recipient is spam@piragua.com, then that email address is listed twice when you retrieve the address list for RecipientType.TO (e.g. GreenMailUtil.getAddressList(message.getRecipients(javax.mail.Message.RecipientType.TO))
* Ability to view HTML email messages as they appear in a mail client rather than as RAW message.

