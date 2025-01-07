---
date: 2024-12-20
categories:
  - ml
tags:
  - ml
  - classification
---

# Linear regression notes

Classification is a great method to predict discrete values from a given dataset, but sometimes you need to predict a continuous value, e.g: height, weight, prices…​ And that’s when linear regression techniques come handy.

<!-- more -->

The definition that I read in the Wikipedia didn’t help me at all. Instead when I related it with a line, it started to make sense to me. If we’ve got a linear function, that is, a function describing a line, where ŵ is the slope of the line and b is called the intercept which is a constant value:

<figure markdown="span">
![](images/linear_regression_notes/plot.png#only-light){ width="60%" }
![](images/linear_regression_notes/plot_dark.png#only-dark){ width="60%" }
</figure>

For every x value a new point will be drawn and eventually altogether will form a line. So, if you think about it visually, given a set of input values, a simple linear regression algorithm will try to come up with a line trying to pass as close as possible to the majority of the input dataset points. So if you try to predict an output value from the input values, the machine learning process will pick up a value from that line.

There are differences between the types of linear regression techniques depending on the presence of regularization (Ridge and Lasso), or the lack of it (Simple Linear Regression). There’s also important the use of polynomial transformation and normalization.

## Prerequisites

For this entry you should need the following dependencies:

=== "Gradle"
    ```groovy
    implementation "com.github.grooviter:underdog-ml:VERSION"
    implementation "com.github.grooviter:underdog-plots:VERSION"
    ```
=== "Maven"
    ```xml
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-ml</artifactId>
        <version>VERSION</version>
    </dependency>
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-plots</artifactId>
        <version>VERSION</version>
    </dependency>
    ```
=== "Grapes"
    ```groovy
    @Grapes([
    @Grab("com.github.grooviter:underdog-ml:VERSION"),
    @Grab("com.github.grooviter:underdog-plots:VERSION")
    ])
    ```

!!! note

    ml and plots modules already have underdog-dataframe dependency as transitive dependency so you don't have to explicitly declare it.

## Simple linear regression

The most popular linear regression uses the least squares technique. It tries to find a slope (w) and constant value (b) that minimizes the mean squared error of the model. It doesn’t have parameters to control model complexity, everything it needs is estimated from training data.

!!! note "UC Irvine Dataset"

    The [dataset](https://archive.ics.uci.edu/dataset/275/bike+sharing+dataset) used for this entry is a Bike sharing 
    dataset from the [UCI Dataset repository](https://archive.ics.uci.edu/) for machine learning. 

First of all I'm loading the [Bike sharing](https://archive.ics.uci.edu/dataset/275/bike+sharing+dataset) daily dataset. We are removing non-numerical series and rows with
missing values:

```groovy title="loading data"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:import"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:load_data"
```

Which outputs:

```shell
                                            day.csv
 season  |  mnth  |  holiday  |  weekday  |  workingday  |  weathersit  |    temp    |   atemp    |    hum     |  windspeed  |  registered  |
---------------------------------------------------------------------------------------------------------------------------------------------
      1  |     1  |        0  |        6  |           0  |           2  |  0.344167  |  0.363625  |  0.805833  |   0.160446  |         654  |
      1  |     1  |        0  |        0  |           0  |           2  |  0.363478  |  0.353739  |  0.696087  |   0.248539  |         670  |
      1  |     1  |        0  |        1  |           1  |           1  |  0.196364  |  0.189405  |  0.437273  |   0.248309  |        1229  |
      1  |     1  |        0  |        2  |           1  |           1  |       0.2  |  0.212122  |  0.590435  |   0.160296  |        1454  |
      1  |     1  |        0  |        3  |           1  |           1  |  0.226957  |   0.22927  |  0.436957  |     0.1869  |        1518  |
```

First, I’d like to see how features could be related to each other using a correlation heatmap:

```groovy title="correlation matrix"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:correlation_matrix"
```

<figure markdown="span">
![](images/linear_regression_notes/correlation_matrix.png#only-light){ width="75%" }
![](images/linear_regression_notes/correlation_matrix_dark.png#only-dark){ width="75%" }
</figure>

There are a lot of features, but I’m focusing on just choosing one, **temp** which is the normalized temperature in Celsius 
the day of the rental. I’d like to see how it looks like visually the relationship between registered number of rentals 
(registered variable) and the temperature feature I’ve chosen:

```groovy title="scatter matrix: temp vs registered"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:pair_plot"
```

<figure markdown="span">
![](images/linear_regression_notes/pair_plot.png#only-light){ width="75%" }
![](images/linear_regression_notes/pair_plot_dark.png#only-dark){ width="75%" }
</figure>

What I’m looking for at this point in the scatter plot, is tendencies. In this case it seems that points tend to go in diagonal from the bottom left to the upper right part of the graph. So far, the more tendency I see the better it seems to work. Now lets create a linear regression:

```groovy title="linear regression"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:linear_regression"
```

```shell title="output"
train: 0.49, test: -1.22
```

If we draw the regression line we’ve got:

```groovy title="scatter & line plot"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:linear_regression_plot"
```

<figure markdown="span">
![](images/linear_regression_notes/plot.png#only-light){ width="60%" }
![](images/linear_regression_notes/plot_dark.png#only-dark){ width="60%" }
</figure>

A straight line won’t be able to do good predictions. A way of helping the linear transformation to adapt 
better to the shape of the model is to use a polynomial transformation.

## Polynomial transformation

When the problem doesn't fit easily a straight line or there are many features, it could become complicated to find a 
good relationship between them, specially with a simple line. The polynomial transformation helps finding those 
relationships. Applying a polynomial transformation to our problem can help the linear regression to adapt better to the 
shape of the data. This is the same linear regression example, but this time applying the PolynomialFeatures class prior 
to the linear regression fit.

```groovy title="applying polynomial transformation"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:linear_regression_polynomial"
```

```shell title="output"
train: 0.52, test: -0.96
```

Because the polynomial transformation is creating more features, they cover a wider spectrum of the data, therefore more 
likely to do improve accuracy, at least in the training dataset. If we draw now the result:

```groovy title="linear regression polynomial plot"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:linear_regression_polynomial_plot"
```

<figure markdown="span">
![](images/linear_regression_notes/plot_polynomial.png#only-light){ width="80%" }
![](images/linear_regression_notes/plot_polynomial_dark.png#only-dark){ width="80%" }
</figure>

Which covers much more than the previous example. However there are a couple of things to keep in mind when applying 
the polynomial transformation:

- Polynomial transformation with a high degree value could overfit the model
- It’s better to combine it with a regularized regression method like Ridge.

However so far it's clear that with just one feature we don't go anywhere as the models we've got so far barely work for training set and are useless for test sets. In regularization and normalization we will be using more features to try to create a viable model.

**References**

- [Polynomial interpolation](https://scikit-learn.org/1.5/auto_examples/linear_model/plot_polynomial_interpolation.html#sphx-glr-auto-examples-linear-model-plot-polynomial-interpolation-py)
- [PolynomialFeatures in scikit-learn](https://scikit-learn.org/1.5/modules/generated/sklearn.preprocessing.PolynomialFeatures.html#sklearn.preprocessing.PolynomialFeatures)
- [Understanding Polynomial Features (Medium)](https://medium.com/@jonesntongana345/understanding-interaction-and-polynomial-features-in-pyspark-a-simple-guide-ef11f2e80eab)

## Feature selection

So far I’ve been working with just one feature temp to predict a possible outcome. I chose this feature by using the
correlation table as a guide. When looking for just one variable to work with, it could be enough, but when looking for
many possible features it could be cumbersome. Lasso regression seems a better method for telling me which features
do perform and which don’t. How ? Well according to how the L1 regulation method works, keeping it short, those features
that are not so important, Lasso makes its coefficient equal to 0, therefore, those features having a coefficient
greater than 0 are worth using them to train the model (the higher the better). Lets use this knowledge to know which
features could be useful to train the model.


```groovy title="using all possible features to see which one fits best in case we only want to use one"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:import"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:import_json"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:lasso_feature_extraction"
```
Which shows the following map:

```shell title="features along with their coefficients"
{
    "season": 424.11017152754937,
    "mnth": -14.868338993584615,
    "holiday": -211.17143310755654,
    "weekday": 36.06114879750305,
    "workingday": 941.7383145447468,
    "weathersit": -397.9241830877648,
    "temp": 1136.7307642112696,
    "atemp": 2730.771127273043,
    "hum": -1670.0999227731259,
    "windspeed": -2200.526972819417
}
```
Now as the theory stated, we can discard those features with 0 value, and maybe those which are negatively correlated.
For this example, where I’m only interested in one feature to validate whether I chose the most significant feature or
not. In this case I’m getting the feature with the highest possitive coefficient:

```groovy
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:best_features"
```

```shell title="output"
['atemp', 'temp', 'workingday', 'season', 'weekday']
```

## Regularization and normalization

**Regularization**

Regularization is a technique used to reduce the model complexity and thus it helps dealing with overfitting:

- It reduces the model size by shrinking the number of parameters the model has to learn 
- It adds weight to the values so that it tries to favor smaller values

Regularization penalizes certain values by using a loss function with a cost. This cost could be of type:

- L1: The cost is proportional to the absolute value of the weight coefficients (Lasso)
- L2: The cost is proportional to the square of the value of the weight coefficients (Ridge)

!!! Tip

    Regularization really shines when there is a high dimensionality, meaning there’re multiple features. So in these 
    examples it won’t make a huge impact with the scores.

**Normalization**

Data normalization is the process of rescaling one or more features to a common scale. It’s normally used when features used to create the model have different scales. There are a few advantages of using normalization is such scenario:

- It could improve the numerical stability of your model 
- It could speed up the training process

Normalization is specially important when applying certain regression techniques, as regression is sensitive to model feature adjustements.

!!! Tip

    Because in this article I’m only using ONE feature, normalization is not going to make much difference but, when 
    using multiple features, and each of them in different scales, then we should use normalization. 

## Ridge

- Follows the leat-squares criterion but it uses regularization as a penalty for large variations in w parameters. 
- Regularization prevents overfitting by restricting the model, it normally reduces its complexity 
- Regularization is controlled by the alpha parameter 
- The high the value of alpha the simpler the model, that is, the model is less likely to overfit

Now I’m using Ridge class with the same dataset:

```groovy title="using Ridge regression"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:ridge_regression"
```

Giving me the following scores:

```shell title="output"
train: 0.68, test: -1.04
```

Although it seems worst than the polynomial example, the takeaway idea here is that the Ridge regression along with a 
high value of alpha is going to reduce the complexity of the model and make the generalization more stable.

Ridge regression score can be improved by applying normalization to the source dataset. Is important for some ML methods 
that all features are on the same scale. In this case we’re apply a MinMax normalization.

```groovy title="Ridge with scaled set"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:ridge_regression_min_max"
```

```shell title="output"
train: 0.68, test: -1.04
```


We can use the scaled X to train the Ridge regression. However there’re some basic tips to be aware of:

- Fit the scaler with the training set and then apply the same scaler to transform the training and test feature sets 
- Don’t use the test dataset to fit the scaler. That could lead to data leakage.

## Lasso

- It uses a L1 type regularization penalty, meaning it minimizes the sum of the absolute values of the coefficients
- It works as a kind of feature selection
- It also has an alpha parameter to control regularization

```groovy title="using lasso regression"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:lasso_regression"
```

```shell title="output"
train: 0.68, test: -1.03
```

And finally using MinMaxScaler to try to improve regression scoring:

```groovy title="lasso with scaled features"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:lasso_regression_min_max"
```

```shell title="output"
train: 0.49, test: -1.22
```

Unfortunately this got it worst.

!!! tip "Ridge vs Lasso"

    In this case we’ve used both algorithms with the same dataset, but there’re situations where one or the other fit best:

    - Ridge: Many small/medium sized effects
    - Lasso: Few medium/large sized effects

## PCA

Principal component analysis (PCA) is an orthogonal linear transformation that transforms a number of possibly correlated variables into a smaller number of uncorrelated variables called principal components. Long story short tries to do the same model with less features involved doing a type of data compression.

```groovy title="pca"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:pca"
```

```shell title="output"
train: 0.65, test: -1.04
```

## Summary

Finally I’ve written a summary table.

| TYPE         | POLYNOMIAL                             | NORMALIZATION                        | REGULARIZATION                        | TRAIN SCORE | TEST_SCORE  |
|--------------|----------------------------------------|--------------------------------------|---------------------------------------|-------------|-------------|
| OLS          | :fontawesome-regular-circle-xmark:     | :fontawesome-regular-circle-xmark:   | :fontawesome-regular-circle-xmark:    | 0.49        | -1.22       |
| OLS          | :fontawesome-regular-circle-check:     | :fontawesome-regular-circle-check:   | :fontawesome-regular-circle-xmark:    | 0.52        | -0.96       |
| Ridge        | :fontawesome-regular-circle-xmark:     | :fontawesome-regular-circle-xmark:   | :fontawesome-regular-circle-check:    | 0.68        | -1.04       |
| Ridge        | :fontawesome-regular-circle-xmark:     | :fontawesome-regular-circle-check:   | :fontawesome-regular-circle-check:    | 0.68        | -1.04       |
| Lasso        | :fontawesome-regular-circle-xmark:     | :fontawesome-regular-circle-xmark:   | :fontawesome-regular-circle-check:    | 0.68        | -1.03       |
| Lasso        | :fontawesome-regular-circle-xmark:     | :fontawesome-regular-circle-check:   | :fontawesome-regular-circle-check:    | 0.49        | -1.22       |
| OLS (PCA)    | :fontawesome-regular-circle-xmark:     | :fontawesome-regular-circle-check:   | :fontawesome-regular-circle-check:    | 0.65        | -1.04       |