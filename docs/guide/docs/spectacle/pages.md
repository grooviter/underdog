## Pages

### Creation

A page represents an HTML page. A Spectacle application can have many pages. You can create a new page by
invoking the **page(...)** method inside the application DSL:

```groovy title="new page"
application {
    page('/poc') {
        // html elements
    }
}
```

As I mentioned earlier you can have as many pages as you want:

```groovy title="many pages"
application {
    page('/poc') {
        // html elements
    }
    page("/about") {
        // ...
    }
}
```


### Externalizing

At some point many pages can be very verbose, and it could become handy to externalize page creation to methods:

```groovy title="Refactor method"
--8<-- "src/test/groovy/underdog/guide/spectacle/PageSpec.groovy:page_refactor_method"
```

Or even using a class:

```groovy title="Refactor class"
--8<-- "src/test/groovy/underdog/guide/spectacle/PageSpec.groovy:page_refactor_class"
```

And them bind the pages to the application

```groovy title="Page binding"
--8<-- "src/test/groovy/underdog/guide/spectacle/PageSpec.groovy:page_refactor"
```

### Themes

Every page can choose between **dark** or **light** themes. You can set the theme wherever you declare the page:

```groovy
application {
    page('/poc', theme: 'dark') {
        // ...
    }
}
```

or 

```groovy
class Pages {
    static HtmlPage createPocPageDark(HtmlApplication app) {
        return new HtmlPage(
            path: '/poc', 
            theme: 'dark', 
            application: app
        ).with {
            spec {
                // ...
            }
        }
    }
}

application {
    page(Pages::createPocPageDark)
}
```
