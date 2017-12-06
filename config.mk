JCOMPILE := javac
JAVA := java
JSOURCE := Main AppController AppView \
			LoginView InputCheck MyConnection \
			DriverNotFoundException ConnectionErrorException ElevDataModel \
			ClasaDataModel ProfilDataModel ProfilTableModel \
			MaterieDataModel MaterieTableModel

MAIN := Main

ifndef ORACLE_HOME
	$(error ORACLE_HOME is not set)
endif

JAR_LIB := ${ORACLE_HOME}/jdbc/lib/ojdbc6_g.jar
CAL_LIB := lib/jcalendar-1.4.jar

CLASSPATH := .:${JAR_LIB}:${CAL_LIB}
CLASSES_DIR := class
SOURCES_DIR := src

OPTIONS := -g -Werror -Xlint:all -classpath ${CLASSPATH} -d ${CLASSES_DIR}

#SOURCES1 := $(patsubst %, ${SOURCES_DIR}/%.java, ${JSOURCE})
SOURCES2 := $(JSOURCE:%=${SOURCES_DIR}/%.java)

## -Werror -> se termina compilarea cand da de vreo eroare 
##
##
##
