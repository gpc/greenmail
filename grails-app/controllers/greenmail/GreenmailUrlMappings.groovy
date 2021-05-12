package greenmail

import grails.util.Holders

class GreenmailUrlMappings {

    static mappings = {
        if (!Holders.grailsApplication.config.getProperty("grails.plugin.greenmail.disabled", Boolean, false)) {
            "/greenmail/$action?/$id?(.$format)?"(controller: 'greenmail')
        }
    }
}
