grails.project.dependency.resolution = {
	inherits "global" // inherit Grails' default dependencies
	log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

	repositories {
		grailsHome()
		mavenCentral()
	}

	dependencies {
		build 'com.icegreen:greenmail:1.3'
	}
}
