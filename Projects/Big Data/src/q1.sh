clear
local_dir=`pwd`
export my_dir=$local_dir/hadoop-1.2.1
export my_bash_dir=$local_dir/scripts
export my_jars_dir=$local_dir/jars
export my_fsd=RDFStats
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo "            Statistiques RDF - Programme 1"
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo ...
$my_dir/bin/hadoop dfs -rmr $my_fsd/MR1/output
echo ...
sh $my_bash_dir/run_P1.sh
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
