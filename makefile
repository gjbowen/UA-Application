
# define a makefile variable for the java & compiler
JCC = javac
JR  = java
rm  = rm -rf

default: Driver.class


# Compile java files
Driver.class:
	$(JCC) -d "./bin" -cp .:jars/* ./src/*.java ./src/public_package/*.java ./src/concur_package/*.java ./src/ar_package/*.java ./src/git_package/*.java  ./src/file_fetch_package/*.java 










# Run
#
run:
	$(JR) -cp .:/jars/* bin/ Driver	

# Removes all .class files, so that the next make rebuilds them
#
clean: 
	$(RM) bin/*.class
	$(RM) src/*.class
