import re
import pandas as pd
import pyttsx3
from sklearn import preprocessing
from sklearn.tree import DecisionTreeClassifier,_tree
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
from sklearn.svm import SVC
import csv
import warnings
import json
import pickle
from keras.models import load_model
import tensorflow as tf
warnings.filterwarnings("ignore", category=DeprecationWarning)

#Data Training dan Testing
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
testx    = data_testing[cols]
testy    = data_testing['prognosis']  
testy    = le.transform(testy)

clf1 = DecisionTreeClassifier()
clf = clf1.fit(x_train,y_train)
scores = cross_val_score(clf, x_test, y_test, cv=3)

importances = clf.feature_importances_
indices = np.argsort(importances)[::-1]
features = cols

#Inisialisasi Variabel
severityDictionary = dict()
description_list = dict()
precautionDictionary =dict()
symptoms_dict = {}

for index, symptom in enumerate(x):
    symptoms_dict[symptom] = index

def calc_condition(exp,days):
    sum=0
    for item in exp:
         sum=sum+severityDictionary[item]
    if((sum*days)/(len(exp)+1)>13):
        print("\nAnda harus berkonsultasi pada Dokter\n")
        #json.dumps({"message":"Anda harus berkonsultasi pada Dokter"})
    else:
        print("\nMungkin ini hal yang buruk, tetapi anda harus mengambil tindakan pencegahan\n")
        #json.dumps({"message":"Mungkin ini hal yang buruk, tetapi anda harus mengambil tindakan pencegahan"})

def getDescription():
    global description_list
    with open('MASTERDATA/symptom_Description.csv') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        for row in csv_reader:
            _description={row[0]:row[1]}
            description_list.update(_description)

def getSeverityDict():
    global severityDictionary
    with open('MASTERDATA/symptom_severity.csv') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        try:
            for row in csv_reader:
                _diction={row[0]:int(row[1])}
                severityDictionary.update(_diction)
        except:
            pass

def getprecautionDict():
    global precautionDictionary
    with open('MASTERDATA/symptom_precaution.csv') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        for row in csv_reader:
            _prec={row[0]:[row[1],row[2],row[3],row[4]]}
            precautionDictionary.update(_prec)

#batas 25 list gejala
def check_pattern(dis_list, inp, max_items=25):
    pred_list = []
    inp = inp.replace(' ', '_')
    patt = f"{inp}"
    regexp = re.compile(patt)
    pred_list = [item for item in dis_list if regexp.search(item)]
    
    if len(pred_list) > max_items:
        print(f"{max_items} Gejala Teratas :")
        pred_list = pred_list[:max_items]

    if len(pred_list) > 0:
        return 1, pred_list
    else:
        return 0, []
    
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


def print_disease(node):
    node = node[0]
    val  = node.nonzero() 
    disease = le.inverse_transform(val[0])
    return list(map(lambda x:x.strip(),list(disease)))

def tree_to_code(tree, feature_names):
    tree_ = tree.tree_
    feature_name = [
        feature_names[i] if i != _tree.TREE_UNDEFINED else "undefined!"
        for i in tree_.feature
    ]

    chk_dis=",".join(feature_names).split(",")
    symptoms_present = []

    while True:
        
        print(getSeverityDict())
        #print("Masukan gejala yang dialami",end=":")
        disease_input = input("")
        conf,cnf_dis=check_pattern(chk_dis,disease_input)
        if conf==1:
            for num,it in enumerate(cnf_dis):
                print(num+1,".",it)
            if num!=0:
                print(f"\n:", end="")
                conf_inp = int(input(""))
            else:
                conf_inp=0

            disease_input=cnf_dis[conf_inp]
            break
        else:
            print("Pilih nama gejala yang valid")

    while True:
        try:
            num_days=int(input("Berapa hari gejala tersebut terjadi : "))
            break
        except:
            print("Masukan gejala yang valid")

    def recurse(node, depth):
        indent = "  " * depth
        if tree_.feature[node] != _tree.TREE_UNDEFINED:
            name = feature_name[node]
            threshold = tree_.threshold[node]

            if name == disease_input:
                val = 1
            else:
                val = 0
            if  val <= threshold:
                recurse(tree_.children_left[node], depth + 1)
            else:
                symptoms_present.append(name)
                recurse(tree_.children_right[node], depth + 1)
        else:
            present_disease = print_disease(tree_.value[node])
            red_cols = reduced_data.columns 
            symptoms_given = red_cols[reduced_data.loc[present_disease].values[0].nonzero()]
            print("Apakah ada pengalaman lain ?")
            symptoms_exp=[]

            #Gejala yang relevan dengan pilihan pertama
            print(list(symptoms_given))

            for syms in list(symptoms_given):                
                inp=""
                print(syms,"? : ",end='')
                while True:
                    inp=input("")
                    if(inp=="ya" or inp=="tidak"):
                        break
                    else:
                        print("(ya/tidak) : ",end="")
                if(inp=="ya"):
                    symptoms_exp.append(syms)
            
            #hasil gejala input
            print(symptoms_exp)

            #prediksi fungsi dengan list gejala
            second_prediction = sec_predict(symptoms_exp)

            calc_condition(symptoms_exp,num_days)
            if(present_disease[0]==second_prediction[0]):
                print("Kamu mempunyai gejala penyakit ", present_disease[0])
                print(description_list[present_disease[0]])

            else:
                print("Kamu mempunyai gejala penyakit ", present_disease[0], "or ", second_prediction[0])
                print(description_list[present_disease[0]])
                print(description_list[second_prediction[0]])

            precution_list=precautionDictionary[present_disease[0]]
            print("Rekomendasi penangganan : ")
            for  i,j in enumerate(precution_list):
                print(i+1,".",j)

    recurse(0, 1)
getSeverityDict()
getDescription()
getprecautionDict()
tree_to_code(clf,cols)
print(cols)



save = {
    'model': clf,
    'label_encoder': le,
    'getSeverityDict': getSeverityDict,
    'getDescription': getDescription,
    'getprecautionDict': getprecautionDict,
    'tree_to_code': tree_to_code,
}

with open('model_dtree.pkl', 'wb') as file:
    pickle.dump(save, file)
