from flask import Flask, render_template, request, jsonify
import joblib
import pandas as pd
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import LabelEncoder
import nltk
from nltk.corpus import stopwords
import string
from nltk.stem.porter import PorterStemmer
from sklearn.feature_extraction.text import TfidfVectorizer

app = Flask(__name__)

# Create a PorterStemmer instance
ps = PorterStemmer()

def transform_text(text):
    text = text.lower()
    text = nltk.word_tokenize(text)

    y = []
    for i in text:
        if i.isalnum():
            y.append(i)

    text = y[:]
    y.clear()

    for i in text:
        if i not in stopwords.words('english') and i not in string.punctuation:
            y.append(i)

    text = y[:]
    y.clear()

    for i in text:
        y.append(ps.stem(i))

    return " ".join(y)

# Load the pre-trained model and vectorizer from the pipeline
model_pipeline = joblib.load('model_pipeline.pkl')

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/classify', methods=['POST'])
def classify_email():
    try:
        # Get the email content from the request
        data = request.json
        email_content = data['emailContent']

        # Transform the input text
        transformed_text = transform_text(email_content)

        # Predict using the pre-trained model
        result = model_pipeline.predict([transformed_text])[0]

        if result == 1:
            return jsonify({'result': "Spam"})
        else:
            return jsonify({'result': "Not Spam"})

    except Exception as e:
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=True)
