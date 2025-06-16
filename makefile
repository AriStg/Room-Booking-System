Run:
	mkdir -p bin
	javac -cp "lib/jdatepicker-1.3.4.jar:bin" -d bin src/*/*.java -Xlint:unchecked
	java -cp "lib/jdatepicker-1.3.4.jar:bin" mainFolder.MainClass
	rm -r bin