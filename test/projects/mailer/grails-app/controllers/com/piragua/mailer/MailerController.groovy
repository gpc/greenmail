package com.piragua.mailer

class MailerController {

    def mailService
    
	def index = {
        Map mail = [message:'hello world', from:'from+unittest@piragua.com', to:'to+unittest@piragua.com', subject:'subject']

        mailService.sendMail {
            to mail.to
            from mail.from
            subject mail.subject
            body mail.message
        }

        render 'test message sent'
    }

}