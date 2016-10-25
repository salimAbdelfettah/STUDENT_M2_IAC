clear
local_dir=`pwd`
export my_dir=$local_dir/hadoop-1.2.1
export my_bash_dir=$local_dir/scripts
export my_jars=$local_dir/jars
export my_fsd=RDFStats
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo "      Statistiques RDF / Remplacement du input"
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo @@ Lancement de la procédure clean_input
sh $my_bash_dir/clean_input.sh
echo @@ Procédure clean_input terminée
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo @@ Lancement de la procédure clean_fs_input
sh $my_bash_dir/clean_fs_input.sh
echo @@ Procédure clean_fs_input terimnée
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo @@ Lancement de la procédure copy_to_dir
sh $my_bash_dir/copy_to_dir.sh
echo @@ Procédure copy_to_dir terminée
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
echo @@ Lancement de la procédure copy_to_dfs
sh $my_bash_dir/copy_to_dfs.sh
sh $my_bash_dir/show_dfs_input.sh
echo @@ Procédure copy_to_dfs terimnée
echo ++++++++++-++++++++++-++++++++++-++++++++++-++++++++++
