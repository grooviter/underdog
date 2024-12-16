---
date: 2024-12-20
categories:
  - ml
tags:
  - ml
---

# Classifying food

In this article I’m using Supervised Learning Classification to classify food into three categories: green, orange or red depending on whether they’re healthy or not. 

<!-- more -->

## Representation Phase

In the representation phase we should find a representative dataset and a suitable algorithm for the problem at hand. Then we use both, dataset and algorithm, to train a software model to make predictions.

### Dataset preparation

A representative dataset, in this case, would be a large dataset of food, labeled as good/bad/not-so-good food. I created a csv file with more than three thousand food entries, collected from this site (Spanish).

```groovy title="loading data"
import underdog.Underdog

def food = Underdog.df()
    .read_csv('food.csv', sep=';')
    .fillna(0) // filling all missing values with 0

food.head()
```

```shell title="output"

```

Each entry has a series of possible features and it’s labeled with a color value (TRAFFICLIGH VALUE) which depends on whether it is a healthy food, not so healthy food or junk food. Next I should choose every column that can be eligible as a feature, for example the name of the food is not a good feature if you’d like to generalize the results.

```groovy title="choosing possible features"
def COLUMNS_OF_INTEREST = [
    "TRAFFICLIGHT VALUE",
    "CARBS",
    "SUGAR",
    "ENERGY",
    "PROTEINS",
    "SATURATED FAT",
    "FAT",
    "SODIUM",
    "FIBER",
    "SALT",
]

// getting only interesting columns and drop na values
def df = food[COLUMNS_OF_INTEREST].dropna()

df.head()
```

```shell title="output"

```

However the goal is to choose the minimum set of features that maximizes the classification. Too many could classify well but it would become too hard to use, too few would not classify well enough. I need to find the balance between the two. Once I’ve found the balance I can use both, features and labels to create a training and test datasets. For that I use the train_test_split function from scikit-learn library.

```groovy title="minimum set of features and creating training and test datasets"
import Underdog

feats = df['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']
label = df['TRAFFICLIGHT VALUE']

def (X_train, X_test, y_train, y_test) = Underdog.ml()
    .utils
    .train_test_split(feats, label, random_state: 0)
```

Drawing a scatter matrix sometimes could help you to spot features that are particularly good on classifying samples. I did the matrix, but I recognize that although some of the feature-pairs are clearly better than others (e.g. proteins/carbs) many of them are inconclusive to me (e.g. salt/fat). I invite you to open the plot in a new window, full size, and take a look for yourself:

```groovy title="scatter matrix"
Underdog.plots()
    .scatterMatrix(XTrain, c: yTrain, bins: 15)
    .show()
```

Notice how I’m using the training dataset features (X_train) and the training dataset labels (y_train) to build the scatter matrix.

### Algorithm selection

In order to choose the algorithm, I needed to identify first the type of problem I was facing. Why I though this would fit as a Supervised Learning Classification Problem ?

- First, I’ve got a labeled dataset, so it looked like I could use the labeled data to train a supervised learning model. 
- Second, I was looking for different types of discrete target values (values for green, orange, red), therefore it seemed to be a classification problem.

Once I confirmed it was a classification problem I chose the only classification algorithm I know so far, the k-nearest neighbors algorithm.

## Evaluation Phase

Then we use both, dataset and algorithm, to train a software model to make predictions. Afterwards the model performance is evaluated with testing datasets. Training and testing are part of the evaluation phase.

### Model creation & training

The k-nearest neighbors algorithm is implemented in scikit-learn via the KNeighborsClassifier class. The algorithm tries to establish to which type the element belongs by checking the closest neighborg elements around. You can customize the K parameter which sets how many neighbors does the algorithm have to check before emmiting its veredict.

Here I’m initializing the algorithm with k=5. Then I’m training the model using the fit function and finally I’m checking how well the model is going to perform by passing the testing dataset (X_test, y_test) to the score function. After some tunes here and there I was able to get a 90% of accuracy by using 6 features.

```groovy title="model training and getting accuracy score with the testing dataset"
knn = Underdog.ml().classifiers.knn(xTrain, yTrain, k: 5)
knn.score(X_test, y_test) // returns 0.9 ==> 90%
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
// Every sample has to provide values for
// [CARBS, SUGAR, PROTEINS, FAT, SALT, FIBER]

tuna_olive_oil = [0, 0, 20, 33, 0.88, 0]    // expected 3
beer_one_liter = [3.4, 0.1, 0.3, 0, 0, 0]   // expected 2
coke           = [10.6, 10.6, 0, 0, 0, 0]   // expected 3
croissants     = [46, 4.5, 8.7, 26, 1.3, 0] //expected 3

// invoking prediction
predictions = knn.predict([tuna_olive_oil, beer_one_liter, coke, croissants])

// checking results
predictions == [3, 2, 3, 3]
```

## Optimization

The optimization was completely hand-crafted. But I can comment on two tools that I think helped trying to optimize the whole process:

- **Scatter matrix**. It helped me to see some features that I though for sure they were not going to work well. 
- **Model score**: I used it as a brute force mechanism. I chose the optimal set of features by running the model score until I got what I though it was a compromised between a high score and a reasonable number of features.