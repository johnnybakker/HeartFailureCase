from flask import Flask, jsonify, request
import joblib
import pandas as pd

scaler = joblib.load('out/scaler.pkl')
model = joblib.load('out/trained_model.pkl')
app = Flask(__name__)

# Define a sample endpoint
@app.route('/api/predict', methods=['POST'])
def predict():
	
	# Get the JSON data from the request
	data = [request.get_json()]
	df = pd.DataFrame(data)
	df = scaler.transform(df)
	prediction = model.predict(df)
	result = int(prediction[0])

	return jsonify(message =result)

if __name__ == '__main__':
	app.run(debug=True)

# Case 1 | result 1
# {
#   "Age": 43,
#   "RestingBP": 115,
#   "Cholesterol": 0,
#   "FastingBS": 0,
#   "MaxHR": 145,
#   "ExerciseAngina": 1,
#   "Oldpeak": 2.0,
#   "Male": 1,
#   "Female": 0,
#   "ChestPainType_ASY": 1,
#   "ChestPainType_ATA": 0,
#   "ChestPainType_NAP": 0,
#   "ChestPainType_TA": 0,
#   "HasCholesterol": 0,
#   "RestingECG_Normal": 1,
#   "RestingECG_ST": 0,
#   "RestingECG_LVH": 0,
#   "ST_Slope_Down": 0,
#   "ST_Slope_Flat": 1,
#   "ST_Slope_Up": 0
# }

# Case 2 | result 0
# {
#   "Age": 53,
#   "RestingBP": 155,
#   "Cholesterol": 175,
#   "FastingBS": 1,
#   "MaxHR": 160,
#   "ExerciseAngina": 0,
#   "Oldpeak": 0.3,
#   "Male": 1,
#   "Female": 0,
#   "ChestPainType_ASY": 0,
#   "ChestPainType_ATA": 0,
#   "ChestPainType_NAP": 1,
#   "ChestPainType_TA": 0,
#   "HasCholesterol": 1,
#   "RestingECG_Normal": 0,
#   "RestingECG_ST": 1,
#   "RestingECG_LVH": 0,
#   "ST_Slope_Down": 0,
#   "ST_Slope_Flat": 0,
#   "ST_Slope_Up": 1
# }