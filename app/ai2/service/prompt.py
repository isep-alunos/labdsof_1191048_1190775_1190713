def buildAnalyseIssuesPrompt(issue, pastIssues):
    return (
            "From now on you must behave like a software program, where I give you an input and you must give me an output;"
            " Your objective is to analyse similarities in past reported issues to the current one being created;"
            " By similarities I mean you must interpret the information and be smart to find similar issues."
            " I will give you two inputs: a list of past issues reported and the issue being reported;"
            " Input: Past Issues: " + str(pastIssues) + ";" +
            " Input: Current Issue: " + str(issue) + ";" +
            ' The output format should be a string with issues ID separated by comma; Example output: uuid_1,uuid_2 ;'
            " In case no result is found you should return empty string"
            " Please give the output for the provided inputs;"
            " Please do not justify your decisions, just write the output based on the input provided;"
            " Your analysis should consider if the issues are related in some way, based on location and description of the issue;"
    )
