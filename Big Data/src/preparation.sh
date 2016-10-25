clear
local_dir=`pwd`
export my_dir=$local_dir/hadoop-1.2.1
export my_bash_dir=$local_dir/scripts
export my_jars_dir=$local_dir/jars
export my_fsd=RDFStats
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo "      Statistiques RDF / Préparation"
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
sh $my_bash_dir/h_start.sh
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
sh $my_bash_dir/mkdir.sh
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
sh replace.sh
