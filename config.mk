JCOMPILE := javac
JAVA := java
JSOURCE := Conexiune  DataModel
MAIN := Main

ifndef ORACLE_HOME
	$(error ORACLE_HOME is not set)
endif

JAR_LIB := ${ORACLE_HOME}/jdbc/lib/ojdbc5.jar

CLASSPATH := .:${JAR_LIB}
CLASSES_DIR := class
SOURCES_DIR := src

OPTIONS := -g -Werror -Xlint:all -classpath ${CLASSPATH} -d ${CLASSES_DIR}

#SOURCES1 := $(patsubst %, ${SOURCES_DIR}/%.java, ${JSOURCE})
SOURCES2 := $(JSOURCE:%=${SOURCES_DIR}/%.java)

## -Werror -> se termina compilarea cand da de vreo eroare 
##
##
##
