from flask import Flask, jsonify, request, make_response
from itertools import islice

import logging

from service.ai import askAI, clarification
from service.ai import analyseIssues

logging.basicConfig(level=logging.DEBUG)

app = Flask(__name__)


@app.route('/chat/<msg>', methods=['GET'])
def chat(msg):
    try:
        return jsonify({"response": askAI(msg)})
    except Exception as e:
        return make_response(jsonify(e.args[0]), 404)


@app.route('/analyze_issues', methods=['POST'])
def analyze_issues():
    try:
        data = request.get_json()
        current_issue = data[next(iter(data))]
        past_issues = next(islice(data.items(), 1, 2))
        result = analyseIssues(current_issue, past_issues)
        print(result)
        return result
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)



@app.route('/generate_clarification', methods=['POST'])
def generate_clarification():
    try:
        data = request.get_json()
        current_issue = data[next(iter(data))]
        result = clarification(current_issue)
        print(result)
        return result
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)


app.run(port=8082, host='0.0.0.0', debug=True)
