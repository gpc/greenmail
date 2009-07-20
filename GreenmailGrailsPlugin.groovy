import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetupTest

class GreenmailGrailsPlugin {
    // the plugin version
    def version = "1.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

	def environments = ['dev', 'test']
	def scopes = [excludes:'war']

    // TODO Fill in these fields
    def author = "Mike Hugo"
    def authorEmail = "mike@piragua.com"
    def title = "Greenmail Plugin for Grails"
    def description = '''\\
Provides a wrapper around GreenMail (http://www.icegreen.com/greenmail/) and provides a view that displays 'sent' messages
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Greenmail+Plugin"

    def doWithSpring = {
		greenMail(GreenMail){
			constructorArg(ServerSetupTest.SMTP)
		}
        // TODO Implement runtime spring config (optional)
    }

    def doWithApplicationContext = { applicationContext ->
		try {
			applicationContext.getBean('greenMail').start()
		} catch (Exception e) {
			log.error 'unable to start greenmail!'
		}
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
