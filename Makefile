DFileSubclasses: DFile
	javac delta/database/DStudent.java
	javac delta/database/DTrial.java
	javac delta/database/DUser.java

DFile:
	javac delta/database/DFile.java

clean:
	rm -f delta/database/*.class

