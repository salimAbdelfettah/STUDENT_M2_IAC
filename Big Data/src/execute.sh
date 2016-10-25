clear
local_dir=`pwd`
export my_dir=$local_dir/hadoop-1.2.1
export my_bash_dir=$local_dir/scripts
export my_jars_dir=$local_dir/jars
export my_fsd=RDFStats
#sh compil.sh
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo "                   Statistiques RDF"
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo @@ Lancement de la procédure clean_fs_output
sh $my_bash_dir/clean_fs_output.sh
echo @@ Procédure clean_fs_output terimnée
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
sh $my_bash_dir/run_P1.sh
echo ----------+----------+----------+----------+----------
read -p 'Pressez une touche pour lancer le deuxième programme'
sh $my_bash_dir/run_P2.sh
echo ----------+----------+----------+----------+----------
read -p 'Pressez une touche pour lancer le troisième programme'
sh $my_bash_dir/run_P3.sh
echo ----------+----------+----------+----------+----------
read -p 'Pressez une touche pour lancer le quatrième programme'
sh $my_bash_dir/run_P4.sh
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
