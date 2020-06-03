import java.util.HashSet;

public class position{
	int x;
	int y;
	HashSet<Percept> percepts;
	
    public boolean equals(Object o) {
        position pos = (position) o;
        return pos.x == x && pos.y == y;
    }

   
    public position(int l) {
    	this.x = (int)(Math.random() * ((l - 1) + 1)) + 1;
    	this.y = (int)(Math.random() * ((l - 1) + 1)) + 1;
    }
    
    
    public position(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        percepts = new HashSet<>();
    }
    
    public String getAllpercepts() {
    	String AllPerceptString ="";
    	for(Percept p:this.percepts) {
    		if(p == Percept.Wumpus || p == Percept.Puit) continue;    		
    		AllPerceptString = AllPerceptString.concat(p + " ");
    	}   	
    	return AllPerceptString;
    }
    
    public String toString() {
    	
    	String descrip = "position  : (" + x + ", " + y+ ") => ";
		descrip = descrip.concat( this.getAllpercepts());

    	return descrip;
    }


    
    
}