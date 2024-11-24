import os
from together import Together

from service.prompt import buildAnalyseIssuesPrompt

client = Together()


def askAI(msg):
    response = client.chat.completions.create(
        model="meta-llama/Meta-Llama-3.1-70B-Instruct-Turbo",
        messages=[{"role": "user", "content": msg}],
        stream=True,
    )

    # Extract the result from the response
    result = ""
    for chunk in response:
        result += chunk.choices[0].delta.content or ""

    # Return the result as a JSON response
    return result

def analyseIssues(issue, pastIssues):
    prompt = buildAnalyseIssuesPrompt(issue, pastIssues)
    return askAI(prompt)
