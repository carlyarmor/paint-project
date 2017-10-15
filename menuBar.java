package paint;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class menuBar extends Paint{
    public menuBar(Canvas canvas, File file, GraphicsContext gc, Stage primaryStage, BorderPane border, StackPane stack, Stack stack1,ImageView myImageView){
        //CREATES MENU ITEMS
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemOpen = new MenuItem("Open");
        menuFile.getItems().addAll(menuItemOpen);
        MenuItem menuItemSave = new MenuItem("Save");
        menuFile.getItems().addAll(menuItemSave);
        MenuItem menuItemSaveAs = new MenuItem("Save As");
        menuFile.getItems().addAll(menuItemSaveAs);
        Menu menuEdit = new Menu("Edit");
        MenuItem menuItemUndo = new MenuItem("Undo");
        menuEdit.getItems().addAll(menuItemUndo);
        
        //CREATES KEYBOARD SHORTCUTS FOR MENUBAR ITEMS
        menuItemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O,KeyCombination.CONTROL_DOWN));
        menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN));
        menuItemSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.A,KeyCombination.CONTROL_DOWN));
        menuItemUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z,KeyCombination.CONTROL_DOWN));
        
        //ADDS MENU ITEMS TO MENUBAR
        menuBar.getMenus().addAll(menuFile, menuEdit);
        
        //OPENS AN IMAGE THE USER CHOOSES WHEN THE OPEN BUTTON FROM THE MENU BAR IS PRESSED
        menuItemOpen.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                FileChooser fileChooser = new FileChooser();
            
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*bmp)", "*.BMP");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterBMP);
            
                File file2 = fileChooser.showOpenDialog(null);
            
                Image bImage = new Image(file2.toURI().toString(),canvas.getWidth(),canvas.getHeight(),true,false);
                /*PixelReader pr = bImage.getPixelReader();
            
                WritableImage wr = new WritableImage((int)bImage.getWidth(), (int)bImage.getHeight());
                PixelWriter pw = wr.getPixelWriter();
            
                for (int x = 0; x < bImage.getWidth(); x++) {
                    for (int y = 0; y < bImage.getHeight(); y++) {
                        pw.setArgb(x, y, pr.getArgb(x, y));
                    }
                }
                canvas.setHeight(bImage.getHeight());
                canvas.setWidth(bImage.getWidth());
                gc.drawImage(wr,0,0);*/
                
                myImageView.setImage(bImage);

                stack.setPrefWidth(canvas.getWidth());
                stack.setPrefHeight(canvas.getHeight());
                stack.setMaxWidth(canvas.getWidth());
                stack.setMaxHeight(canvas.getHeight());
                stack.getChildren().clear();
                stack.getChildren().addAll(myImageView, canvas);
                stack1.push(stack.snapshot(null, null));
            }
        });
        
        //SAVE THE OPENED IMAGE TO ITS ORIGINAL STORAGE PLACE WHEN THE SAVE BUTTON FROM MENUBAR IS PRESSED
        menuItemSave.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                WritableImage image = stack.snapshot(null, null);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                } catch (IOException s) {
                    throw new RuntimeException(s);   
                }
                }
        });
        
        //SAVES OPENED IMAGE TO USER CHOSEN STORAGE PLACE WHEN SAVE AS BUTTON FROM MENUBAR IS PRESSED
        menuItemSaveAs.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                FileChooser fChooser = new FileChooser();
            
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*bmp)", "*.BMP");
                fChooser.getExtensionFilters().addAll(extFilter, extFilterPNG, extFilterBMP);
            
                File file2 = fChooser.showSaveDialog(primaryStage);
            
                WritableImage image = stack.snapshot(null, null);
            
                try {
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file2);
                }
                catch (IOException s) {  
                    throw new RuntimeException(s);
                }
            }
        });
        
        //SAVE THE OPENED IMAGE TO ITS ORIGINAL STORAGE PLACE WHEN THE SAVE BUTTON FROM MENUBAR IS PRESSED
        menuItemUndo.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                    undo(stack1, stack, canvas, gc);
                }
        });
        
        //ADDS MENUBAR TO SCENE
        border.setTop(menuBar);
    }  
}
