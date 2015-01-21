/*
 * Copyright 2015 Patrick Osgood
 */

package pathmaster.Domain;


import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * CNode is a type of pane with numeric fields for A* algorithm use. 
 * CNode is intended to be created within a loop structure that adds x y parameters
 * in it's loop iterations. 
 * 
 * When setting up object you MUST call setH later:
 * CNode nodeName = new CNode(x, y);
 * nodeName.setH(targetNode);
 * 
 * 
 * @author patrick
 */
public class CNode extends Pane {
    private final int myX, myY;
    private boolean barrier;
    private CNode parent;
    private int g, h, f;
    private Line line1, line2;
   
   
   //Default Constructor
    public CNode(){
        setStyle("-fx-border-color: black");
        this.setPrefSize(60, 60);
        this.setOnMouseClicked(e -> handleMouseClick());
        this.myX = 0;
        this.myY = 0;
        this.barrier = false;
        this.g = 0;
        this.h = 0;
        this.f = 0;
    }
   
   //Constructor with params citing location in array
    public CNode(int x, int y){
        setStyle("-fx-border-color: black");
        this.setPrefSize(75, 75);
        this.setOnMouseClicked(e -> handleMouseClick());
        this.myX = x;
        this.myY = y;
        this.barrier = false;
        this.g = 0;
        this.h = 0;
        this.f = 0;
        this.parent = new CNode();
    }
   
   
    // Getter Functions
    public int getX(){
        return myX;
    }
   
    public int getY(){
        return myY;
    }
   
    public boolean isBarrier(){
        return barrier;
    }
   
    public CNode getParentNode(){
        return parent;
    }
   
    public int getG(){
        return g;
    }
   
    public int getH(){
        return h;
    }
    
    public int getF(){
        return f;
    }
   
    //Setter Methods
    public void setBarrier(boolean b){
        this.barrier = b;
       
        //This adds the x to box indicating a barrier
        if (this.barrier == true){
            line1 = new Line(10, 10, this.getWidth() - 10, this.getHeight() - 10);
            line1.endXProperty().bind(this.widthProperty().subtract(10));
            line1.endYProperty().bind(this.heightProperty().subtract(10)); 
            line2 = new Line(10, this.getHeight() - 10, this.getWidth() - 10, 10);
            line2.startYProperty().bind(this.heightProperty().subtract(10));
            line2.endXProperty().bind(this.widthProperty().subtract(10));
            this.getChildren().addAll(line1, line2);
        }
        
        if (this.barrier == false){
            this.getChildren().removeAll(line1, line2);
        }
    }
   
    public void setParentNode(CNode parent){
        this.parent = parent;
    }
   
    public void setG(int g){
        this.g = g;
    }
   
    /* setH()
     * Incorporates heuristic for A*. 
     * In this case we use Manhattan calculations (no diagonals)
     * MUST BE CALLED AFTER OBJECT INSTANTIATED
    */
    public void setH(CNode targetNode){
        this.h = Math.abs(myX - targetNode.getX()) + Math.abs(myY - targetNode.getY());
    }
   
    public void setH(int h){
        this.h = h;
    }
   
    public void setF(){
        this.f = g + h;
    }
   
    public void handleMouseClick(){
        if (barrier == false){
            this.setBarrier(true);
            return;    //this keeps method from turning barrier to false again
                       //automatically. Leave in!
        }
        
        if (barrier == true){
            this.setBarrier(false);
        }
    }
    //END OF CLASS
}
