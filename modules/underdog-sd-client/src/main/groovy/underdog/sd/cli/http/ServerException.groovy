package underdog.sd.cli.http

import groovy.transform.TupleConstructor

@TupleConstructor
class ServerException extends Exception {
    int code
    String message
}
