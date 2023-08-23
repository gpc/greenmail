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

import com.icegreen.greenmail.util.ServerSetup
import com.icegreen.greenmail.util.ServerSetupTest
import grails.plugins.Plugin
import groovy.util.logging.Slf4j

@Slf4j
class GreenmailGrailsPlugin extends Plugin {

	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "5.0.0 > *"
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

    private boolean isGreenMailEnabled() {
        return !config.getProperty("grails.plugin.greenmail.disabled", Boolean, false)
    }

	@Override
	Closure doWithSpring() { {->
			if (greenMailEnabled) {
				int smtpPort = config.getProperty("grails.plugin.greenmail.ports.smtp", Integer, ServerSetupTest.SMTP.port) 
				ServerSetup smtp = new ServerSetup(smtpPort, null, "smtp")

				greenMail(GreenMail, [smtp] as ServerSetup[])
			} else {
                log.debug("GreenMail is disabled")
            }
		}
	}

    private GreenMail getGreenMailBean() {
        return applicationContext.getBean("greenMail", GreenMail)
    }

    @Override
    void doWithApplicationContext() {
        if (greenMailEnabled) {
            greenMailBean.start()
            if (greenMailBean.running) {
                log.info("GreenMail is running with SMTP port ${greenMailBean.smtp.port}")
            }
        }
    }

    @Override
    void onShutdown(Map<String, Object> event) {
        if (greenMailEnabled) {
            log.info("Shutting down GreenMail")
            greenMailBean.stop()
        }
    }
}
