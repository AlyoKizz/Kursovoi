import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.io.IOException;

public class Demo {

    JButton b1;
    public static void main(String[] args) throws IOException {
        File file = getFile();
        Graph<String, String> g = getGraph(file);
        System.out.println("Количесвто вершин - "+g.getVertexCount());
        System.out.println("Количество ребер - " + g.getEdgeCount());
        double clust =  clustering(g);
        System.out.println("Коэффициент кластреризации - " + clust);
        frame(g);

    }
    public static double clustering(Graph g) throws IOException {

        double clust=0,openL, closeL, o=0;

        for (Object ij:g.getVertices())
        {
            openL=0; closeL=0;
            Object mass[] = g.getNeighbors(ij).toArray();
            int n = mass.length;
            openL = n * (n - 1) / 2;
            if(openL!=0) {
                for (int i = 0; i < n - 1; i++) {
                    for (int j = i + 1; j < n; j++) {
                        if (g.isNeighbor(mass[i], mass[j])) closeL++;
                    }
                }
                o++;
                clust = clust + closeL / (openL - closeL);
            }
        }
        File file = new File("1.txt");
        file.createNewFile();
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fr.write(String.valueOf(clust/o));
        fr.close();
        return clust/o;
    }

    public static Graph<String, String> getGraph(File file) {
        Factory<String> edgeFactory = new Factory<String>() {
            int i;
            @Override
            public String create() {
                return new Integer(i++).toString();
            }};

        Graph<String, String> graph = new SparseMultigraph<String, String>();
        PajekNetReader pnr = new PajekNetReader(edgeFactory);
        try {
            graph = pnr.load(file.getAbsolutePath(), graph);
        } catch (Exception e) {
            System.out.println(" Извини, но не работает загрузка графа");
            System.exit(-1);
        }
        return graph;
    }

    public static void frame(Graph g) throws FileNotFoundException {
        final JButton b1;
        Layout<Integer, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300, 300));
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(350, 350));

        Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
            @Override
            public Paint transform(Integer i) {
                return Color.GREEN;
            }
        };

        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            @Override
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };

        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);


        final JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        b1 = new JButton("Выбрать граф");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == b1) {

                    try {
                        File file = getFile();
                        Graph<String, String> g = getGraph(file);
                        System.out.println("Количесвто вершин - "+g.getVertexCount());
                        System.out.println("Количество ребер - " + g.getEdgeCount());
                        double clust =  clustering(g);
                        System.out.println("Коэффициент кластреризации - " + clust);
                        frame(g);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });

        GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);

        p.setLayout(new BorderLayout());
        p.add(b1,BorderLayout.NORTH);
        p.add(panel);
        frame.add(p);

        frame.pack();
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public static File getFile() throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Graph files", "net");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        }
        return chooser.getSelectedFile().getCanonicalFile();
    }

}

