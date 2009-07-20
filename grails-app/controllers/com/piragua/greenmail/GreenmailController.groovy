package com.piragua.greenmail.controller

import com.icegreen.greenmail.util.GreenMailUtil

public class GreenmailController {

    def greenMail

    def list = {
        return [list:greenMail.getReceivedMessages().sort({it.sentDate}).reverse() ]        
    }

    def show = {
        def messages = Arrays.asList(greenMail.getReceivedMessages().sort({it.sentDate}).reverse())
        render "<pre>${GreenMailUtil.getWholeMessage(messages[Integer.valueOf(params.id).intValue()])}</pre>"
    }

    def clear = {
        greenMail.stop()
        greenMail.start()
        render 'Email messages have been cleared'
    }

}