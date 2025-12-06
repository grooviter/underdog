## Streaming

Sometimes we may want to stream the response to some of the outputs we have configured. Use cases for that could be:

- When using AI models it's very common to receive the response from the AI as a stream of text, aka chatbots
- There is a variable we want to receive the data in real time

### Project Reactor

In general for streaming data to the UI your methods (the event handlers) should be returning Flux objects. Spectacle
depends on the [Project Reactor](https://projectreactor.io/) to do that. So I strongly recommend you to become familiar with it when dealing with
data streaming in Spectacle. We'll see a full example in the next sections.

In Spectacle there are three scenarios where you can configure your streaming actions:

- Spec
- Form
- Single components

### Spec

[Spec](#spec) is a DSL to make it easier to build a UI without having to design a layout by ourselves. When you want to execute
whatever you have in your spec you end up invoking the **onSubmit** button. The return values of the function will be
rendered in the output fields marked in your spec:

```groovy title="Value"
--8<-- "src/test/groovy/underdog/guide/spectacle/SpringAISpec.groovy:onSubmit"
```

However if you'd like to stream something your **onSubmit** function must return an instance of type 
**reactor.core.publisher.Flux<?>**

```groovy title="Streaming"
--8<-- "src/test/groovy/underdog/guide/spectacle/SpringAIStreamingSpec.groovy:onSubmit"
```

### Form

In the case of using a form to stream data:

1) we need to mark the form as streaming:

```groovy
form(streaming: true){
    ...
}
```

2) Our **onSubmit** function must return a **reactor.core.publisher.Flux<?>** object.

```groovy
form(streaming: true){
    onSubmit(...) {
        return Flux.fromArray(...)
    }
}
```

!!! Danger

    The Flux type (Flux&lt;TYPE&gt;) must match the expected component value. For example:

    - HtmlNumber will require a Flux<Number>
    - HTMLMarkdown or an HtmlInputText will expect a Flux<String>
    - etc

    Check the API to be sure 

Here is an example of a page having a streaming form

```groovy title="Streaming (Form)"
--8<-- "src/test/groovy/underdog/guide/spectacle/streaming/FormStreamingSpec.groovy:form_streaming"
```

The output fields don't have to be inside the form as long as they are referenced as output fields in the form. Once
the form is submitted (this time via clicking on the button) the function will be executed and the streaming values
will be sent to the output field.

### Components

It's not mandatory to create a form in order to stream values to some other component. The only two things necessary
are:

- One component triggering an action
- One output component receiving the streaming data

Normally this can be done because in every component capable of triggering an action there will be methods representing
event invocations. For example a button (HtmlButton) has the **onClick** method which triggers an action:

```groovy
button(...) {
    onClick([inputs], [outputs], streaming){
        ...
    }
}
```

Here you have an example:

```groovy title="Streaming (Component)"
--8<-- "src/test/groovy/underdog/guide/spectacle/streaming/ComponentStreamingSpec.groovy:component_streaming"
```

Here the **onClick** method is only referencing the output field. It uses the third parameter to indicate that this
action will be streaming data. Again because this is going to be streaming data your function
must return a **Flux** instance matching the type of the target component.