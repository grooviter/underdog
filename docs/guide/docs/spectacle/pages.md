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

```groovy

static HtmlPage createPocPage() {
    return new HtmlPage(path: '/poc').with {
        spec {
            ...
        }
    }
}

application {
    page(createPocPage())
}
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
static HtmlPage createPocPageDark() {
    return new HtmlPage(path: '/poc', theme: 'dark').with {
        spec {
            // ...
        }
    }
}

application {
    page(createPocPageDark())
}
```