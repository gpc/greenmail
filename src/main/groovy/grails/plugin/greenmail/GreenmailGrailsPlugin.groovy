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

import com.icegreen.greenmail.util.ServerSetupTest
import com.icegreen.greenmail.util.ServerSetup
import grails.plugins.Plugin

import javax.mail.internet.MimeMessage
import javax.mail.Message.RecipientType

class GreenmailGrailsPlugin extends Plugin {

	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "3.0.0 > *"
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
			"grails-app/views/error.gsp"
	]

	def title = "Greenmail Plugin for Grails"
	def author = "Grails Plugin Collective"
	def authorEmail = "grails-plugin-collective@gmail.com"
	def description = "Provides a wrapper around GreenMail (http://www.icegreen.com/greenmail/) and provides a view that displays 'sent' messages"
	def documentation = "http://grails.org/plugin/greenmail"

	def profiles = ['web']

	@Override
	Closure doWithSpring() { {->
			if (!config.get("grails.plugin.greenmail.disabled")){
				int smtpPort = config.get("grails.plugin.greenmail.ports.smtp") ?: ServerSetupTest.SMTP.port
				ServerSetup smtp = new ServerSetup(smtpPort, null, "smtp")

				greenMail(GreenMail, [smtp] as ServerSetup[]) {
					it.initMethod = 'start'
					it.destroyMethod = 'stop'
				}
			}
		}
	}

	@Override
	void doWithDynamicMethods() {
		MimeMessage.metaClass {
			getTo { -> delegate.tos[0] }
			getTos { -> delegate.getRecipients(RecipientType.TO)*.toString() }
			getCc { -> delegate.ccs[0] }
			getCcs { -> delegate.getRecipients(RecipientType.CC)*.toString() }
			getBcc { -> delegate.bccs[0] }
			getBccs { -> delegate.getRecipients(RecipientType.BCC)*.toString() }
		}
	}

}
