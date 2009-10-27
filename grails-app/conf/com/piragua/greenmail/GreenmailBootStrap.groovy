package com.piragua.greenmail

class GreenmailBootStrap {

    def grailsApplication

    def init = {servletContext ->
    }

    def destroy = {
        grailsApplication.mainContext.greenMail.stop()
    }
}