import grails.plugin.greenmail.GreenMail
import com.icegreen.greenmail.util.ServerSetupTest
import com.icegreen.greenmail.util.ServerSetup

class GreenmailGrailsPlugin {
    // the plugin version
    def version = "1.3-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

	def scopes = [excludes:'war']

    def author = "Mike Hugo"
    def authorEmail = "mike@piragua.com"
    def title = "Greenmail Plugin for Grails"
    def description = '''\\
Provides a wrapper around GreenMail (http://www.icegreen.com/greenmail/) and provides a view that displays 'sent' messages
'''

    def documentation = "http://grails.org/plugin/greenmail"

    def doWithSpring = {
		def smtpPort = application.config.greenmail.ports.smtp ?: ServerSetupTest.SMTP.port
		def smtp = new ServerSetup(smtpPort, null, "smtp")
	
		greenMail(GreenMail, [smtp] as ServerSetup[]) {
			it.initMethod = 'start'
			it.destroyMethod = 'stop'
		}
        // TODO Implement runtime spring config (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = { event ->
	    // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
	
}
