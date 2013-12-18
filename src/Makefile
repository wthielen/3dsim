.SUFFIXES: .java .class

PACKAGE = sim3d
FILES = \
		${PACKAGE}/World.java \
		${PACKAGE}/BaseObject.java \
		${PACKAGE}/Block.java

CLASSPATH = .:/usr/share/java/vecmath.jar

classes: $(FILES:.java=.class)

default: classes

.java.class:
	javac -cp ${CLASSPATH} -d ../bin $*.java

clean:
	/bin/rm -rf ../bin/${PACKAGE}
