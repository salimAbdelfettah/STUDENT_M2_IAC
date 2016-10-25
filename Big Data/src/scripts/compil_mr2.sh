echo ----------+----------+----------+----------+----------
echo ...
javac -classpath $my_dir/hadoop-core-1.2.1.jar $my_dir/rdf_stats/MR2/MR2.java
echo ----------+----------+----------+----------+----------
echo ...
jar -cvf $my_jars_dir/MR2.jar -C $my_dir/rdf_stats/MR2/ .
echo ----------+----------+----------+----------+----------
