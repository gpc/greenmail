package app1

class UrlMappings {

    static mappings = {
        '/'(redirect: '/greenmail')
        '500'(view: '/error')
        '404'(view: '/notFound')
    }
}
