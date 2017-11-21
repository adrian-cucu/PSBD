include config.mk

## default target va fi selectat ruland comanda 'make'
default: all 

all:
	${JCOMPILE} ${OPTIONS} ${SOURCES2}

run:
	@${JAVA} -classpath ${CLASSES_DIR}:${CLASSPATH} ${MAIN}

clean:
	rm -I ${CLASSES_DIR}/*.class 	
	@echo Clean!
