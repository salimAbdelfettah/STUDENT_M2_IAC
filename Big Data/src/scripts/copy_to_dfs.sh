echo ----------+----------+----------+----------+----------
echo ...
$my_dir/bin/hadoop dfs -copyFromLocal $my_dir/input/* $my_fsd/MR1/input
echo ----------+----------+----------+----------+----------
echo ...
$my_dir/bin/hadoop dfs -copyFromLocal $my_dir/input/* $my_fsd/MR3/MR31/input
echo ----------+----------+----------+----------+----------
echo ...
$my_dir/bin/hadoop dfs -copyFromLocal $my_dir/input/* $my_fsd/MR4/MR41/input
echo ----------+----------+----------+----------+----------
