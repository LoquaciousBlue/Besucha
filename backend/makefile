build:
	mvn compile
	mvn package
	-chmod +x ./target/*.jar
	clear && echo "build finished"

checks:
	mvn validate
	mvn clean
	mvn compile
	mvn test

package:
	mvn package

validate:
	mvn validate

compile:
	mvn compile

clean:
	mvn clean

test:
	mvn test


