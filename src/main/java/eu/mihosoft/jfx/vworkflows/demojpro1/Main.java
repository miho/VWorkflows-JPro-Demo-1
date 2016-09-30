package eu.mihosoft.jfx.vworkflows.demojpro1;

import com.jpro.JProApplication;
import eu.mihosoft.vrl.workflow.FlowFactory;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.VisualizationRequest;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import eu.mihosoft.vrl.workflow.fx.VCanvas;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends JProApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Version: " + System.getProperty("java.version"));
        launch(args);
    }
    private int counter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // scalable canvas
        VCanvas canvas = new VCanvas();
        canvas.setTranslateToMinNodePos(false);
        canvas.setMaxScaleX(1.0);
        canvas.setMaxScaleY(1.0);

        // create a new flow object
        VFlow flow = FlowFactory.newFlow();

        // make it visible
        flow.getModel().setVisible(true);

        // create skin factory for flow visualization
        FXSkinFactory fXSkinFactory = new FXSkinFactory(canvas);

        // generate the ui for the flow
        flow.setSkinFactories(fXSkinFactory);

        generate(flow, 5, 10);

        System.out.println(" --> #nodes: " + counter);

//        // add two nodes to the flow
//        VNode n1 = flow.newNode();
//        VNode n2 = flow.newNode();
//
//        // specify input & output capabilities...
//        
//        // ... for node 1
//        n1.addInput("data");
//        n1.addOutput("data");
//        
//        // ... for node 2
//        n2.addInput("data");
//        n2.addOutput("data");
        // the usual application setup
        Scene scene = new Scene(canvas, 800, 800);
        scene.getStylesheets().add("/eu/mihosoft/jfx/vworkflows/demojpro1/default.css");
        primaryStage.setTitle("VWorkflows Tutorial 01");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void generate(VFlow workflow, int depth, int width) {

        if (depth < 1) {
            return;
        }

        String[] connectionTypes = {"control", "data", "event"};

        for (int i = 0; i < width; i++) {

            counter++;

            VNode n;

            if (i % 2 == 0) {
                VFlow subFlow = workflow.newSubFlow();
                n = subFlow.getModel();
                generate(subFlow, depth - 1, width);
            } else {
                n = workflow.newNode();
            }

            n.setTitle("Node " + n.getId());

            String type = connectionTypes[i % connectionTypes.length];

            n.setMainInput(n.addInput(type)).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
            n.setMainInput(n.addInput("event")).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);

            for (int j = 0; j < 3; j++) {
                n.addInput(type).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
            }

            n.addOutput(type).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
            n.setMainOutput(n.addOutput("event")).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
            n.addOutput(type).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);

            for (int j = 0; j < 3; j++) {
                n.addOutput(type).getVisualizationRequest().set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
            }

            n.setWidth(300);
            n.setHeight(200);

            n.setX((i % 5) * (n.getWidth() + 30));
            n.setY((i / 5) * (n.getHeight() + 30));
        }
    }
}
