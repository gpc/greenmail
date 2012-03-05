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
grails.project.dependency.resolution = {
	inherits "global"
	log "warn"

	repositories {
		grailsHome()
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		compile 'com.icegreen:greenmail:1.3'
		runtime 'javax.mail:mail:1.4.1'
	}
	
	plugins {
		if (appName == "greenmail") {
			compile(":tomcat:$grailsVersion", ":hibernate:$grailsVersion") {
				export = false
			}
			test (":spock:0.6-SNAPSHOT", ":mail:1.0") {
				export = false
			}
			build(":release:1.0.1", ":svn:1.0.2") {
				export = false
			}
		}
	}
}

grails.release.scm.enabled = false
grails.project.work.dir = "target"