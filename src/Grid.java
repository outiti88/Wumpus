import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Grid {
	int taille;
	List<position> cells;
	int nbOfPits;
	List <position> PuitsPos ;
	position W;
	position G;
	position agent;
	int score;
	int fleche;
	Orientation sens;
	boolean gameOver;
	
	enum Orientation{
		EST,OUEST,SUD,NORD	
	}

	
	public Grid(int taille) {
		gameOver = false;
		score=0;
		this.taille= taille;
		initCells(this.taille);
		
		agent = new position(1,1);
		fleche=1;
		sens = Orientation.EST;
		//Allpositions : list of unique positions of Gold, Wumpus, and Pits (machi ism 3ala mosama ! )
		List<position> WGPPositions = generateWGPPositions(taille);
    	this.nbOfPits = WGPPositions.size()-2;
    	
		this.W = WGPPositions.get(0);
		this.G = WGPPositions.get(1);
		PuitsPos = new ArrayList<position>(WGPPositions.size()-2);
    	
		for(int i=2; i<WGPPositions.size();i++) {
			PuitsPos.add(WGPPositions.get(i));
    	}
    	
		this.cells = populateTheCells(WGPPositions);
		
	}
	

//Init all the cells ! 	
	public void initCells(int taille) {
		this.cells  = new ArrayList<position>(taille*taille);
		for(int i=1;i<=taille;i++) {
	    	for(int j=1; j<=taille;j++) {
	    		this.cells.add(new position(i, j));
	    	}
	    }
	}
	
// add "OK" to safe cells	
	public  List<position>  AddSafeandSort(List<position> cells){
		//add Ok to all other cells, with no harm
			for(position pos:cells){
				if(pos.percepts.isEmpty()) pos.percepts.add(Percept.OK);
			}	
			//Sort, makhasrin walo xD :2 :3 :p
			Collections.sort(cells, new SortGrid()); 
			return cells;
		
	}
	
	
//add Brise,lueur, puanteur.. 	
	public  List<position> populateTheCells(List<position> WGPPositions) {
		// in WGP positions, we have wumpus pos, then gold pos, then all pits
		
		// find Pits Adj cells, and add Brise to them		
		for(int i=2;i<WGPPositions.size();i++) {
				List <position> PitAdjCells = GetAdjCells(this.cells.get(this.cells.indexOf(WGPPositions.get(i))));
				position Puit = this.cells.get(this.cells.indexOf(WGPPositions.get(i)));
				Puit.percepts.add(Percept.Puit);
				for(position pos:PitAdjCells) {
					pos = this.cells.get(this.cells.indexOf(pos));
					pos.percepts.add(Percept.Brise);
				}
		}		
	
		
		// find Wumpus Adj cells, and add Puanteur to them
		position Wu = this.cells.get(this.cells.indexOf(WGPPositions.get(0)));
		Wu.percepts.add(Percept.Wumpus);
		List<position> WAdj =  GetAdjCells(Wu);
		for(position pos:WAdj) {
			pos = this.cells.get(this.cells.indexOf(pos));
			pos.percepts.add(Percept.Puanteur);
		}

		//Gold ..
		position Go = this.cells.get(this.cells.indexOf(WGPPositions.get(1)));
		Go.percepts.add(Percept.Lueur);

		
		AddSafeandSort(this.cells);
		
		return cells;
	}
	
	
	

	///*** Get Adj cells	***////
	
	public List<position> GetAdjCells(position pos){
		List<position> AdjCells = new ArrayList<position>(4);
	
		if(pos.x==1) { 
				if(pos.y==1) {//(1,1)
					position posAdj1 = new position(1,2); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
					position posAdj2 = new position(2,1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				}
				else if(pos.y==taille) {//(1,taille)
					position posAdj1 = new position(1,taille-1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
					position posAdj2 = new position(2,taille); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				}
				else {
					position posAdj1 = new position(1,pos.y-1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
					position posAdj2 = new position(2,pos.y); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
					position posAdj3 = new position(1,pos.y+1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj3)));
				} 
				
		}else if(pos.x==taille) {
				
				if(pos.y==1) {//(taille,1)
					position posAdj1 = new position(taille,2); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
					position posAdj2 = new position(taille-1,1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				}
				else if(pos.y==taille) {//(taille,taille)
					position posAdj1 = new position(taille,taille-1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
					position posAdj2 = new position(taille-1,taille); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				}
				else {
					position posAdj1 = new position(taille,pos.y-1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
					position posAdj2 = new position(taille-1,pos.y); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
					position posAdj3 = new position(taille,pos.y+1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj3)));
				} 	
		}else {
			if(pos.y==1) {
				position posAdj1 = new position(pos.x+1,1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
				position posAdj2 = new position(pos.x,2); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				position posAdj3 = new position(pos.x-1,1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj3)));
			}else if(pos.y==taille) {
				position posAdj1 = new position(pos.x+1,taille); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
				position posAdj2 = new position(pos.x,taille-1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				position posAdj3 = new position(pos.x-1,taille); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj3)));
				
			}else {
				position posAdj1 = new position(pos.x+1, pos.y); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj1)));
				position posAdj2 = new position(pos.x, pos.y+1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj2)));
				position posAdj3 = new position(pos.x-1, pos.y); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj3)));
				position posAdj4 = new position(pos.x, pos.y-1); AdjCells.add(this.cells.get(this.cells.indexOf(posAdj4)));	
			}

		}
		
		return AdjCells;
	}
	
	
	//genere les position des puits, gold, et wumpus
	public static  List<position> generateWGPPositions(int taille){
	    /* So the thing is, nombre de puit makhasch yfot taille, w khasna position dial wumpus w gold : on suppose khasna m unique positions
	     * ghadi  n generew ga3 les paires li momkin nsaybo taille*taille-1 (position initialle) , shuffle  w nakhdmo m paire de positions
	    */	
	    	int nbOfPits = (int)(Math.random() * ((taille - 2) + 1)) + 1;
	    	int nbTotalAgenerer = 2 + nbOfPits;
	    	List<position> allPositions = new ArrayList<position>(taille*taille);
	    	List<position> WGPPositions = new ArrayList<position>(nbTotalAgenerer);
	    	for(int i=1;i<=taille;i++) {
	    		for(int j=1; j<=taille;j++) {
	    			if(i==1 && j==1) continue; //dima pos(1,1) fiha agent w safi.
	    			allPositions.add(new position(i, j));
	    		}
	    	}
	    	
	    	Collections.shuffle(allPositions);
	    	for(int i=0; i<nbTotalAgenerer;i++) {
	    		WGPPositions.add(allPositions.get(i));
	    	}
	    	return WGPPositions;
	    }
	

	 
//*****************AGENT METHODS  ***************////	
	public String AgentPercept(){
		String AgentPercepts="";
		position Ag = this.agent;
		position AgCorspCell = this.cells.get(this.cells.indexOf(Ag));
		AgentPercepts += AgCorspCell.getAllpercepts();
	/*	List<position> AgAdjCells = GetAdjCells(Ag);
		for(position p:AgAdjCells){
			AgentPercepts += p.getAllpercepts();
		}*/
		
		return AgentPercepts;
	}
	
	
	public String AgentActions(){
		String AgentActions = "Avancer, Tourner a Droite, Tourner a Gauche, Ramasser";
		if(this.fleche==1) AgentActions += ", Tirer";
		if(agent.x==1 && agent.y==1) AgentActions += ", Grimper";

	
		return AgentActions;
	}
	
	public String QueSouhaitezVousFaire(){
		String QueSouhaitezVousFaire = "Que souhaitez vous faire ? \n";
		QueSouhaitezVousFaire += "Avancer :0, Droite :1, Gauche :2, Ramasser :3";
		if(this.fleche==1) QueSouhaitezVousFaire += ", Tirer :4";
		if(agent.x==1 && agent.y==1) QueSouhaitezVousFaire += ", Grimper :5";
		
		return QueSouhaitezVousFaire;
	}
	
	
	
	
	public void TurnRight() {

		if(this.sens == Orientation.EST) {
			this.sens = Orientation.SUD;
		}
		else if(this.sens == Orientation.SUD){
			this.sens = Orientation.OUEST;
		} 
		else if(this.sens == Orientation.OUEST){
			this.sens = Orientation.NORD; 
		}
		else if(this.sens == Orientation.NORD){
			this.sens = Orientation.EST;
		}
		this.score--;
	}
	
	
	public void TurnLeft() {
		if(this.sens == Orientation.EST) this.sens = Orientation.NORD;
		else if(this.sens == Orientation.NORD) this.sens = Orientation.OUEST;
		else if(this.sens == Orientation.OUEST) this.sens = Orientation.SUD;
		else if(this.sens == Orientation.SUD) this.sens = Orientation.EST;
		this.score--;
	}
	
	public boolean ifWumpusEnFaceAgent() {
		position WCorespCell = this.cells.get(this.cells.indexOf(this.W));
		position W = WCorespCell;
		position Ag = this.agent;
		Orientation s = this.sens;
		if(s==Orientation.SUD && Ag.y == W.y && Ag.x == W.x+1) return true;
		if(s==Orientation.EST && Ag.y == W.y-1 && Ag.x == W.x) return true;
		if(s==Orientation.OUEST && Ag.y == W.y+1 && Ag.x == W.x) return true;
		if(s==Orientation.NORD && Ag.y == W.y && Ag.x == W.x-1) return true;
		
		return false;
	} 
	
	public void tirerFleche() {
		if(this.fleche==1) {
			this.fleche = 0;
			this.score-=10;
		}	
	}
	
	
	public boolean RamaserGold(position p) {	
		position TheCell = this.cells.get(this.cells.indexOf(p));
		if(TheCell.percepts.contains(Percept.Lueur)) {
			this.score+=1000;
			return true;
		}
		this.score--;		
		return false;
	}
	
	
	
	public Percept Avancer(int taille) {
		/**	if can move, move and return 'v' ..  return 'bp' if wall	**/
		Percept answer= Percept.V;
		
		
		if(this.sens== Orientation.EST) {
			if(this.agent.y!=taille) this.agent.y++;
			else answer = Percept.Bump;
		}
		
		if(this.sens== Orientation.NORD) {
			if(this.agent.x!=taille)this.agent.x++ ;
			else answer= Percept.Bump;
		}
		
		if(this.sens== Orientation.OUEST) {
			if(this.agent.y!=1) this.agent.y--;
			else answer = Percept.Bump;
		}
		
		if(this.sens== Orientation.SUD) {
			if(this.agent.x!=1)this.agent.x-- ;
			else answer= Percept.Bump;		
		}
		this.score--;
		return answer;
	}	
	

	
	
	
	
	public static void main(String[] args) {

		boolean start = true;
		String message="";
		Scanner sc = new Scanner(System.in);
		while(start){
			System.out.println("Bienvenue dans le monde du Wumpus ! ");
			int taille;
			System.out.println("Donner la taille de la caverne");
			while((taille=sc.nextInt()) <= 0) {
				System.out.println("La taille doit etre superieur strictement a 0.");
			}
			
			Grid g = new Grid(taille);
			start=false;
			while(!g.gameOver){
				int choix;
				
				System.out.println("Agent : (" + g.agent.x + ", " + g.agent.y + "), Oriente vers " + g.sens +" et a " + g.fleche + " fleche " );			
				System.out.println("Percepts : " + g.AgentPercept());
			//	System.out.println("Actions : " + g.AgentActions());
				System.out.println(g.QueSouhaitezVousFaire());
				
				
				
				choix = sc.nextInt();
				if(choix==0) { //Avancer
					if(g.Avancer(taille) == Percept.Bump) System.out.println(Percept.Bump);				
				} 
				if(choix==1) g.TurnRight();				//Droite
				if(choix==2) g.TurnLeft();				//Gauche
				
				if(choix==3) { //Ramasser
					if(g.RamaserGold(g.agent)) {
						message = "Vous avez trouvez l'or ! Bravo !";
						g.gameOver=true;
					}
				} 
				if(choix==4) {  //Tirer 
					g.tirerFleche();
					if(g.ifWumpusEnFaceAgent()) {
						System.out.println("AAAAAahhh!!!");
						System.out.println("Vous avez tuez le Wumpus !!!");
						// take off wumpus from cells 
						
						position WcorespCell = g.cells.get(g.cells.indexOf(g.W));
						WcorespCell.percepts.remove(Percept.Wumpus);
						
					}
				}
				
				if(choix==5) { //Grimper
					if(g.agent.equals(new position(1,1))) {
						message = "Vous etes sortie de la caverne!";
						g.gameOver=true;
					}
				} 
				
				if((g.cells.get(g.cells.indexOf(g.agent))).percepts.contains(Percept.Puit)) {
					message = "t7ti fi lbir am3lm ! nod 3la slamtek !";
					g.score-=1000;
					g.gameOver=true;			
				}
				
				if((g.cells.get(g.cells.indexOf(g.agent))).percepts.contains(Percept.Wumpus)) {
					message = "Klak lWumpus a 3mo ! nod 3la slamtek !";
					g.gameOver=true;			
				}
				


			}
			
			System.out.println("Partie termine !");
			System.out.println(message);
			System.out.println("Votre score est : " + g.score);	
			System.out.println("Pour reprendre une nouvelle partie, tapez n'importe quel touche.");
			if(sc.hasNext()) { start=true;sc.next();}
			
		
		}
		sc.close();

	}

}
