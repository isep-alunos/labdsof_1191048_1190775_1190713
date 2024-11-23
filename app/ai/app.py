from flask import Flask, jsonify, make_response

import logging

from service.ai import getResponse

logging.basicConfig(level=logging.DEBUG)

app = Flask(__name__)


# /products/<sku>

@app.route('/chat/<msg>', methods=['GET'])
def chat(msg):
    try:
        return jsonify(getResponse(msg))
    except Exception as e:
        return make_response(jsonify(e.args[0]), 404)


app.run(port=8081, host='0.0.0.0', debug=True)
