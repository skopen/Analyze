import java.awt.*;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This program demonstrates how to draw lines using Graphics2D object.
 * @author www.codejava.net
 *
 */
public class ChartDrawer extends JFrame {

    Ticker[] prices = null;

    public ChartDrawer() {
        super("S&P Data Chart");

        setSize(2000, 1400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    void drawLines(Graphics g) {

        if (prices == null) {
            Strategy runner = new Strategy();
            runner.readData(true);
            prices = runner.processData();

            double minTime = Double.MAX_VALUE, maxTime = Double.MIN_VALUE;
            double minPrice = Double.MAX_VALUE, maxPrice = Double.MIN_VALUE;

            for (int i = 0; i < prices.length; i++) {
                if (prices[i].price < minPrice)
                    minPrice = prices[i].price;
                if (prices[i].price > maxPrice)
                    maxPrice = prices[i].price;
                if (prices[i].time < minTime)
                    minTime = prices[i].time;
                if (prices[i].time > maxTime)
                    maxTime = prices[i].time;
            }

            // Point.setScales((int) (minTime * 0.98), (int) (maxTime * 1.02), ((int) minPrice * 0.98), (int) (maxPrice * 1.02));
            Point.setScales((int) (minTime - 1), (int) (maxTime + 1), ((int) minPrice - 1), (int) (maxPrice + 1));
        }

        Graphics2D g2d = (Graphics2D) g;

        g2d.setBackground(Color.BLACK);
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5f));

        g2d.drawLine(Point.x(Point.SCALEX1), Point.y(Point.SCALEY1), Point.x(Point.SCALEX1), Point.y(Point.SCALEY2));
        g2d.drawLine(Point.x(Point.SCALEX1), Point.y(Point.SCALEY2), Point.x(Point.SCALEX2), Point.y(Point.SCALEY2));
        g2d.drawLine(Point.x(Point.SCALEX2), Point.y(Point.SCALEY2), Point.x(Point.SCALEX2), Point.y(Point.SCALEY1));
        g2d.drawLine(Point.x(Point.SCALEX2), Point.y(Point.SCALEY1), Point.x(Point.SCALEX1), Point.y(Point.SCALEY1));


        g2d.setStroke(new BasicStroke(3f));

        for (int i = 1; i < prices.length; i++)
        {
            if (prices[i-1].trend == Ticker.UP)
            {
                g2d.setColor(Color.GREEN);
            }
            else if (prices[i-1].trend == Ticker.DOWN)
            {
                g2d.setColor(Color.RED);
            }
            else
            {
                g2d.setColor(Color.BLACK);
            }

            g2d.drawLine(Point.x(prices[i-1].time), Point.y(prices[i-1].price), Point.x(prices[i].time), Point.y(prices[i].price));

            g2d.setColor(Color.BLUE);
            if (prices[i].inflection == Ticker.DOWN || prices[i].inflection == Ticker.UP) {
                g2d.drawOval(Point.x(prices[i].time)-5, Point.y(prices[i].price)-5, 10, 10);
               // g2d.drawString(prices[i].date + ": " + Double.toString(prices[i].price), Point.x(prices[i].time) + 10, Point.y(prices[i].price)-5);
            }
//            else
//            {
//                g2d.drawString(prices[i].date + ": " + Double.toString(prices[i].price), Point.x(prices[i].time) + 10, Point.y(prices[i].price)-5);
//            }

            if (prices[i].localMinMax != null)
            {
              //  g2d.drawString(prices[i].localMinMax + ": " + Double.toString(prices[i].price), Point.x(prices[i].time) + 10, Point.y(prices[i].price)-5);
            }
        }

        g2d.setStroke(new BasicStroke(5f));
        g2d.setColor(Color.BLUE);
        int lastInflex = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i].inflection == Ticker.DOWN || prices[i].inflection == Ticker.UP) {
                g2d.drawLine(Point.x(prices[lastInflex].time), Point.y(prices[lastInflex].price), Point.x(prices[i].time), Point.y(prices[i].price));
                lastInflex = i;

                g2d.drawString("CAP: " + Integer.toString((int)prices[i].capital), Point.x(prices[i].time) + 10, Point.y(prices[i].price)-5);
            }
        }
        g2d.drawLine(Point.x(prices[lastInflex].time), Point.y(prices[lastInflex].price), Point.x(prices[prices.length-1].time), Point.y(prices[prices.length-1].price));
        g2d.drawString("CAP: " + Integer.toString((int)prices[0].capital), Point.x(prices[0].time) + 10, Point.y(prices[0].price)-5);
        g2d.drawString("CAP: " + Integer.toString((int)prices[prices.length-1].capital), Point.x(prices[prices.length-1].time) + 10, Point.y(prices[prices.length-1].price)-5);

        double cap = prices[0].capital * (prices[prices.length-1].price/prices[0].price);
        g2d.drawString("BASE CAP: " + Integer.toString((int)cap), Point.x(prices[prices.length-1].time) + 10, Point.y(prices[prices.length-1].price)+15);

//        g2d.setColor(Color.GREEN);
//        for (int i = 0; i < prices.length; i++)
//        {
//            if (prices[i].trend == Ticker.UP)
//            {
//                g2d.drawOval(Point.x(prices[i].time), Point.y(prices[i].price), 2, 2);
//            }
//        }
//
//        g2d.setColor(Color.RED);
//        for (int i = 0; i < prices.length; i++)
//        {
//            if (prices[i].trend == Ticker.DOWN)
//            {
//                g2d.drawOval(Point.x(prices[i].time), Point.y(prices[i].price), 2, 2);
//            }
//
//        }


//        for (int i = 20; i < 500; i+=5) {
//            g2d.setColor(Color.BLUE);
//            g2d.setStroke(new BasicStroke(6f));
//            g2d.drawOval(i, i, 3, 3);
//        }

        //g2d.drawLine(120, 50, 360, 50);

        //g2d.draw(new Line2D.Double(59.2d, 99.8d, 419.1d, 99.8d));

        //g2d.draw(new Line2D.Float(21.50f, 132.50f, 459.50f, 132.50f));
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChartDrawer().setVisible(true);
            }
        });
    }
}
