gradle clean
gradle distZip
rm ww.html
open build/distributions/winston_wolfe.zip
sleep 1
./build/distributions/winston_wolfe/bin/winston_wolfe src/examples/environments/dev.yaml src/examples/US4274/Unavailable/TestCase03_SQ2.yaml > ww.html
open ww.html
