.PHONY: ui api t api-t install all

all: ui api

ui:
	mvn test -pl ui -am ``-Dheadless=true``

api:
	mvn test -pl api -am

t:
	mvn test -pl ui -am ``-Dtest=$(T)`` ``-Dheadless=true``

api-t:
	mvn test -pl api -am ``-Dtest=$(T)``

install:
	mvn install -DskipTests
