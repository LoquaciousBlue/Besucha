##To Merge:

Merge master into the parent of the dev branch in question (I.e. if you're working in DEV_SomeBranch, merge main into SomeBranch).
Then merge the parent into DEV; this will allow you to take care of merge conflicts in the proper space.

Then, once conflicts are fixed, create a pull request and get cleared by the other contributers. Then, from DEV merge to parent, and finally to master (if the goal is to merge that far).

Making sure to do this will reduce the amount of patching/conflict handling across all edits.