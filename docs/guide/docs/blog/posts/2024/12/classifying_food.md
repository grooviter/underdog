---
date: 2024-12-20
categories:
  - ml
tags:
  - ml
  - classification
---

# Classifying food

In this article I’m using Supervised Learning Classification to classify food into three categories: green, orange or red depending on whether they’re healthy or not. 

<!-- more -->

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

## Representation Phase


In the representation phase we should find a representative dataset and a suitable algorithm for the problem at hand. Then we use both, dataset and algorithm, to train a software model to make predictions.

### Dataset preparation

A representative dataset, in this case, would be a large dataset of food, labeled as good/bad/not-so-good food. I created a csv file with more than three thousand food entries, collected from this site (Spanish).

```groovy title="loading data"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:imports"

--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:read_csv"
```

```shell title="output"
  ID   |               NAME                |  BRAND ID  |    BRAND     |  GROUP ID  |        GROUP NAME        |  SUBGROUP ID  |     SUBGROUP NAME      |  SPECIAL  |  TRAFFICLIGHT VALUE  |  PYRAMID VALUE  |  CARBS  |  SUGAR  |  ENERGY  |  PROTEINS  |  SATURATED FAT  |  FAT  |  SALT  |  SODIUM  |  FIBER  |
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  891  |       Aceitunas negras sin hueso  |        76  |      Ybarra  |         8  |                  Frutas  |           24  |            Procesados  |       No  |                   3  |             2A  |      0  |      0  |     129  |       0.5  |            2.2  |   13  |   1.5  |     0.6  |      0  |
  848  |                          Acelgas  |        78  |  Verdifresh  |         9  |   Verduras y hortalizas  |           24  |            Procesados  |       No  |                   1  |             2A  |    3.7  |      0  |      21  |      1.68  |              0  |  0.2  |  0.53  |       0  |    1.6  |
 1118  |                          Acelgas  |        70  |    Florette  |         9  |   Verduras y hortalizas  |           24  |            Procesados  |       No  |                   1  |             2A  |    2.1  |    1.1  |      21  |       1.8  |              0  |  0.2  |  0.53  |       0  |    1.6  |
 2856  |       Acelgas primera al natural  |       150  |     Helio's  |         9  |   Verduras y hortalizas  |           12  |              Conserva  |       No  |                   1  |             2A  |    4.5  |    3.5  |      25  |       1.2  |              0  |    0  |   0.2  |    0.08  |      1  |
 2903  |  Acelgas troceadas calidad extra  |       130  |     Gvtarra  |         9  |   Verduras y hortalizas  |           12  |              Conserva  |       No  |                   3  |             2A  |    1.2  |      0  |      12  |       0.9  |              0  |    0  |  0.88  |   0.352  |    1.7  |
 2911  |    Acelgas, patatas y zanahorias  |       130  |     Gvtarra  |         9  |   Verduras y hortalizas  |           12  |              Conserva  |       No  |                   2  |             2A  |    3.6  |    0.5  |      20  |       0.7  |              0  |    0  |   0.7  |    0.28  |    1.3  |
 2715  |                    Agua de limón  |       175  |  Font Vella  |         5  |  Bebidas no alcohólicas  |            6  |  Bebidas refrescantes  |       No  |                   3  |              0  |    4.2  |    4.2  |      17  |         0  |              0  |    0  |  0.02  |   0.008  |      0  |
 2713  |                  Agua de manzana  |       175  |  Font Vella  |         5  |  Bebidas no alcohólicas  |            6  |  Bebidas refrescantes  |       No  |                   3  |              0  |    4.1  |    4.1  |      17  |         0  |              0  |    0  |  0.01  |   0.004  |      0  |
 2718  |                  Agua de naranja  |       175  |  Font Vella  |         5  |  Bebidas no alcohólicas  |            6  |  Bebidas refrescantes  |       No  |                   3  |              0  |    4.2  |    4.2  |      19  |         0  |              0  |    0  |  0.01  |   0.004  |      0  |
 2719  |                     Agua de piña  |       175  |  Font Vella  |         5  |  Bebidas no alcohólicas  |            6  |  Bebidas refrescantes  |       No  |                   3  |              0  |    4.2  |    4.2  |      17  |         0  |              0  |    0  |  0.01  |   0.004  |      0  |
```

Each entry has a series of possible features and it’s labeled with a color value (TRAFFICLIGH VALUE) which depends on whether it is a healthy food, not so healthy food or junk food. Next I should choose every column that can be eligible as a feature, for example the name of the food is not a good feature if you’d like to generalize the results.

```groovy title="choosing possible features"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:narrowing_dataframe"
```

```shell title="output"
 TRAFFICLIGHT VALUE  |  CARBS  |  SUGAR  |  ENERGY  |  PROTEINS  |  SATURATED FAT  |  FAT  |  SODIUM  |  FIBER  |  SALT  |
--------------------------------------------------------------------------------------------------------------------------
                  3  |      0  |      0  |     129  |       0.5  |            2.2  |   13  |     0.6  |      0  |   1.5  |
                  1  |    3.7  |      0  |      21  |      1.68  |              0  |  0.2  |       0  |    1.6  |  0.53  |
                  1  |    2.1  |    1.1  |      21  |       1.8  |              0  |  0.2  |       0  |    1.6  |  0.53  |
                  1  |    4.5  |    3.5  |      25  |       1.2  |              0  |    0  |    0.08  |      1  |   0.2  |
                  3  |    1.2  |      0  |      12  |       0.9  |              0  |    0  |   0.352  |    1.7  |  0.88  |
                  2  |    3.6  |    0.5  |      20  |       0.7  |              0  |    0  |    0.28  |    1.3  |   0.7  |
                  3  |    4.2  |    4.2  |      17  |         0  |              0  |    0  |   0.008  |      0  |  0.02  |
                  3  |    4.1  |    4.1  |      17  |         0  |              0  |    0  |   0.004  |      0  |  0.01  |
                  3  |    4.2  |    4.2  |      19  |         0  |              0  |    0  |   0.004  |      0  |  0.01  |
                  3  |    4.2  |    4.2  |      17  |         0  |              0  |    0  |   0.004  |      0  |  0.01  |
```

However the goal is to choose the minimum set of features that maximizes the classification. Too many could classify well but it would become too hard to use, too few would not classify well enough. I need to find the balance between the two. Once I’ve found the balance I can use both, features and labels to create a training and test datasets. For that I use the **trainTestSplit** function.

```groovy title="minimum set of features and creating training and test datasets"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:train_test_split"
```

Drawing a scatter matrix sometimes could help you to spot features that are particularly good on classifying samples. I did the matrix, but I recognize that although some of the feature-pairs are clearly better than others (e.g. proteins/carbs) many of them are inconclusive to me (e.g. salt/fat). I invite you to open the plot in a new window, full size, and take a look for yourself:

```groovy title="scatter matrix"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:scatter_matrix"
```

<figure markdown="span">
![scatter matrix](images/classifying_food/scatter_matrix.png#only-light)
![scatter matrix](images/classifying_food/scatter_matrix_dark.png#only-dark)
</figure>

### Algorithm selection

In order to choose the algorithm, I needed to identify first the type of problem I was facing. Why I though this would fit as a Supervised Learning Classification Problem ?

- First, I’ve got a labeled dataset, so it looked like I could use the labeled data to train a supervised learning model. 
- Second, I was looking for different types of discrete target values (values for green, orange, red), therefore it seemed to be a classification problem.

Once I confirmed it was a classification problem I picked the [k-nearest neighbors](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) algorithm.

## Evaluation Phase

Then we use both, dataset and algorithm, to train a software model to make predictions. Afterwards the model performance is evaluated with testing datasets. Training and testing are part of the evaluation phase.

### Model creation & training
The k-nearest neighbors algorithm tries to establish to which type the element belongs by checking the closest neighborg elements around. You can customize the K parameter which sets how many neighbors does the algorithm have to check before emmiting its veredict.

Here I’m initializing the algorithm with k=5. Then I’m training the model using the fit function and finally I’m checking how well the model is going to perform by passing the testing dataset (X_test, y_test) to the score function. I was able to get more than 80% of accuracy by using 6 features.

```groovy title="model training and getting accuracy score with the testing dataset"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:knn_predictions"
```

```groovy title="accuracy check"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:accuracy_check"
```

### Model testing

To get a prediction I need to provide the following measurements to the model:

- CARBS 
- SUGAR 
- PROTEINS 
- FAT 
- SALT 
- FIBER

I’m passing as many samples as I want to the KNeighborsClassifier’s predict function and for every sample I’m getting a prediction. Every sample is passed as an array with the required feature values. In this case I’m using another online supermarket dataset to test the model with other datasets than the training and testing datasets.

```groovy title="prediction"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:food_samples_predictions"
```

With the expected output of:

```groovy title="sample predictions"
--8<-- "src/test/groovy/underdog/blog/y2024/m12/ClassifyingFoodSpec.groovy:food_samples_predictions_expectations"
```

Which matches the initial expectations over these samples.

## Optimization

The optimization was completely hand-crafted. But I can comment on two tools that I think helped trying to optimize the whole process:

- **Scatter matrix**. It helped me to see some features that I though for sure they were not going to work well. 
- **Model score**: I used it as a brute force mechanism. I chose the optimal set of features by running the model score until I got what I though it was a compromised between a high score and a reasonable number of features.