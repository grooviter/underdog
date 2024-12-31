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

## Simple linear regression

The most popular linear regression uses the least squares technique. It tries to find a slope (w) and constant value (b) that minimizes the mean squared error of the model. It doesn’t have parameters to control model complexity, everything it needs is estimated from training data.

!!! note "UC Irvine Dataset"

    The [dataset](https://archive.ics.uci.edu/dataset/275/bike+sharing+dataset) used for this entry is a Bike sharing 
    dataset from the [UCI Dataset repository](https://archive.ics.uci.edu/) for machine learning. 

First of all I'm loading the [Bike sharing](https://archive.ics.uci.edu/dataset/275/bike+sharing+dataset) daily dataset

```groovy title="loading data"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:import"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:load_data"
```

First, I’d like to see how features could be related to each other using a correlation heatmap:

```python title="correlation matrix"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:correlation_matrix"
```

<figure markdown="span">
![](images/linear_regression_notes/correlation_matrix.png#only-light){ width="80%" }
![](images/linear_regression_notes/correlation_matrix_dark.png#only-dark){ width="80%" }
</figure>

There are a lot of features, but I’m focusing on just choosing one, **temp** which is the normalized temperature in Celsius 
the day of the rental. I’d like to see how it looks like visually the relationship between registered number of rentals 
(registered variable) and the temperature feature I’ve chosen:

```groovy title="scatter matrix: temp vs registered"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:pair_plot"
```

<figure markdown="span">
![](images/linear_regression_notes/pair_plot.png#only-light){ width="80%" }
![](images/linear_regression_notes/pair_plot_dark.png#only-dark){ width="80%" }
</figure>

What I’m looking for at this point in the scatter plot, is tendencies. In this case it seems that points tend to go in diagonal from the bottom left to the upper right part of the graph. So far, the more tendency I see the better it seems to work. Now lets create a linear regression:

```groovy title="linear regression"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:linear_regression"
```

```shell title="output"
train: 0.26, test: 0.11
```

If we draw the regression line we’ve got:

```groovy title="scatter & line plot"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:linear_regression_plot"
```

<figure markdown="span">
![](images/linear_regression_notes/plot.png#only-light){ width="60%" }
![](images/linear_regression_notes/plot_dark.png#only-dark){ width="60%" }
</figure>

As you can see a straight line won’t be able to do good predictions. A way of helping the linear transformation to adapt 
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
train: 0.39, test: 0.22
```

Because the polynomial transformation is creating more features, they cover a wider spectrum of the data, therefore more 
likely to do better predictions, at least in the training dataset. If we draw now the result:

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

### References

- [Polynomial interpolation](https://scikit-learn.org/1.5/auto_examples/linear_model/plot_polynomial_interpolation.html#sphx-glr-auto-examples-linear-model-plot-polynomial-interpolation-py)
- [PolynomialFeatures in scikit-learn](https://scikit-learn.org/1.5/modules/generated/sklearn.preprocessing.PolynomialFeatures.html#sklearn.preprocessing.PolynomialFeatures)
- [Understanding Polynomial Features (Medium)](https://medium.com/@jonesntongana345/understanding-interaction-and-polynomial-features-in-pyspark-a-simple-guide-ef11f2e80eab)

## Regularization and normalization

### Regularization

Regularization is a technique used to reduce the model complexity and thus it helps dealing with overfitting:

- It reduces the model size by shrinking the number of parameters the model has to learn 
- It adds weight to the values so that it tries to favor smaller values

Regularization penalizes certain values by using a loss function with a cost. This cost could be of type:

- L1: The cost is proportional to the absolute value of the weight coefficients (Lasso)
- L2: The cost is proportional to the square of the value of the weight coefficients (Ridge)

!!! Tip

    Regularization really shines when there is a high dimensionality, meaning there’re multiple features. So in these 
    examples it won’t make a huge impact with the scores.

### Normalization

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
train: 0.26, test: 0.11
```

Although it seems worst than the polynomial example, the takeaway idea here is that the Ridge regression along with a 
high value of alpha is going to reduce the complexity of the model and make the generalization more stable.

Ridge regression score can be improved by applying normalization to the source dataset. Is important for some ML methods 
that all features are on the same scale. In this case we’re apply a MinMax normalization.

```python title="Ridge with scaled set"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:ridge_regression_min_max"
```

```shell title="output"
train: 0.26, test: 0.11
```


We can use the scaled X to train the Ridge regression. However there’re some basic tips to be aware of:

- Fit the scaler with the training set and then apply the same scaler to transform the training and test feature sets 
- Don’t use the test dataset to fit the scaler. That could lead to data leakage.

## Lasso

- It uses a L1 type regularization penalty, meaning it minimizes the sum of the absolute values of the coefficients
- It works as a kind of feature selection
- It also has an alpha parameter to control regularization

```python title="using lasso regression"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:lasso_regression"
```

```shell title="output"
train: 0.2842911095363777, test: 0.2813866438355652
```

And finally using MinMaxScaler to try to improve regression scoring:

```python title="lasso with scaled features"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:lasso_regression_min_max"
```

```shell title="output"
train: 0.2865231606947747, test: 0.285332265748411
```

## Results summary

Finally I’ve written a summary table.

| TYPE   | METHOD              | POLYNOMIAL | NORMALIZATION | REGULARIZATION | TRAIN SCORE | TEST_SCORE |
|--------|---------------------|------------|---------------|----------------|-------------|------------|
| Linear | linearRegression    | No         | No            | No             | 0.2         | 0.2        |
| Linear | linearRegression    | Yes        | No            | No             | 0.2         | 0.2        |
| Ridge  | linearRegression    | Yes        | No            | No             | 0.2         | 0.2        |
| Ridge  | linearRegression    | No         | Yes           | Yes            | 0.2         | 0.2        |
| Lasso  | linearRegression    | No         | No            | Yes            | 0.2         | 0.2        |
| Lasso  | linearRegression    | No         | Yes           | Yes            | 0.2         | 0.2        |

## Feature selection (Lasso)

So far I’ve been working with just one feature temp to predict a possible outcome. I chose this feature by using the 
correlation table as a guide. When looking for just one variable to work with, it could be enough, but when looking for 
many possible features it could be cumbersome. The Lasso regression seems a better method for telling me which features 
do perform and which don’t. How ? Well according to how the L1 regulation method works, keeping it short, those features 
that are not so important, Lasso makes its coefficient equal to 0, therefore, those features having a coefficient 
greater than 0 are worth using them to train the model (the higher the better). Lets use this knowledge to know which 
features could be useful to train the model.

```python title="using all possible features to see which one fits best in case we only want to use one"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:import"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:import_json"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:lasso_feature_extraction"
```
Which shows the following map:

```shell title="features along with their coefficients"
{
    "season": 148.203100031951,
    "mnth": -51.5955943027072,
    "holiday": -133.05061286830426,
    "weekday": 29.090724044926613,
    "workingday": 801.273893612997,
    "weathersit": -359.44201023763367,
    "temp": -5220.004446707639,
    "atemp": 10331.516802106882,
    "hum": -1420.219253743334,
    "windspeed": -1418.2069786643817
}
```

Now as the theory stated, we can discard those features with 0 value, and maybe those which are negatively correlated. 
For this example, where I’m only interested in one feature to validate whether I chose the most significant feature or 
not. In this case I’m getting the feature with the highest possitive coefficient:

```python
--8<-- "src/test/groovy/underdog/blog/y2024/m12/LinearRegressionNotesSpec.groovy:best_features"
```

```shell title="output"
['atemp', 'workingday', 'season', 'weekday']
```

## Ridge vs Lasso

In this case we’ve used both algorithms with the same dataset, but there’re situations where one or the other fit best:

- Ridge: Many small/medium sized effects
- Lasso: Few medium/large sized effects