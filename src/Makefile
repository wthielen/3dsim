PACKAGE = sim3d
BINDIR = ../bin

CLASSPATH = .:/usr/share/java/vecmath.jar:/usr/share/java/commons-collections3.jar

${PACKAGE}/%.class: ${PACKAGE}/%.java
	javac -cp ${CLASSPATH} -d ${BINDIR} $(addsuffix .java, $(basename $@))

default: $(addsuffix .class, $(basename $(wildcard ${PACKAGE}/*.java)))

clean:
	/bin/rm -rf ${BINDIR}/${PACKAGE}
