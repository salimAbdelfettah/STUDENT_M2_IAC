echo ...
local_dir=`pwd`
export my_dir=$local_dir/hadoop-1.2.1
$my_dir/bin/hadoop namenode -format
$my_dir/bin/stop-all.sh
$my_dir/bin/start-all.sh
