## Components

This section lists all out-of-the-box components you can use in your Spectacle applications. 

### Common properties 

In general all components inherit from **HtmlElement** therefore have the following properties:

- **id**: used by the application for replacing the html whenever the output fields are updated
- **name**: used by the application to get data from input fields and send data to output fields
- **application**: has access to almost every aspect to the application: elements, events, configuration...
- **className**: HTML class attribute. Can be used to customize the appearance of the element
- **parent**: give access to the immediate parent container

### Bootstrap

All components and containers are based on [Bootstrap](https://getbootstrap.com) and more specifically in the  [Tabler](https://github.com/tabler/tabler) project. Checkout an eye on these project's guidelines whenever you'd like to customize existent or new custom components.

### HtmlDiv

```groovy title="div"
div(className: '...') {
    // nested elements
}
```

### HtmlColumn

```groovy title="column"
col(className: '...') {
    // nested elements
}
```

### HtmlRow

```groovy title="row"
row(className: '...') {
    // nested elements
}
```

### HtmlForm

```groovy title="form"
form {
    // nested elements    
}
```

### HtmlButton

Represents a HTML button

![](./images/components/HtmlButton.png){ width="15%" }

### HtmlInputText

Represents a HTML input of type text

![](./images/components/HtmlInputText.png){ width="80%" }

### HtmlInputNumber

Represents a HTML input of type number

![](./images/components/HtmlInputNumber.png){ width="80%" }

### HtmlCard

Represents a Bootstrap card. A card has normally three parts:

- card header
- card body
- card footer

### HtmlChart

Renders an Underdog's plot. It receives as input value any `underdog.plots.Options` instance.

![](./images/components/HtmlChart.png){ width="50%" }

### HtmlDataFrame

Represents an Underdog's dataframe

![](./images/components/HtmlDataFrame.png){ width="80%" }

### HtmlMarkdown

Represents a markDown text

### HtmlNumberCard

Represents a Bootstrap card showing just a number

### HtmlOptionGroup

Represents an HTML option group input field

![](./images/components/HtmlOptionGroup.png){ width="40%" }

### HtmlRange

Represents an HTML slider input field

### HtmlResetLink

Inside a form, represents a reset button, with the appearance of a link

### HtmlSelect

Represents an HTML select input field

![](./images/components/HtmlSelect.png){ width="80%" }

### HtmlTextArea

Represents an HTML text area field