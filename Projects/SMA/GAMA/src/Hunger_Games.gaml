/**
 *  HungerGamesZI
 *  Author: A.S.
 *  Description: 
 */

model Hunger_Games

/* Insert your model definition here */

global{
	//int p1 <- 4;
	//int p2 <- 0;
	// nombre de participants vivants
	int nb_armes <- 0;
	int nb_armes_prises <- 0;
	int nb_soins <- 0;
	int nb_soins_pris <- 0;
	int nb_nourritures <- 0;
	int nb_nourritures_manges <- 0;
	int size_env <- 200;
	int nb_districts <- 12;
	int nb_vivants <- nb_districts*2;
	int nb_m_type_0_faim <- 0;
	int nb_m_type_0_tir <- 0;
	int nb_m_type_1_faim <- 0;
	int nb_m_type_1_tir <- 0;
	int nb_m_type_2_faim <- 0;
	int nb_m_type_2_tir <- 0;
	int nb_m_type_3_faim <- 0;
	int nb_m_type_3_tir <- 0;
	int nb_m_type_4_faim <- 0;
	int nb_m_type_4_tir <- 0;
	int nb_m_faim <- 0;
	int nb_m_tir <- 0;
	int dist_vu <- 50;
	int dist <- 10;
	int nb_coop <- 0;
	int nb_tirs <- 0;
	int nb_tirs_reussis <- 0;
	int default_impact <- 1;
	
	// initialisation
	init{
		// création de 12 participants et 12 participantes (au début 13*2)
		loop i from: 1 to: nb_districts{
			//
			create participant number:1{
				my_district <- i;
				male <- true;
				switch(my_district){
					match 1 {my_color <- rgb("red");}
					match 2 {my_color <- rgb("yellow");}
					match 3 {my_color <- rgb("green");}
					match 4 {my_color <- rgb("blue");}
					match 5 {my_color <- rgb("black");}
					match 6 {my_color <- rgb("gray");}
					match 7 {my_color <- rgb("orange");}
					match 8 {my_color <- rgb("pink");}
					match 9 {my_color <- rgb("cyan");}
					match 10 {my_color <- rgb("magenta");}
					match 11 {my_color <- rgb("white");}
					match 12 {my_color <- rgb("darkGray");}
					match 13 {my_color <- rgb("lightGray");}
				}
				type_p <- rnd(4);
				//type_p <- p1;
				switch(type_p){
					match_one [0, 3] {peut_coop <- false; mode_coop <- false;}
					match_one [1, 2, 4] {peut_coop <- true; mode_coop <- true;}
				}
			}
			//
			create participant number:1{
				my_district <- i;
				male <- false;
				my_color <- (my_district=1) ? rgb("red") : rgb("green");
				type_p <- rnd(4);
				//type_p <- p2;
				switch(my_district){
					match 1 {my_color <- rgb("red");}
					match 2 {my_color <- rgb("yellow");}
					match 3 {my_color <- rgb("green");}
					match 4 {my_color <- rgb("blue");}
					match 5 {my_color <- rgb("black");}
					match 6 {my_color <- rgb("gray");}
					match 7 {my_color <- rgb("orange");}
					match 8 {my_color <- rgb("pink");}
					match 9 {my_color <- rgb("cyan");}
					match 10 {my_color <- rgb("magenta");}
					match 11 {my_color <- rgb("white");}
					match 12 {my_color <- rgb("darkGray");}
					match 13 {my_color <- rgb("lightGray");}
				}
				switch(type_p){
					match_one [0, 3] {peut_coop <- false; mode_coop <- false;}
					match_one [1, 2, 4] {peut_coop <- true; mode_coop <- true;}
				}
			}
		}
		//création du controleur
		create controleur number:1{
			proba_armes <- 0.05;
			proba_soins <- 0.05;
			proba_nourritures <- 0.05;
		}
		save [size_env, nb_districts] type:"csv" to : "result_env.csv";
	}
	//
	reflex write_results {
		//write("--- "+nb_coop+" ---");
		save [step, nb_m_faim, nb_m_tir] type:"csv" to : "result_morts.csv";
		save [step, nb_armes, nb_armes_prises] type:"csv" to : "result_armes.csv";
		save [step, nb_soins, nb_soins_pris] type:"csv" to : "result_soins.csv";
		save [step, nb_nourritures, nb_nourritures_manges] type:"csv" to : "result_nourritures.csv";
		save [step, nb_tirs, nb_tirs_reussis] type:"csv" to : "result_tirs.csv";
	}
	//
	reflex stop_simulation when: nb_vivants <= 1 {
		do halt ;
	} 
}

environment bounds:size_env{
	
}

entities{
	// <--- Participant (Définition) --->
	species participant{
		// 0 --> ZI ; 1 --> Prudent ; 2 --> Peureux ; 3 --> Agréssif ; 4 --> Intélligent
		float my_default_precision <- (80+rnd(20))/100;
		bool mode_coop;
		bool peut_coop;
		int type_p;
		int energie <- 1000;
		int my_district;
		rgb my_color;
		bool male;
		bool chef <- false;
		int points <- 3;
		list<arme> mes_armes;
		soin mon_soin <- nil;
		list<participant> voisins;
		list<participant> cooperation;
		list<arme> armes;
		list<soin> soins;
		list<nourriture> nourritures;
		//
		reflex upd {
			if(energie<=0 or points<=0){
				nb_vivants <- nb_vivants -1;
				if(points<=0){
					nb_m_tir <- nb_m_tir +1;
					switch(type_p){
						match 0 {nb_m_type_0_tir <- nb_m_type_0_tir +1;}
						match 1 {nb_m_type_1_tir <- nb_m_type_1_tir +1;}
						match 2 {nb_m_type_2_tir <- nb_m_type_2_tir +1;}
						match 3 {nb_m_type_3_tir <- nb_m_type_3_tir +1;}
						match 4 {nb_m_type_4_tir <- nb_m_type_4_tir +1;}
					}
				}else{
					nb_m_faim <- nb_m_faim +1;
					switch(type_p){
						match 0 {nb_m_type_0_faim <- nb_m_type_0_faim +1;}
						match 1 {nb_m_type_1_faim <- nb_m_type_1_faim +1;}
						match 2 {nb_m_type_2_faim <- nb_m_type_2_faim +1;}
						match 3 {nb_m_type_3_faim <- nb_m_type_3_faim +1;}
						match 4 {nb_m_type_4_faim <- nb_m_type_4_faim +1;}
					}
				}
				if(chef){
					ask one_of (cooperation){
						do devient_chef;
					}
				}
				do die;
			}else{
				voisins  <- participant at_distance dist_vu;
				armes  <- arme at_distance dist - mes_armes;
				soins  <- soin at_distance dist - soins;
				nourritures  <- nourriture at_distance dist;
				let ma_liste type: list of: participant <- cooperation;
				let me type: participant <- self;
				cooperation <- cooperation inter voisins;
				ask (cooperation-self){
					do maj liste: ma_liste;
				}
				ask mon_soin{
					do maj_soin proprietaire: me;
				}
				ask mes_armes{
					do maj_arme proprietaire: me;
				}
			}
		}
		//
		reflex agir when: length(voisins-cooperation)>0 and nb_vivants>length(cooperation){
			if(!peut_coop or !mode_coop){
				let la_cible type: participant <- one_of (voisins-cooperation);
				do tirer cible: la_cible;
				ask (cooperation-self){
					do tirer cible: la_cible;
				}
			}else{
				if(type_p=2){
					do bouger;
				}else if(type_p=4 and length(cooperation)<=4){
					let candidat type: participant <- one_of (voisins-cooperation);
					let me type: participant <- self;
					ask candidat{
						do demande_coop emmeteur: me;
					}
				}
			}
		}
		//
		reflex seul when: length(cooperation)=0 and length(voisins)=0{
			mode_coop <- peut_coop;
		}
		//
		reflex eliminer_reste when: nb_vivants<=length(cooperation){
			peut_coop <- false;
			mode_coop <- false;
			cooperation <- nil;
			do tirer cible: one_of(voisins);
		}
		//
		reflex bouger_rester when: length(cooperation)=0 or chef{
			if((type_p!=2)and!(type_p=1 and length(voisins)=0)){
				do bouger;
			}else{
				energie <- energie -1;
			}
		}
		//
		reflex manger when: length(nourritures)>0{
			nb_nourritures_manges <- nb_nourritures_manges +1;
			let nourriture_a_manger type: nourriture <- one_of (nourritures);
			if(length(cooperation)=0){
				set location <- nourriture_a_manger.location;
				set energie <- energie + nourriture_a_manger.energie - 2;
			}else if(chef){
				set location <- nourriture_a_manger.location;
				set energie <- energie - 2;
				let energie_a_prendre type: int <- int(nourriture_a_manger.energie / length(cooperation))-1;
				ask (cooperation){
					do se_nourrire energie_recu: energie_a_prendre;
				}
			}
			ask nourriture_a_manger{
				do die;
			}
		}
		//
		//
		reflex prendre_arme when: length(armes)>0 and type_p!=2{
			let arme_a_prendre type: arme <- one_of (armes);
			if(length(mes_armes)<3){
				if(arme_a_prendre.quantite>0){
					nb_armes_prises <- nb_armes_prises +1;
					mes_armes <- mes_armes + arme_a_prendre;
				}
			}else{
				if(type_p=3){
					// détruit l'arme
					arme_a_prendre.quantite <- 0;
				}
			}
		}
		//
		reflex prendre_soins when: length(soins)>0{
			let soin_a_prendre type: soin <- one_of (soins);
			set location <- soin_a_prendre.location;
			set energie <- energie - 2;
			if(mon_soin=nil){
				nb_soins_pris <- nb_soins_pris +1;
				mon_soin <- soin_a_prendre;
			}else if(type_p=0){
				nb_soins_pris <- nb_soins_pris +1;
				mon_soin <- soin_a_prendre;
			}else{
				if(soin_a_prendre.quantite>mon_soin.quantite){
					if(type_p=3){
						//détruit le soin
						mon_soin.quantite <- 0;
					}
					mon_soin <- soin_a_prendre;
					nb_soins_pris <- nb_soins_pris +1;
				}else{
					if(type_p=3){
						soin_a_prendre.quantite <- 0;
					}
					
				}
			}
		}
		//
		reflex se_soigner when: points<3 and mon_soin!=nil{
			let diff type: int <- 3 - points;
			if(mon_soin.quantite>=diff){
				points <- 3;
				mon_soin.quantite <- mon_soin.quantite -diff; 
			}else{
				points <- points + mon_soin.quantite;
				mon_soin.quantite <- 0;
			}
			if(mon_soin.quantite=0){
				mon_soin <- nil;
			}
		}
		//
		action bouger{
			if(type_p!=4 or (type_p=4 and length(voisins)=0)){
				set location <- any_location_in (circle(dist));
			}
			energie <- energie -3;
			if(chef and energie> 0 and length(voisins)=0){
				let me type: participant <- self;
				ask cooperation{
					do suiver_moi le_chef: me;
				}
			}
		}
		//
		action demande_coop(participant emmeteur){
			let me type: participant <- self;
			if(peut_coop and mode_coop and length(cooperation)=0){
				ask emmeteur{
					do accepter_coop recepteur: me;
				}
			}else{
				ask emmeteur{
					do refuser_coop recepteur: me;
				}
			}
		}
		//
		action accepter_coop(participant recepteur){
			if(length(cooperation)=0){
				write("-- "+self+" --- "+recepteur);
				nb_coop <- nb_coop +1;
				chef <- true;
				cooperation <- cooperation + self;
			}
			cooperation <- cooperation + recepteur;
		}
		//
		action refuser_coop(participant recepteur){
			if(length(voisins - recepteur)<length(voisins)){
				do tirer cible: recepteur;
				ask (cooperation-self){
					do tirer cible: recepteur;
				}
			}
		}
		//
		action maj(list<participant> liste){
			cooperation <- liste;
		}
		//
		action tirer(participant cible){
			if(cible!=nil){
			let ma_precision <- my_default_precision;
			let mon_impact type: int <- default_impact;
			let distance type: float <- self distance_to cible;
			let b type: bool <- true;
			if(length(mes_armes)>0){
				if(type_p=0){
					let arme_a_utiliser type: arme <- one_of(mes_armes);
					if(arme_a_utiliser.distance+0.0>= distance){
						ma_precision <- arme_a_utiliser.precision;
						mon_impact <- arme_a_utiliser.impact;
					}else{
						set b <- false;
						nb_tirs <- nb_tirs +1;
					}
				}else{
					let arme_a_utiliser type: arme <- nil;
					let arme_temp type: arme <- one_of(mes_armes);
					let precision_temp type: float <- 0.0;
					let impact_temp type: int <- 0;
					loop i from: 0 to: length(mes_armes)-1{
						arme_temp <- mes_armes[i]; 
						if(arme_temp.distance+0.0>=distance){
							if(type_p=3){
								//choix sur l'impact
								if(arme_temp.impact>impact_temp){
									impact_temp <- arme_temp.impact;
									arme_a_utiliser <- arme_temp;
									precision_temp <- arme_temp.precision;
								}
							}else{
								//choix sur la precision
								if(arme_temp.precision>precision_temp){
									impact_temp <- arme_temp.impact;
									arme_a_utiliser <- arme_temp;
									precision_temp <- arme_temp.precision;
								}
							}
						}
					}
					if(arme_a_utiliser !=nil){
						ma_precision <- precision_temp;
						mon_impact <- impact_temp;
						arme_a_utiliser.quantite <- arme_a_utiliser.quantite -1;
						if(arme_a_utiliser.quantite=0){
							mes_armes <- mes_armes - arme_a_utiliser;
						}
					}else{
						set b <- false;
					}
				}
			}else{
				if(distance > 10.0){
					set b <- false;
				}
			}
			if(b){
				ask cible {
					do recevoir precision_tir: ma_precision impact_tir: mon_impact;
				}
			}
			}
		}
		//
		action recevoir(float precision_tir, int impact_tir){
			nb_tirs <- nb_tirs +1;
			mode_coop <- false;
			if(flip(precision_tir)){
				nb_tirs_reussis <- nb_tirs_reussis +1;
				points <- points - impact_tir;
			}
			if(points>0 and (type_p!= 0 and type_p!=2) and flip(0.5)){
				let la_cible type: participant <- one_of(voisins-cooperation);
				do tirer cible: la_cible;
			}
		}
		//
		action suiver_moi(participant le_chef){
			if(le_chef!=self){
				set location <- le_chef.location + {rnd(5), rnd(5)};
				energie <- energie -2 ;
			}
		}
		//
		action devient_chef{
			chef <- true;
		}
		//
		action se_nourrire(int energie_recu){
			energie <- energie +energie_recu;
		}
		// aspect
		aspect aspet_participant{
			draw male? square(10) : circle(5) color:my_color;
		}
	}
	// <--- Arme (Définition) --->
	species arme{
		float precision;
		int distance;
		int quantite;
		int impact;
		//
		action maj_arme(participant proprietaire){
			set location <- proprietaire.location;
		}
		//
		aspect aspet_arme{
			draw triangle(2) color:rgb("black");
		}
	}
	// <--- Soin (Définition) --->
	species soin{
		int quantite;
		//
		action maj_soin(participant proprietaire){
			set location <- proprietaire.location;
		}
		//
		aspect aspet_soin{
			draw square(2) color:rgb("red");
		}
	}
	// <--- Nourriture (Définition) --->
	species nourriture{
		int energie;
		aspect aspet_nourriture{
			draw circle(1) color:rgb("blue");
		}
	}
	// <--- Controleur (Définition) --->
	species controleur{
		float proba_armes;
		float proba_soins;
		float proba_nourritures;
		reflex gen{
			if(flip(proba_armes)){
				// création d'arme
				create arme number:1{
					nb_armes <- nb_armes +1;
					precision <- (rnd(50)+50)/100;
					let var <- rnd(5);
					distance <- rnd(5)*100;
					quantite <- (5-var)*10;
					impact <- rnd(2)+1;
				}
			}
			if(flip(proba_soins)){
				// création de soins
				create soin number:1{
					nb_soins <- nb_soins +1;
					quantite <- rnd(3);
				}
			}
			if(flip(proba_nourritures)){
				// création de sac de nourriture
				create nourriture number:1{
					nb_nourritures <- nb_nourritures +1;
					energie <- rnd(20)*10;
				}
			}
		}
	}
}

experiment Hunger_Games type:gui{
	output{
		
		display Interface{
			species participant aspect:aspet_participant;
			species arme aspect:aspet_arme;
			species soin aspect:aspet_soin;
			species nourriture aspect:aspet_nourriture; 
		}
		display Graphe_mort{
			chart "Nombre de morts" type: series background: rgb("white"){
			data "Faim" color: rgb("red") value: nb_m_faim;
			data "Tirs" color: rgb("blue") value: nb_m_tir;
			/*data "4 faim" color: rgb("red") value: nb_m_type_4_faim;
			data "4 tirs" color: rgb("blue") value: nb_m_type_4_tir;
			data "0 faim" color: rgb("magenta") value: nb_m_type_0_faim;
			data "0 tir" color: rgb("green") value: nb_m_type_0_tir;*/
			}
		}
		display Graphe_armes{
			chart "Nombre d'armes" type: series background: rgb("white"){
			data "générées" color: rgb("red") value: nb_armes;
			data "prises" color: rgb("blue") value: nb_armes_prises;
			}
		}
		monitor 'Nombre de vivants' value: nb_vivants;
		monitor 'Nombre de morts de faim' value: nb_m_faim;
		monitor 'Nombre de morts sur tirs' value: nb_m_tir;
		monitor 'Nombre de tirs' value: nb_tirs;
		monitor 'Nombre de tirs réussis' value: nb_tirs_reussis;
		monitor 'Nombre d\'armes' value: nb_armes;
		monitor 'Nombre d\'armes prises' value: nb_armes_prises;
		monitor 'Nombre de sacs de soins' value: nb_soins;
		monitor 'Nombre de sacs de soins pris' value: nb_soins_pris;
		monitor 'Nombre de sacs de nourritures' value: nb_nourritures;
		monitor 'Nombre de sacs de nourritures mangés' value: nb_nourritures_manges;
	}
}