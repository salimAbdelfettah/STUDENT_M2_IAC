echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -ls $my_fsd/MR4/MR41/output
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -cat $my_fsd/MR4/MR41/output/part-*
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -ls $my_fsd/MR4/MR42/output
echo ----------+----------+----------+----------+----------
$my_dir/bin/hadoop dfs -cat $my_fsd/MR4/MR42/output/part-*
echo ----------+----------+----------+----------+----------
