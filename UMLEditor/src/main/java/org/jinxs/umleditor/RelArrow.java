package org.jinxs.umleditor;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Line2D;

// Follows closely to custom painting tutorial provided in Oracle documentation
// https://docs.oracle.com/javase/tutorial/uiswing/painting/index.html
// by overriding a component's paintComponent method
public class RelArrow extends JPanel{
    //draw arrow globals
    final static float THICKNESS = 3;
    final static float DASH_ARRAY[] = {10};
    final static BasicStroke SOLID_STROKE = new BasicStroke(THICKNESS);
    final static BasicStroke DASHED_STROKE = new BasicStroke(THICKNESS, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, DASH_ARRAY, 0);

    final static int CAP_MAJOR_LENGTH = 16;
    final static int CAP_MINOR_LENGTH = 8;

    private JPanel panelFrom;
    private JPanel panelTo;
    private Point from = new Point();
    private Point to = new Point();

    private String relType;

    private boolean isHorizontal;
    private boolean isReverse;

    // Constructor
    // Builds the arrow from two panels and the rel type
    public RelArrow(JPanel panelFrom, JPanel panelTo, String relType)
    {
        this.panelFrom = panelFrom;
        this.panelTo = panelTo;
        this.relType = relType;

        from = new Point();
        to = new Point();

        calculateEndpoints();
    }

    // Finds the starting point and end point of the arrow based
    // on the panels' size, location on the JFrame, and relative
    // location to each other
    private void calculateEndpoints()
    {        
        int xDist = panelTo.getX() - panelFrom.getX();
        int yDist = panelTo.getY() - panelFrom.getY();

        if(Math.abs(xDist) > Math.abs(yDist)) {
            //Arrow mostly horizontal
            isHorizontal = true;
            if(xDist > 0) {
                //Left to right
                isReverse = false;
                from.x = panelFrom.getX() + panelFrom.getWidth();
                to.x = panelTo.getX();
            } else {
                //Right to left
                isReverse = true;
                from.x = panelFrom.getX();
                to.x = panelTo.getX() + panelTo.getWidth();

            }
            from.y = panelFrom.getY() + (panelFrom.getHeight() / 2);
            to.y = panelTo.getY() + (panelTo.getHeight() / 2);
        } else {
            //Arrow mostly vertical
            isHorizontal = false;
            if(yDist > 0) {
                //Top to bottom 
                isReverse = false;
                from.y = panelFrom.getY() + panelFrom.getHeight();
                to.y = panelTo.getY();
            } else {
                //Bottom to top
                isReverse = true;
                from.y = panelFrom.getY();
                to.y = panelTo.getY() + panelTo.getHeight();
            }
            from.x = panelFrom.getX() + (panelFrom.getWidth() / 2);
            to.x = panelTo.getX() + (panelTo.getWidth() / 2);
        }
    }

    // Draws the arrow head
    private void drawCap(Graphics2D g2d, boolean isDiamond, String relType)
    {
        Polygon cap = new Polygon();
        cap.addPoint(to.x, to.y);
        if (isHorizontal) {
            if (isReverse) {
                cap.addPoint(to.x + CAP_MAJOR_LENGTH, to.y - CAP_MINOR_LENGTH);
                if (isDiamond) {
                    cap.addPoint(to.x + (CAP_MAJOR_LENGTH * 2), to.y);
                }
                cap.addPoint(to.x + CAP_MAJOR_LENGTH, to.y + CAP_MINOR_LENGTH);
            } else {
                cap.addPoint(to.x - CAP_MAJOR_LENGTH, to.y - CAP_MINOR_LENGTH);
                if (isDiamond) {
                    cap.addPoint(to.x - (CAP_MAJOR_LENGTH * 2), to.y);
                }
                cap.addPoint(to.x - CAP_MAJOR_LENGTH, to.y + CAP_MINOR_LENGTH);
            }
        } else {
            if (isReverse) {
                cap.addPoint(to.x + CAP_MINOR_LENGTH, to.y + CAP_MAJOR_LENGTH);
                if (isDiamond) {
                    cap.addPoint(to.x, to.y + (CAP_MAJOR_LENGTH * 2));
                }
                cap.addPoint(to.x - CAP_MINOR_LENGTH, to.y + CAP_MAJOR_LENGTH);
            } else {
                cap.addPoint(to.x + CAP_MINOR_LENGTH, to.y - CAP_MAJOR_LENGTH);
                if (isDiamond) {
                    cap.addPoint(to.x, to.y - (CAP_MAJOR_LENGTH * 2));
                }
                cap.addPoint(to.x - CAP_MINOR_LENGTH, to.y - CAP_MAJOR_LENGTH);
            }
        }
        g2d.draw(cap);
        g2d.setColor(Color.BLUE);
        if (relType.equals("realization") || relType.equals("composition")) {
            g2d.setColor(Color.BLACK);
        }
        if (relType.equals("aggregation") || relType.equals("inheritance"))
            g2d.fill(cap);
    }

    // Overrides the paintComponent method inherited from JPanel to draw the
    // Graphics2D object on a component that can be added to the JFrame
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.BLUE);
        if (relType.equals("realization") || relType.equals("composition")) {
            g2d.setStroke(DASHED_STROKE);
            g2d.setColor(Color.BLACK);
        }
        else {
            g2d.setStroke(SOLID_STROKE);
        }

        boolean isDiamond = (relType.equals("aggregation") || relType.equals("composition"));

        if (panelFrom == panelTo) {
            int offsetX = from.x + panelFrom.getWidth() / 2;
            int offsetY = to.y - panelFrom.getHeight() / 2;
            g2d.draw(new Line2D.Float(from.x, from.y, offsetX, from.y));
            g2d.draw(new Line2D.Float(offsetX, from.y, offsetX, offsetY));
            g2d.draw(new Line2D.Float(offsetX, offsetY, to.x, offsetY));
            g2d.draw(new Line2D.Float(to.x, offsetY, to.x, to.y));
        } else {
            if (isHorizontal) {
                int midwayX = (from.x + to.x) / 2;
                g2d.draw(new Line2D.Float(from.x, from.y, midwayX, from.y));
                g2d.draw(new Line2D.Float(midwayX, from.y, midwayX, to.y));
                g2d.draw(new Line2D.Float(midwayX, to.y, to.x, to.y));
            } else {
                int midwayY = (from.y + to.y) / 2;
                g2d.draw(new Line2D.Float(from.x, from.y, from.x, midwayY));
                g2d.draw(new Line2D.Float(from.x, midwayY, to.x, midwayY));
                g2d.draw(new Line2D.Float(to.x, midwayY, to.x, to.y));
            }
        }
        g2d.setStroke(SOLID_STROKE);
        drawCap(g2d, isDiamond, relType);
    }
}
