package memento.plots.charts

trait ToMapAware {

    Map toMap() {
        def map = [:]
        def fields = this.metaClass.getProperties().findAll { it.name != "class" }
        fields.each {
            def field = this.metaClass.getProperty(this, it.name)
            if(field instanceof ToMapAware) {
                map[(it.name)] = field.toMap()
            } else if(field) {
                map[(it.name)] = field
            }
        }
        return map
    }
}