from flask import Flask, request, jsonify
import re
import pandas as pd
from sklearn import preprocessing
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
import numpy as np
import csv
import warnings
import pickle

app = Flask(__name__)
warnings.filterwarnings("ignore", category=DeprecationWarning)

# Load Data
data_training = pd.read_csv('DATA/Training.csv')
data_testing = pd.read_csv('DATA/Testing.csv')

cols = data_training.columns
cols = cols[:-1]

x = data_training[cols]
y = data_training['prognosis']

reduced_data = data_training.groupby(data_training['prognosis']).max()

le = preprocessing.LabelEncoder()
le.fit(y)
y = le.transform(y)

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.33, random_state=42)
testx = data_testing[cols]
testy = data_testing['prognosis']
testy = le.transform(testy)

clf1 = DecisionTreeClassifier()
clf = clf1.fit(x_train, y_train)

importances = clf.feature_importances_
indices = np.argsort(importances)[::-1]
features = cols

with open('MODEL-SYNCO.pkl', 'wb') as file:
    pickle.dump(clf, file)

severityDictionary = dict()
description_list = dict()
precautionDictionary = dict()
symptoms_dict = {}


@app.route('/')
def index():
    return "Hallo ini adalah API untuk prediksi gejala penyakit!"

@app.route('/predict', methods=['POST'])
def predict_disease():
    try:
        data = request.get_json()
        symptoms_input = data['symptoms']
        prediction_result = process_symptoms(symptoms_input)

        response = {
            'prediction': prediction_result
        }

        return jsonify(response)
    except Exception as e:
        return jsonify({'message': 'An error occurred: ' + str(e)}), 500
        

def process_symptoms(symptoms_exp):
    prediction = sec_predict(symptoms_exp)
    return prediction.tolist()

def sec_predict(symptoms_exp):
    df = pd.read_csv('DATA/Training.csv')
    X = df.iloc[:, :-1]
    y = df['prognosis']
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=20)
    rf_clf = DecisionTreeClassifier()
    rf_clf.fit(X_train, y_train)

    symptoms_dict = {symptom: index for index, symptom in enumerate(X)}
    input_vector = np.zeros(len(symptoms_dict))
    for item in symptoms_exp:
        input_vector[[symptoms_dict[item]]] = 1

    return rf_clf.predict([input_vector])

if __name__ == '__main__':
    app.run(debug=False,host='0.0.0.0')
