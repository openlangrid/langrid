#!/bin/bash

ver=$1

svn mkdir https://svn.code.sf.net/p/servicegrid/code/mvn/$ver -m "prepare $ver"
for prj in `java -cp target/classes ListupProjects`
do
  svn cp https://svn.code.sf.net/p/servicegrid/code/service-grid-server/trunk/$prj https://svn.code.sf.net/p/servicegrid/code/mvn/$ver/$prj -m "prepare $ver"
done
