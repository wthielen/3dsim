.SUFFIXES: .java .class

PACKAGE = sim3d
BINDIR = ../bin
FILES = \
		${PACKAGE}/World.java \
		${PACKAGE}/BaseObject.java \
		${PACKAGE}/Block.java

CLASSPATH = .:/usr/share/java/vecmath.jar:/usr/share/java/commons-collections3.jar

classes: $(FILES:.java=.class)

default: classes

.java.class:
	javac -cp ${CLASSPATH} -d ${BINDIR} $*.java

clean:
	/bin/rm -rf ${BINDIR}/${PACKAGE}
