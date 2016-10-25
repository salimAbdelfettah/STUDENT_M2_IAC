sh $my_bash_dir/copy_p1_to_p2.sh
echo ----------+----------+----------+----------+----------
echo '@@ Lancement du programme 2'
sh $my_bash_dir/jarMR2.sh
echo '@@ Programme 2 terminé'
echo ----------+----------+----------+----------+----------
echo '@@ Affichage des résultats (Programme 2)'
sh $my_bash_dir/show_dfs_output_MR2.sh
