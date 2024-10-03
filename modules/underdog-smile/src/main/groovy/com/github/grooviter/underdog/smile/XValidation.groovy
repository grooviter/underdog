package com.github.grooviter.underdog.smile

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import smile.classification.Classifier
import smile.validation.ClassificationValidations
import smile.validation.CrossValidation

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @since 0.1.0
 */
class XValidation {

    /**
     * Because cross validation calls to models are done from Smile
     * Java code, Groovy mechanisms such as ExtensionModule doesn't
     * work. That is necessary for things like adding new predict()
     * methods to existent models.
     *
     * In order to do that these calls are redispatched via this
     * class to force Groovy to be aware of them.
     *
     * @since 0.1.0
     */
    @TupleConstructor
    static class ClassifierProxy implements InvocationHandler {
        Classifier<double[]> classifier

        @Override
        Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return classifier.invokeMethod(method.name, args)
        }
    }

    /**
     * @param X
     * @param y
     * @param k
     * @param rounds
     * @param trainer
     * @return {@link Classifier} of type double[][]
     * @since 0.1.0
     */
    @NamedVariant
    ClassificationValidations<Classifier<double[]>> classification(
            double[][] X,
            int[] y,
            @NamedParam(required = false) int k = 2,
            @NamedParam(required = false) int rounds = 10,
            @ClosureParams(value = FromString, options = 'double[][],int[]') Closure<Classifier<double[]>> trainer) {
        return CrossValidation.classification(rounds, k, X, y, (xs, ys) -> proxyClassifier(trainer.call(xs, ys)))
    }

    private Classifier<double[]> proxyClassifier(Classifier<double[]> real){
        return Proxy.newProxyInstance(getClass().getClassLoader(), [Classifier] as Class[], new ClassifierProxy(real)) as Classifier<double[]>
    }
}
