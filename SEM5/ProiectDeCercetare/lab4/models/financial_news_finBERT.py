# -*- coding: utf-8 -*-
"""Untitled6.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1HQR-1GW724DPOysijrTUkHI0i3TMYuxj
"""

from transformers import pipeline, AutoTokenizer, AutoModelForSequenceClassification

# Load pre-trained FinBERT model and tokenizer from ProsusAI repository
tokenizer = AutoTokenizer.from_pretrained("ProsusAI/finbert")
model = AutoModelForSequenceClassification.from_pretrained("ProsusAI/finbert")

# Initialize a pipeline for text classification
pipe = pipeline("text-classification", model=model, tokenizer=tokenizer)

# Example input financial news text
text1 = "Stock prices are rising as investors are optimistic about the future of technology companies."
text2 = "Stock prices are damaged by the starting of the Russian war, but they will recover"
text3 = "Preturile la bursa se plafoneaza global"

# Get sentiment prediction for the input text
result1 = pipe(text1)
result2 = pipe(text2)
result3 = pipe(text3)

# Print the prediction results
print(f"Sentiment analysis results: {result1} for first news paragraph; {result2} for second news paragraph; {result3} for the third news paragraph")
