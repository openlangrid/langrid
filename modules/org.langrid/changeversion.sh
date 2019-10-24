echo $1
java -cp target/classes langrid.ListupModules | xargs sed -i "" s/1\.1-SNAPSHOT/$1/g

