import java.util.Comparator;


@SuppressWarnings("rawtypes")
public class  SortGrid implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		position pos1 = (position)o1;
		position pos2 = (position)o2;
		

		if(pos1.x > pos2.x) return -1;
		else if(pos1.x < pos2.x) return 1;
		else {
			if(pos1.y < pos2.y) return 1;
			else if(pos1.y > pos2.y) return -1;
			else return 0;
		}
	}

}
