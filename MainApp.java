/*
 * Copyright 2015 Patrick Osgood
 */

package pathmaster;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pathmaster.Domain.CNode;

/**
 *
 * @author patrick
 */
public class MainApp extends Application {
    //Status text
    Text topText;
    
    //Creates grid for algorithm
    CNode[][] grid = new CNode[8][8];
    
    //Creating open and closed lists
    ArrayList<CNode> open = new ArrayList();
    ArrayList<CNode> closed = new ArrayList();
    ArrayList<CNode> neighbors = new ArrayList();
    
    //Current node being investigated
    CNode current;
     
    //Start and ending nodes
    CNode startNode;
    CNode targetNode;
    
    
    int neighborsThatAreBarriers = 0;
     
    @Override
    public void start(Stage primaryStage) {
    
        //CREATE GUI COMPONENTS
        //Main Housing of components
        BorderPane border = new BorderPane();
        
        //Setup status Text
        topText = new Text("Click boxes to select barriers");
        topText.setFont(new Font("Verdana", 35));
        border.setTop(topText);
        
        //Creates grid pane
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(300, 300);
        gridPane.setGridLinesVisible(true);
        gridPane.setVisible(true);
        
        //Setting up grid array
        for (int x = 0; x < 8; x ++){
            for(int y = 0; y < 8; y ++){
                grid[x][y] = new CNode(x, y);
                gridPane.add(grid[x][y], x, y);
            }
        }
        //Assigning starting and ending nodes
        startNode = grid[0][0];
        startNode.setStyle("-fx-background-color: blue");
        Text startT = new Text("Start");
        startT.setStroke(Color.WHITE);
        startT.setX(startNode.getWidth() + 25);
        startT.setY(startNode.getHeight() + 40);
        startNode.getChildren().add(startT);
        
        targetNode = grid[7][7];
        targetNode.setStyle("-fx-background-color: blue");
        Text endT = new Text("End");
        endT.setStroke(Color.WHITE);
        endT.setX(targetNode.getWidth() + 25);
        endT.setY(targetNode.getHeight() + 40);
        targetNode.getChildren().add(endT);
        
        // Set H for each node
        for (int x = 0; x < 8; x ++){
            for(int y = 0; y < 8; y ++){
                grid[x][y].setH(targetNode);
            }
        }
        border.setCenter(gridPane);
        
        //Set Buttons and set in pane
        FlowPane flow = new FlowPane();
        flow.setAlignment(Pos.CENTER);
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setHgap(60);
        
        Button startAlgorithm = new Button();
        startAlgorithm.setPadding(new Insets(10, 10, 10, 10));
        startAlgorithm.setText("Find Path");
        
        Button clearGrid = new Button();
        clearGrid.setPadding(new Insets(10, 10, 10, 10));
        clearGrid.setText("Reset Grid");

        flow.getChildren().add(startAlgorithm);
        flow.getChildren().add(clearGrid);
        border.setBottom(flow);
        
        //Starts algorithm working!
        startAlgorithm.setOnMouseClicked(e -> handleStartAlgorithm());
        clearGrid.setOnMouseClicked(e -> handleClearGrid());

        Scene scene = new Scene(border, 600, 660);
        
        primaryStage.setTitle("PathMaster!  ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
   
    public void handleStartAlgorithm(){
        startNode.setF();
        open.add(startNode);
        
        while(true){
        current = findLowestF(open);
        //This code below shows 
//      current.setStyle("-fx-background-color: green");
        closed.add(current);
        open.remove(current);
        
        if (current == targetNode){
           traceback();
            break;
        }
         getNeighbors(current);
         
         //Iterating through neighbors list for current node
        for (int j = 0; j < neighbors.size(); j++){
            if (neighbors.get(j).isBarrier() == true || 
                closed.contains(neighbors.get(j))){
                neighborsThatAreBarriers++;
                continue;
            }
            if(neighbors.get(j).getG() > current.getG() + 1 || open.contains(neighbors.get(j)) == false){
                neighbors.get(j).setF();
                neighbors.get(j).setParentNode(current);
                if (open.contains(neighbors.get(j)) == false){
                    open.add(neighbors.get(j));  
                }
            }
            
        }
        if (neighborsThatAreBarriers == neighbors.size() && open.isEmpty()){
            topText.setText("Cannot reach end node.");
            break;
        }
        neighborsThatAreBarriers = 0;
        neighbors.clear();
        }
    }
    
    
    public CNode findLowestF(ArrayList<CNode> open){
        CNode lowest = new CNode();
        lowest.setG(50);
        lowest.setH(50);
        lowest.setF();
        for (int i = 0; i < open.size(); i++){
            if (open.get(i).getF() <= lowest.getF() && open.get(i).getH() < lowest.getH()){
                lowest = open.get(i);   
            }
        }
        return lowest;
    }
    //This method puts neighbors in neighbors array list, and calculates g
    public void getNeighbors(CNode current){
        if (current.getY() + 1 <= 7){ //BOTTOM NEIGHBOR
            if(grid[current.getX()][current.getY() + 1].getG() == 0){
               grid[current.getX()][current.getY() + 1].setG(current.getG() + 1);
            }
            neighbors.add(grid[current.getX()][current.getY() + 1]);
        }
        
        if (current.getX() + 1 <= 7){ //RIGHT NEIGHBOR
             if(grid[current.getX() + 1][current.getY()].getG() == 0){
               grid[current.getX() + 1][current.getY()].setG(current.getG() + 1);
            }
            neighbors.add(grid[current.getX() + 1][current.getY()]);
        }
        
        if (current.getY() - 1 >= 0){  //TOP NEIGHBOR
            if (grid[current.getX()][current.getY() - 1].getG() == 0){
                grid[current.getX()][current.getY() - 1].setG(current.getG() + 1);
            }
            neighbors.add(grid[current.getX()][current.getY() - 1]);
        }
        
        if (current.getX() - 1 >= 0){ //LEFT NEIGHBOR
            if(grid[current.getX() - 1][current.getY()].getG() == 0){
               grid[current.getX() - 1][current.getY()].setG(current.getG() + 1);
            }
            neighbors.add(grid[current.getX() - 1][current.getY()]);
        }
        
        
        
    }
    
    public void traceback(){
        topText.setText("Path Found");
        while (current != startNode){
            if (current != targetNode){
                current.setStyle("-fx-background-color: red");
            }
        current = current.getParentNode();
        }
    }
    
    //Clears grid nodes to start another map
    public void handleClearGrid(){
        for (int x = 0; x < 8; x ++){
            for(int y = 0; y < 8; y ++){
                grid[x][y].setG(0);
                grid[x][y].setF();
                grid[x][y].setBarrier(false);
                grid[x][y].setParentNode(new CNode());
                if (grid[x][y] != startNode && grid[x][y] != targetNode){
                    grid[x][y].setStyle("-fx-background-color: none");
                }
            }
            open.clear();
            closed.clear();
            topText.setText("Click boxes to select barriers");
        }
    }
 //END OF CLASS       
}
