echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -ls $my_fsd/MR3/MR31/output
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -cat $my_fsd/MR3/MR31/output/part-*
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -ls $my_fsd/MR3/MR32/output
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -cat $my_fsd/MR3/MR32/output/part-*
echo ----------+----------+----------+----------+----------
