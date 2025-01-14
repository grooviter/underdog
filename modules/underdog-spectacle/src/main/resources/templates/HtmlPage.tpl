html {
    head {
        title('Title')
        link(href: 'css/spectacle.css', rel: 'stylesheet')
    }
    body {
        yieldUnescaped childrenContent
    }
}