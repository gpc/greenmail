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

import grails.plugin.greenmail.GreenMail
import grails.plugin.spock.*

class SendAndRetrieveSpec extends IntegrationSpec {

    def mailService
    GreenMail greenMail

    void setup() {
        greenMail.deleteAllMessages()
    }

    void cleanup() {
        greenMail.deleteAllMessages()
    }

    def "send mail and retrieve via greenmail"() {
        when:
          mailService.sendMail {
              to "tester@test.com"
              from "grails@grails.org"
              subject "test"
              text "This is a test"
          }
        then:
          greenMail.messagesCount == 1
          greenMail.messages.size() == 1
          greenMail.getMessage(0).to == "tester@test.com"
          greenMail.getMessage(1) == null
          greenMail.messages[0].to == "tester@test.com"
          greenMail.latestMessage.to == "tester@test.com"
          greenMail.messages.last().to == "tester@test.com"
    }

    def 'cleanup mailbox'() {
        given:
          mailService.sendMail {
              to "tester@test.com"
              from "grails@grails.org"
              subject "test"
              text "This is a test"
          }
          assert greenMail.messages.last().to == "tester@test.com"
        when:
          greenMail.deleteAllMessages()
        then:
          greenMail.messages.isEmpty()
    }
}