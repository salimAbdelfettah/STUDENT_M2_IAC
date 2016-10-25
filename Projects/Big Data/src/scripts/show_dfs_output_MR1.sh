echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -ls $my_fsd/MR1/output
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -cat $my_fsd/MR1/output/part-*
echo ----------+----------+----------+----------+----------
