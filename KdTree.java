
/*************************************************************************
 *************************************************************************/
package s3;
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class KdTree {
	private int size;
	private Node root;
	
	private static class Node{
		private Point2D p;
		private Node leftChild;
		private Node rightChild;
		private RectHV rect;
		
		public Node(Point2D point, Node lc, Node rc, RectHV rec) {
			p = point;
			leftChild = lc;
			rightChild = rc;
			rect = rec;
		}
	}
	
	public SET<Point2D> set;
    // construct an empty set of points
    public KdTree() {
    	size = 0;
    	root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        if(size == 0) {
        	return true;
        }
        return false;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
    	root = insHelp(root, p, true, 0.0, 0.0, 1.0, 1.0);
    };
    // Recursion fall insert
    private Node insHelp(Node node, Point2D p, boolean vertical, double xLow, double yLow, double xHi, double yHi) {
    	// ef n��ann er null, �a� ���ir a� komi� er �t� enda �ar sem � a� inserta n��unni.
    	//�� er b�in til n� n� n��a me� RectHv gildum sem sem komu �r recursionuni.
    	if(node == null) {
    		RectHV recVal = new RectHV(xLow, yLow, xHi, yHi);
    		size++;
    		return new Node(p, null, null, recVal);
    	}
    	else if(node.p.x() == p.x() && node.p.y() == p.y()) {
    		return node;
    	}
    	// ef l��r�tt �� �arf a� compera x-value
    	if(vertical == true) {
    		//ef n�ja x er minna heldur en x n��urnar �� fer n�ja n��an til vinstri, annas til h�gri
    		if(node.p.x() > p.x()) {
    			//h�r �arf a� l�ta xmax gildi� vera sama og x gildi foreldran��urnar n��urnar svo �essi n��a fara ekki yfir foreldra n��una
    			node.leftChild = insHelp(node.leftChild, p, !vertical, xLow, yLow, node.p.x(), yHi);
    		}
    		else {
    			//sama og fyrir ofan nema xmin gildi� �arf a� vera skilgreint h�r
    			node.rightChild = insHelp(node.rightChild, p, !vertical, node.p.x(), yLow, xHi, yHi);
    		}
    	}else {
    		// ef l�r�tt compera y-value
    		if(node.p.y() > p.y()) {
    			//ymxa m� ekkifara framyfir forledran��u
    			node.leftChild = insHelp(node.leftChild, p, !vertical, xLow, yLow, xHi, node.p.y());
    		}
    		else {
    			//ymin m� ekki fara framyfir foreldra n��u
    			node.rightChild = insHelp(node.rightChild, p, !vertical, xLow, node.p.y(), xHi, yHi);
    		}
    	}
    	
    	return node;
    }
    
    // does the set contain the point p?
    public boolean contains(Point2D p) {
       return contHelp(root, p, true);
    }
    
    private boolean contHelp(Node node, Point2D p, boolean vertical){
    	//ef komi� er �t� enda.. �� �
    	if(node == null) {
    		return false;
    	}
    	
    	//ef n��an er fundin �arf a� skila true upp allt recursion-i�
    	else if(node.p.x() == p.x() && node.p.y() == p.y()) {
    		return true;
    	}
    	else {
    		//ef l��r�tt �� �arf a� compera x-value
    		if(vertical == true) {
    			if(node.p.x() > p.x()) {
    				//ef x-value sem er veri� a� leita af er minna en x-value n��urnar sem er komi� � �arf a� fara til vinstri
    				return contHelp(node.leftChild, p, !vertical);
    			}
    			// ef x-value sem er veri� a� leita af er st�rra en x-values n��unar sem er komi� � �arf a� fara til h�gri
    			else {
    				return contHelp(node.rightChild, p, !vertical);
    			}
    		}
    		else {
    			if(node.p.y() > p.y()) {
    				// sama h�r nema me� y-value.
    				return contHelp(node.leftChild, p, !vertical);
    			}
    			else {
    				return contHelp(node.rightChild, p, !vertical);
    			}
    		}
    	}
    }

    // draw all of the points to standard draw
    public void draw() {
    	drawHelp(root, true);
    }
    
    // h�r er n��ur teikna�a recirsivie, n��an teiknu� svo vinstri svo h�gri.
    public void drawHelp(Node node, boolean vertical) {
    	if(node == null) {
    		return;
    	}
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.setPenRadius(0.01);
    	node.p.draw();
    	// ef l��r�tt �� er teikna� fr� x-punkti og til y-min og y-max
    	if(vertical == true) {
    		StdDraw.setPenColor(StdDraw.RED);
    		StdDraw.setPenRadius();
    		StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
    	}
    	//annars y-punkt til xmin og xmax.
    	else {
    		StdDraw.setPenColor(StdDraw.BLUE);
    		StdDraw.setPenRadius();
    		StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
    	}
    
    	drawHelp(node.leftChild, !vertical);
    	drawHelp(node.rightChild, !vertical);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
    	ArrayList<Point2D> list = new ArrayList<Point2D>();
        ranHelp(root, rect, list);
        return list;
    }
    private void ranHelp(Node node, RectHV rec, ArrayList<Point2D> list){
    	if(node == null) {
    		return;
    	}
    	if(rec.contains(node.p)) {
    		list.add(node.p);
    	}
    	if(rec.intersects(node.rect)) {
    		ranHelp(node.leftChild, rec, list);
    		ranHelp(node.rightChild, rec, list);
    	}
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return nearHelp(root, p, root.p, true);
    }
    //Hj�lparfall fyrir recuresive method.
    public Point2D nearHelp(Node node, Point2D distancePoint, Point2D near, boolean vertical) {
    	
    	Point2D nearestPoint = near;
    	
    	//ef komi� er �t� enda � tr�nu �� �arf a� fara a� returnar points
    	if(node == null) {
    		return nearestPoint;
    	}
    	//�arf a� breyta n�rsta punktinum ef fundinn var minni punktur
    	if(node.p.distanceSquaredTo(distancePoint) < nearestPoint.distanceSquaredTo(distancePoint)) {
    		nearestPoint = node.p;
    	}
    	//ef lengdin mill nearestPoint og punktsins sem er veri� a� compera er minni en lenddin milli rect n��urnar og punktsisn �� �arf ekki a� sko�a subtr� �eirra n��u.
    	if(nearestPoint.distanceSquaredTo(distancePoint) > node.rect.distanceSquaredTo(distancePoint)) {
			nearestPoint = nearHelp(node.leftChild, distancePoint, nearestPoint, !vertical);
			nearestPoint = nearHelp(node.rightChild, distancePoint, nearestPoint, !vertical);
    	}
    	// �egar b�i� er a� fara � gegnum allar n��ur sem koma til greina
    	return nearestPoint;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int nrOfRecangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRecangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRecangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        KdTree set = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRecangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }

        out.println();
    }
}
