# Aldrich-Drescher-Kingsmith-F20
Senior Project Repository

This repository is CPSC498 and CPSC499 (Senior Project I and II).

The only authorized users are Professor Ewa Syta, Edwin Aldrich, Logan Drescher and Bettina King-Smith.

If you are not one of these people, you are not permitted to view, copy, or change any of the files in this repository.

The rules regarding academic honesty as outlined in the Student Handbook apply to ALL files in this repository.

You must check in ALL files related to your project into the repository. At any point in time, the state of the repository should reflect the current state of your project. This means that you should be checking in changes as you make them.

Use the ‘code’ folder for your source code and related files. Use the ‘writing’ folder for any written material produced for the project (the proposal, specs, final report, etc.).

## To Use
Our final project can be found in the `final_project/` folder. Run the program by clicking on the `BESUCHA.jar` executable. Provide the application with the sample files in order to see the desired results. 

Sample files in the `final_project\` folder are as follows:

- `RandomSections.xlsx`and `RandomPreferences.xlsx` should be used together. They simulate a large number of students, sections, and preferences.
- `SmallNumSections.xlsx` and `SmallNumPreferences.xlsx` should be used together. They simulate an extremely small number of students, sections, and preferences.

## Make an executable

Go to `backend/` folder. There will be a `makefile`. 

1. To make sure that everything is correctly configured before creating the executable, run `make checks` from terminal. This runs several different maven commands, like compiling the code and running unit tests. If you see a line like `BUILD SUCCESS` printed out to the terminal, this is a good sign that everything is working properly.
2. If you receive an `ERROR` screen instead of `BUILD SUCCESS`, the following commands might be helpful when trying to identify the problem:

```
 make validate
 make compile
 make clean
 make test
 ```
 Also see the following link: https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
 
 3. If you receive the `BUILD SUCCESS` message, run `make package` from the terminal. This will create the jar file, which can be found in the `target\` folder. The executable can be run in a point and click fashion.
