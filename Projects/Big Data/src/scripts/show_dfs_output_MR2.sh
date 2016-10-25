echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -ls $my_fsd/MR2/output
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -cat $my_fsd/MR2/output/part-*
echo ----------+----------+----------+----------+----------
