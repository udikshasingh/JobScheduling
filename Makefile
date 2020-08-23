JFLAGS = -g
JC = javac
JVM= java
.SUFFIXES: .java .class
.java.class: ; $(JC) $(JFLAGS) $*.java
CLASSES = \
	     risingCity.java \
	     MinHeap.java \
	     Node.java  \
	     RedBlacktree.java \

MAIN = risingCity

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean: $(RM) *.class

	     
