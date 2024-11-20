import os
from together import Together

client = Together()


def getResponse(msg):
    response = client.chat.completions.create(
        model="meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo",
        messages=[{"role": "user", "content": msg}],
        stream=True,
    )

    # Extract the result from the response
    result = ""
    for chunk in response:
        result += chunk.choices[0].delta.content or ""

    # Return the result as a JSON response
    return {"response": result}
