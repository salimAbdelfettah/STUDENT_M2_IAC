echo ----------+----------+----------+----------+----------
echo ...
javac -classpath $my_dir/hadoop-core-1.2.1.jar $my_dir/rdf_stats/MR1/MR1.java
echo ----------+----------+----------+----------+----------
echo ...
jar -cvf $my_jars_dir/MR1.jar -C $my_dir/rdf_stats/MR1/ .
echo ----------+----------+----------+----------+----------
