package underdog.plots.dsl

class NodeMap {
    static final String FUNCTION_START = "---FNS---"
    static final String FUNCTION_ENDS = "---FNE---"

    Map map = [:]

    static String fn(String function) {
        return "$FUNCTION_START$function$FUNCTION_ENDS"
    }
}
