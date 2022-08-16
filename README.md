# TSP with AI Iterative Deepening Search algorithm

Algorithms and Data Structures assignments:  Traveling Salesperson Problem.

This code is posted with instructor's permission and adheres to Metropolitan State University's policy.

DelivB:  Finds the shortest bitonic tour of the cities from westernmost city to easternmost city and back to westernmost city.  The “val” field of the test file lists a floating point number that indicates either the latitude or longitude of the city. The output is the distance of the shortest bitonic tour, and the order of cities in the tour.

DelivC:  Depth-first search with iterative deepening finds the nearest goal city (val = "G") from the starting city (val = “S”). Depth bound starts at 1 and increases by 1 each time through the graph. Prints results for each depth bound that causes a change over the previous depth bound. 

DelivD:  Uses AI Iterative Deepening Search algorithm to find the best possible Hamiltonian Cycle of a given graph in the test files, from specified start "S" to goal "G" cities. Prints the initial path, and every path found that is better than all previous paths.  Creates edges between cities where none exists. AI Iterative Deepening Search algorithm was converted to Java from procedural code found in "Artificial Intelligence" by Poole & Mackworth,  Sections 4.7 – 4.9.
