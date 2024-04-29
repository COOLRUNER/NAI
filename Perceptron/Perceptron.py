import pandas as pd
import numpy as np
import random


class Perceptron:
    def __init__(self, learning_rate = 0.1):
        self.weights = []
        self.bias = 0.5
        self.learning_rate = learning_rate

    def load_data(self, path):
        with open(path, 'r') as file:
            x = []
            y = []
            for line in file:
                x_ = [float(i) for i in line.strip().split(',')[:-1]]
                if line.strip().split(',')[-1] == 'Iris-versicolor':
                    y.append(0)
                else:
                    y.append(1)
                x.append(x_)
        return x, y

    def predict(self, x):
        output = 0
        for i in range(len(self.weights)):
            output += self.weights[i] * x[i]
        output -= self.bias
        if output > 0:
            return 1
        else:
            return 0

    def train(self, x, y, epochs):
        for _ in range(len(x[0])):
            self.weights.append(random.uniform(0, 1))
        for _ in range(epochs):
            for xi, target in zip(x, y):
                output = self.predict(xi)
                update = self.learning_rate * (target - output)
                for i in range(len(self.weights)):
                    self.weights[i] += update * xi[i]
                self.bias -= update

    def find_accuracy(self, x, y):
        counter = 0
        for i in range(len(x)):
            if self.predict(x[i]) == y[i]:
                counter += 1
        accuracy = counter / len(y)
        return accuracy

    def classify(self, x):
        return self.predict(x)

    def load_and_test(self, test_path):
        x_test, y_test = self.load_data(test_path)
        return self.find_accuracy(x_test, y_test)

    def manual_input(self, vector):
        if self.classify(np.array(vector)) == 1:
            return 'Iris-virginica'
        else:
            return 'Iris-versicolor'


class main:
    perceptron = Perceptron(learning_rate=0.01)
    x_train, y_train = perceptron.load_data('perceptron.data')
    perceptron.train(x_train, y_train, epochs=10000)
    accuracy = perceptron.load_and_test('perceptron.test.data')
    print(f'Test accuracy: {round((accuracy * 100), 2)}%')
    vector = [4.9,2.4,3.3,1.0]
    print(f'Classification result: {perceptron.manual_input(vector)}')
