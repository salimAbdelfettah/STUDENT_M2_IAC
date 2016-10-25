echo ----------+----------+----------+----------+----------
echo '@@ Lancement du programme 4'
sh $my_bash_dir/jarMR41.sh
echo ----------+----------+----------+----------+----------
sh $my_bash_dir/copy_p41_to_p42.sh
echo ----------+----------+----------+----------+----------
sh $my_bash_dir/jarMR42.sh
echo ----------+----------+----------+----------+----------
echo '@@ Programme 4 terminé'
echo ----------+----------+----------+----------+----------
echo '@@ Affichage des résultats (Programme 4)'
sh $my_bash_dir/show_dfs_output_MR4.sh
