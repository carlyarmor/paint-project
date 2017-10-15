/***************************************************************************** * 
 * File name: Paint.java * 
 * Authors:	C. Armor (carly.armor@valpo.edu) * *
 * Modified by:	C. Armor * * Coded by:	C. Armor	      
 * * Date:	09/08/2017		       
 * * Description: * Creates a window with a menubar that can open an image, draw on the image, and save the image.****/ 
package paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.geometry.Orientation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Paint extends Application implements Cloneable {
    File file;
    WritableImage wr;
    Canvas canvas;
    
    /*Creates the window, menubar, and handles the opening and saving of images when the open and save buttons are pressed
      Also sets*/
    @Override
    public void start(Stage primaryStage){ 
        //SETS STAGE WITH SPLIT AND BORDER PANE AND CREATES THE CANVAS
        primaryStage.setTitle("Paint");
        SplitPane sPane = new SplitPane();
        Scene scene = new Scene(sPane, 825, 600);
        //scene.setFill(Color.OLDLACE);
        BorderPane border = new BorderPane();
        sPane.setOrientation(Orientation.VERTICAL);
        StackPane stack = new StackPane();
        Stack<Image> stack1 = new Stack<Image>();
        ImageView myImageView = new ImageView();
        


        //CREATES CANVAS PROPERTIES
        sPane.prefWidthProperty().bind(primaryStage.widthProperty());
        sPane.prefHeightProperty().bind(primaryStage.heightProperty());
        canvas = new Canvas(450,350);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stack.getChildren().add(canvas);;

        //CREATES TOOLBAR
        drawTools drawBar = new drawTools(gc,canvas,border,stack1,stack,scene, myImageView);

        //CREATES MENUBAR
        menuBar menuBar = new menuBar(canvas, file, gc, primaryStage, border, stack,stack1,myImageView);
        
        //CALLS SMARTSAVE UPON EXIT REQUEST
        primaryStage.setOnCloseRequest(WindowEvent ->{
            smartSave(WindowEvent, primaryStage);
        });
       
        gc.setFill(Color.BLACK);
        gc.setFont(Font.getDefault());
        
        //SETS SCENE LAYOUT
        border.setCenter(stack);
        sPane.getItems().add(border);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //CALLS THE START METHOD
    public static void main(String[] args) {
        launch(args);
    }
   
    //ASKS USER TO SAVE UPON EXIT
    public void smartSave(Event we, Stage primaryStage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Smart Save");
            alert.setContentText("Would you like to save?");
            
            ButtonType dont = new ButtonType("Don't save");
            ButtonType smartSave = new ButtonType("Save");
            ButtonType cancel = new ButtonType("Cancel");
            
            alert.getButtonTypes().setAll(dont, smartSave, cancel);
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == dont){
                System.exit(0);
            }
            if(result.get() == smartSave){
                FileChooser fChooser = new FileChooser();
            
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*bmp)", "*.BMP");
                fChooser.getExtensionFilters().addAll(extFilterBMP);
                fChooser.getExtensionFilters().add(extFilter);
                fChooser.getExtensionFilters().add(extFilterPNG);
                
                fChooser.getExtensionFilters().addAll(extFilter,extFilterPNG);
                
                File file = fChooser.showSaveDialog(primaryStage);
                if(file!=null){
                    WritableImage image = canvas.snapshot(null, null);
            
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    }
                    catch (IOException s) {  
                        throw new RuntimeException(s);
                    }
                }
                
            }
            if (result.get() == cancel){
                we.consume();
            }
    }

    //UNDOS LAST EVENT    
    public void undo(Stack<Image> stack1, StackPane stack, Canvas canvas, GraphicsContext gc){
        if (!stack1.empty()){     
            gc.drawImage(stack1.pop(),0,0);
        }
    }   
}
