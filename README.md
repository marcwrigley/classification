# Decision Tree Classifier

Implementation of a two class Decision Tree Classifier

## Classes

Classifier: Abstract class, subclasses implement specific classifiers
DecisionTreeClassifier: A two class Decision Tree classifier

ClassifierBuilder: Abstract class, subclasses are used to build specific classes that extend Classifier
DecisionTreeBuilder: Used to build and test a DecisionTreeClassifier

ClassifierData: Stores data used to build and test classifiers, see Data Format section below
SplitClassifierData: Used to split ClassifierData in two. Used to split the data on a given feature, or to split the data into training and testing data.

TreeNode: A node of a Decision Tree.

ClassificationExample: Gives a simple example of building and testing a DecisionTree from a data file. Data file used can be found here:
https://www.kaggle.com/uciml/pima-indians-diabetes-database

## Data Format

When reading data from a file, data should be in a .csv or similar text file in the following format:

```
columnHeading1, columnHeading2, columnHeading3, classLabel
1.0, 2.0, 3.0, 0
1.0, 2.0, 3.0, 1
.
.
.
1.0, 2.0, 3.0, 0
```
	  
where each attribute is separated by a ","
Currently attributes should be real numbers, labels should be integers
	  

## Notes

DecisionTree classifier currently requires that input data be stored as a Double, future work will fix this issue so that input data can be of any comparable type.

