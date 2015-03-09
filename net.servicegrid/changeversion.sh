echo $1
java -cp target/classes ListupModules | xargs sed -i "" s/1\.0-SNAPSHOT/$1/g

