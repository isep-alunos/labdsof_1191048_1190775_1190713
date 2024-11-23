from flask import Flask, jsonify, request, make_response
from itertools import islice

import logging

from service.ai import askAI

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
        response = askAI(
            'From now on you must behave like a software program, where I give you an input and you must give me an output'
            + '; Your objective is to analyse similarities in past reported issues to the current one being created'
            + '; I will give you two inputs: a list of past issues reported and a the issue being reported'
            + '; Input: Past Issues: ' + str(past_issues)
            + '; Input: Current Issue: ' + str(current_issue)
            + '; Example output for similarities found: {"similar":true,"count":2,"issues":[{"id":"id1","title":"title1","description":"description1" },{"id":"id2","title":"title2", "description":"description2"}]}'
            + '; Example output for no similarities found: {"similar":false,"count":0,"issues":[]}'
            + '; Please give the output for the provided inputs'
            + '; Please do not justify your decisions, just write the output based on the input provided'
            + '; Your analysis should consider if the issues are related in some way, based on location and description of the issue'
            + '; By similarities I mean you must interpret the information and be smart to find similar issues'

            )
        return jsonify(response)
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)


app.run(port=8081, host='0.0.0.0', debug=True)
