echo ----------+----------+----------+----------+----------
echo '@@ Lancement du programme 3'
sh $my_bash_dir/jarMR31.sh
echo ----------+----------+----------+----------+----------
sh $my_bash_dir/copy_p31_to_p32.sh
echo ----------+----------+----------+----------+----------
sh $my_bash_dir/jarMR32.sh
echo ----------+----------+----------+----------+----------
echo '@@ Programme 3 terminé'
echo ----------+----------+----------+----------+----------
echo '@@ Affichage des résultats (Programme 3)'
sh $my_bash_dir/show_dfs_output_MR3.sh
