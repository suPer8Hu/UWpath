.DEFAULT_GOAL := runBDTests

# Compile all Java files in the directory
compile:
	javac -cp .:../junit5.jar Backend.java BackendInterface.java BaseGraph.java DijkstraGraph.java GraphADT.java PlaceholderGraph.java PlaceholderMap.java ShortestPathInterface.java BackendDeveloperTests.java FrontendInterface.java MapADT.java

# Run backend developer tests
runBDTests: compile
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

# Clean up compiled files
clean:
	rm -f *.class

.PHONY: clean compile runBDTests
