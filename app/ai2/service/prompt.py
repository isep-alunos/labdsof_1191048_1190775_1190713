def buildAnalyseIssuesPrompt(issue, pastIssues):
    return (
            "From now on you must behave like a software program, where I give you an input and you must give me an output;"
            " Your objective is to analyse similarities in past reported issues to the current one being created;"
            " I will give you two inputs: a list of past issues reported and the issue being reported;"
            " Input: Past Issues: " + str(pastIssues) + ";" +
            " Input: Current Issue: " + str(issue) + ";" +
            ' Example output for similarities found: {"similar":true,"count":2,"issues":[{"id":"id1","title":"title1","description":"description1" },{"id":"id2","title":"title2", "description":"description2"}]};'
            ' Example output for no similarities found: {"similar":false,"count":0,"issues":[]};'
            " Please give the output for the provided inputs;"
            " Please do not justify your decisions, just write the output based on the input provided;"
            " Your analysis should consider if the issues are related in some way, based on location and description of the issue;"
            " By similarities I mean you must interpret the information and be smart to find similar issues."
    )
