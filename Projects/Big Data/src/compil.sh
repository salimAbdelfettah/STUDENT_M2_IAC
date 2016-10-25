clear
local_dir=`pwd`
export my_dir=$local_dir/hadoop-1.2.1
export my_bash_dir=$local_dir/scripts
export my_jars_dir=$local_dir/jars
export my_fsd=RDFStats
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo "          Statistiques RDF / Compilation"
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo '@@ Compilation du programme 1'
sh $my_bash_dir/compil_mr1.sh
echo '@@ Compilation du programme 1 terminée'
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo '@@ Compilation du programme 2'
sh $my_bash_dir/compil_mr2.sh
echo '@@ Compilation du programme 2 terminée'
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo '@@ Compilation du programme 3'
sh $my_bash_dir/compil_mr3.sh
echo '@@ Compilation du programme 3 terminée'
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo '@@ Compilation du programme 4'
sh $my_bash_dir/compil_mr4.sh
echo '@@ Compilation du programme 4 terminée'
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
