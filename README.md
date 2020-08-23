# JobScheduling

## Problem description 

Wayne Enterprises is developing a new city. They are constructing many buildings and plan to use software to keep track of all buildings under construction in this new city. A building record has the following fields: 

 **buildingNum**: unique integer identifier for each building.

 **executed_time**: total number of days spent so far on this building.

 **total_time**: the total number of days needed to complete the construction of the building.

The needed operations are:

1. Print (buildingNum) prints the triplet buildingNume,executed_time,total_time.

2. Print (buildingNum1, buildingNum2) prints all triplets bn, executed_tims, total_time for which buildingNum1 <= bn <= buildingNum2.

Wayne Construction works on one building at a time. When it is time to select a building to work on, the building with the lowest executed_time (ties are broken by selecting the building with the lowest buildingNum) is selected. The selected building is worked on until complete or for 5 days, whichever happens first. If the building completes during this period its number and day of completion is output and it is removed from the data structures. Otherwise, the building’s executed_time is updated. In both cases, Wayne Construction selects the next building to work on using the selection rule. When no building remains, the completion date of the new city is output.

## Input Format

Input test data will be given in the following format:

Insert(buildingNum, 

total_time)Print(buildingNum)

Print (buildingNum1, buildingNum2)

## Output Format

Insert(buildingNum ,total_time) should produce no output unless buildingNum is a duplicate in which case you should output an error and stop. 

PrintBuilding (buildingNum) will output the (buildingNum,executed_time,total_time) triplet if the buildingNum exists. If not print (0,0,0).

PrintBuilding (buildingNum1, buildingNum2) will output all (buildingNum,executed_time,total_time) triplets separated by commas in a single line including buildingNum1 and buildingNum2; if they exist. If there is no building in the specified range, output (0,0,0). You should not print an additional comma at the end of the line. 

Other output includes completion date of each building and completion date of city.

All output should go to a file named “output_file.txt”.
