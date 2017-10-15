package paint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Stack;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.Pos.TOP_LEFT;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import static javafx.scene.text.Font.getFontNames;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class drawTools extends Paint {
    public drawTools(GraphicsContext gc, Canvas canvas, BorderPane border, Stack stack1, StackPane stack,Scene scene, ImageView myImageView){       
        //CREATES BUTTONS FOR TOOLBAR
        Button drawLine = new Button("Draw Line");
        Button freeDraw = new Button("Free Draw");
        Button rectangle = new Button("Draw Rectangle");
        Button square = new Button("Draw Square");
        Button select = new Button("Select");
        Button move = new Button("Move");
        Button eraser = new Button("Eraser");
        Button dropper = new Button("Dropper");
        Button write = new Button("Write");
       
        //CREATES SLIDER FOR LINE THICKNESS
        Slider slider = new Slider();
        slider.setMin(1);
        slider.setMax(40);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        //CREATES RECTANGLE FOR SELECT/MOVE
        Rectangle r = new Rectangle();
        r.setHeight(0);
        r.setWidth(0);
        r.setX(0);
        r.setY(0);

        //CREATES COLORPICKER
        ColorPicker cp = new ColorPicker();
        cp.setValue(Color.BLUE);
        cp.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                gc.setStroke(cp.getValue());
            }
        });
        
        //CALLS WRITE
        write.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                write(canvas, gc, cp.getValue(), slider.getValue(),stack1,stack,cp, myImageView);
            }
        });

        //CALLS DROPPER
        dropper.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                dropper(canvas, gc, stack,scene,stack1,cp);
            }
        });
        
        //CALLS ERASER
        eraser.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                eraser(canvas, gc, cp.getValue(), slider.getValue(),stack1,stack,cp, myImageView);
            }
        });
        
        //CALLS DRAWS RECTANGLE
        rectangle.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                drawRect(canvas, gc, cp.getValue(), slider.getValue(),stack1,stack);
            }
        });
        
        //CALLS DRAW SQUARE
        square.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                drawSquare(canvas, gc, cp.getValue(), slider.getValue(),stack1,stack);
            }
        });
        
        //CALLS FREE DRAW TO LET THE USER SCRIBBLE
        freeDraw.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                freeDraw(canvas, gc, cp.getValue(), slider.getValue(),stack1,stack);
            }
        });
        
        //CALLS SELECT TO LET USER SELECT SECTION OF CANVAS FOR MOVEMENT
        select.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                select(canvas, gc, cp.getValue(), slider.getValue(),stack1, canvas,r);
            }
        });
        
        //CALLS MOVE TO LET USER MOVE SECTION OF CANVAS
        move.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                move(canvas,gc,r,stack,scene,stack1);
            }
        });
        
        //CALLS DRAW LINE WHEN THE DRAW LINE BUTTON FROM DRAW BAR IS PRESSED
        drawLine.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                drawLine(canvas, gc, cp.getValue(), slider.getValue(), stack1,stack);
             //   stack1.push(canvas.snapshot(null, null));
            }
        });

        //CREATES TOOLBAR FUNCTIONS, ADDS BUTTONS SLIDER AND COLOR PICKER TO IT AND ADDS TOOLBAR TO SCENE
        ToolBar toolbar = new ToolBar(drawLine,freeDraw,rectangle, square,slider,cp);
        ToolBar toolbar2 = new ToolBar(select,move,eraser,dropper,write);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(toolbar,toolbar2);
        border.setBottom(vbox);
        
    }
    
    //PRINTS WORDS ON TOP OF PICTUE
    public void write(Canvas canvas, GraphicsContext gc, Color c, double w, Stack<Image> stack1, StackPane stack, ColorPicker cp, ImageView myImageView){
        clear(canvas,gc);

        Stage scene2 = new Stage();
        BorderPane bPane = new BorderPane();
        Scene scene = new Scene(bPane, 300,150);
        scene2.setScene(scene);

        Label text = new Label("Enter text ");
        Label fontSize = new Label("Font size  ");
        Label font = new Label("Font         ");
        
        ChoiceBox<String> cb = new ChoiceBox<String>();
        
        cb.getItems().addAll(javafx.scene.text.Font.getFamilies());
        cb.setValue("Font");
        gc.setStroke(Color.BLACK);
   
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gc.setLineWidth(1);
                Button okay = new Button("Okay");
                TextField t1 = new TextField();
                TextField t2 = new TextField();
                
                HBox hbox = new HBox();
                VBox vbox1 = new VBox();
                HBox hbox2 = new HBox();
                HBox hbox3 = new HBox();
                hbox2.getChildren().addAll(text,t1);
                hbox3.getChildren().addAll(font,cb);
                hbox.getChildren().addAll(fontSize,t2);
                vbox1.getChildren().addAll(hbox2,hbox,hbox3);

                
                scene2.show();
                bPane.setCenter(vbox1);
                bPane.setBottom(okay);
                

                okay.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                        public void handle(ActionEvent event){
                            gc.setFont(new Font((String)cb.getValue(), Double.parseDouble(t2.getText())));
                            gc.strokeText(t1.getText(),e.getX(),e.getY());
                            scene2.close();
                            gc.setLineWidth(w);
                    }
                });
            }
        });
        
        Image image = stack.snapshot(null,null);
        stack1.push(image);
    }
    
    //SETS STROKE TO COLOR OF SELECTED PIXEL
    public void dropper(Canvas canvas, GraphicsContext gc, StackPane stack, Scene scene,Stack stack1, ColorPicker cp){
        clear(canvas,gc);
        
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Image bImage = stack.snapshot(null,null);
                PixelReader pr = bImage.getPixelReader();
                Color c;
                c = pr.getColor((int)e.getX(),(int)e.getY());
                
                cp.setValue(c);
                gc.setStroke(c);
                Image image = stack.snapshot(null,null);
                stack1.push(image);
            }
        });
    }
    
    //ERASES PIXELS FROM CANVAS
    public void eraser(Canvas canvas, GraphicsContext gc, Color c, double w, Stack<Image> stack1, StackPane stack, ColorPicker cp, ImageView myImageView){
        clear(canvas, gc);
        cp.setValue(Color.TRANSPARENT);
        gc.setStroke(Color.TRANSPARENT);
        gc.setLineWidth(w);
        
        PixelWriter pw = gc.getPixelWriter();
        
        canvas.setOnMousePressed((MouseEvent e) -> {
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());
                gc.stroke();
                pw.setColor((int)e.getX(), (int)e.getY(), Color.TRANSPARENT);
                gc.clearRect(e.getX(),e.getY(),w,w);
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();   
                pw.setColor((int)e.getX(), (int)e.getY(), Color.TRANSPARENT);
                gc.clearRect(e.getX(),e.getY(),w,w);
        });
        canvas.setOnMouseReleased((MouseEvent e) ->{
            gc.closePath();
            Image image = stack.snapshot(null,null);
            stack1.push(image);
            pw.setColor((int)e.getX(), (int)e.getY(), Color.TRANSPARENT);
            

        });
    }
    
    //MOVES SELECTED RECTANGLE
    public void move(Canvas canvas, GraphicsContext gc, Rectangle r, StackPane stack, Scene scene,Stack stack1){
        clear(canvas, gc);
        
        WritableImage wr3 = new WritableImage((int)r.getWidth(),(int)r.getHeight());
        PixelWriter pw = wr3.getPixelWriter();
        
        Image bImage = stack.snapshot(null,null);
        PixelReader pr = bImage.getPixelReader();

        for (int x = 0; x <(int)(r.getWidth()); x++) {
            for (int y = 0; y <(int)(r.getHeight()); y++) {
                pw.setArgb(x, y, pr.getArgb((int)r.getX()+x, (int)r.getY()+y));
            }
        }
        gc.setFill(Color.WHITE); 
        gc.fillRect(r.getX(), r.getY(), r.getWidth()+1, r.getHeight()+1);
        Canvas canvas2 = new Canvas(r.getWidth()+1,r.getHeight()+1);
        stack.getChildren().add(canvas2);
        
        canvas2.setTranslateX(-(canvas.getWidth()/2)+(r.getWidth()/2)+r.getX());
        canvas2.setTranslateY(-(canvas.getHeight()/2)+(r.getHeight()/2)+r.getY());
        
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        gc2.drawImage(wr3,0,0);
 
 
        canvas.setOnMousePressed((MouseEvent e) -> {
            canvas2.setTranslateX(e.getSceneX()-(scene.getWidth())/2);
            canvas2.setTranslateY(e.getSceneY()-(scene.getHeight())/2);
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {   
            canvas2.setTranslateX(e.getSceneX()-(scene.getWidth())/2);
            canvas2.setTranslateY(e.getSceneY()-(scene.getHeight())/2);  
        });
        
        canvas.setOnMouseReleased((MouseEvent e) ->{
            gc.drawImage(wr3, (e.getX()), e.getY());
            stack.getChildren().remove(canvas2);
            Image image = stack.snapshot(null,null);
            stack1.push(image);
        });
    }
    
    //DRAWS STRAIGHT LINE
    public void drawLine(Canvas canvas, GraphicsContext gc, Color c, double w,Stack stack1, StackPane stack){
        clear(canvas, gc);
        gc.setStroke(c);
        gc.setLineWidth(w);

        canvas.setOnMousePressed((MouseEvent e) -> {
            gc.beginPath();
            gc.lineTo(e.getX(),e.getY());
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {
            
        });
        canvas.setOnMouseReleased((MouseEvent e) ->{
            gc.lineTo(e.getX(),e.getY());
            gc.stroke();
            gc.closePath();
            Image image = stack.snapshot(null,null);
            stack1.push(image);
        });
    }
    
    //DRAWS ON CANVAS WHEREVER MOUSE IS CLICKED AND DRAGGED
    private void freeDraw(Canvas canvas, GraphicsContext gc, Color c, double w, Stack<Image> stack1, StackPane stack){
        clear(canvas, gc);
        gc.setStroke(c);
        gc.setLineWidth(w);
        
        canvas.setOnMousePressed((MouseEvent e) -> {
            gc.setLineWidth(w); 
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {
            gc.setLineWidth(w);    
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
        canvas.setOnMouseReleased((MouseEvent e) ->{
            gc.closePath();
            Image image = stack.snapshot(null,null);
            stack1.push(image);
        });
    }
    
    //DRAWS A RECTANGLE
    public void drawRect(Canvas canvas, GraphicsContext gc, Color c, Double w, Stack stack1, StackPane stack){
        Rectangle r1 = new Rectangle();
        
        clear(canvas, gc);
        gc.setLineWidth(w);
        gc.setStroke(c);
        
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           r1.setX(e.getX());
           r1.setY(e.getY());
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {
            
        });
        canvas.setOnMouseReleased((MouseEvent e) ->{
            r1.setWidth(e.getX()-r1.getX());
            r1.setHeight(e.getY()-r1.getY());
            gc.strokeRect(r1.getX(), r1.getY(), r1.getWidth(), r1.getHeight());
            gc.closePath();
            Image image = stack.snapshot(null,null);
            stack1.push(image);
        });
    }
    
    //DRAWS A SQUARE
    public void drawSquare(Canvas canvas, GraphicsContext gc, Color c, Double w, Stack stack1, StackPane stack){
        clear(canvas, gc);
        gc.setLineWidth(w);
        Rectangle r = new Rectangle();
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           r.setX(e.getX());
           r.setY(e.getY());
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {
            
        });
        canvas.setOnMouseReleased((MouseEvent e) ->{
            r.setWidth(e.getX()-r.getX());
            r.setHeight(e.getY()-r.getY());
            gc.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getWidth());
            gc.closePath();
            Image image = stack.snapshot(null,null);
            stack1.push(image);
        });
    }
    
    //SELECTS AREA FOR MOVEMENT
    public void select(Canvas canvas, GraphicsContext gc, Color c, Double w, Stack stack1, Canvas canvas2, Rectangle r){
        clear(canvas, gc);
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(w);
       
        clear(canvas, gc);
        gc.setLineWidth(w);

        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.clearRect(r.getX(), r.getY(), r.getWidth()+1, r.getHeight()+1);
           gc.beginPath();
           r.setX(e.getX());
           r.setY(e.getY());
        });
        canvas.setOnMouseDragged((MouseEvent e) -> {

        });
        canvas.setOnMouseReleased((MouseEvent e) ->{
            r.setWidth(e.getX()-r.getX());
            r.setHeight(e.getY()-r.getY());
            gc.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            gc.closePath();
       });
    }

    //RESETS THE MOUSE EVENTS
    public void clear(Canvas canvas, GraphicsContext gc){
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseClicked(null);
    }
}
