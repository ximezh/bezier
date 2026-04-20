/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graficacion;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author GenaroML
 */
public class JPanelDibujo extends JPanel implements MouseMotionListener {

    double[] Px, Py;
    int n=4, n1, w, h, h1, w2;
    boolean initiated = false;

    public JPanelDibujo() {
        super();        
    }
  

    public void init(){
        n = 5;
        n1 = n + 1;
        h = this.getHeight();
        w = this.getWidth();

        h1 = h - 1;
        w2 = w ;

        Px = new double[n];
        Py = new double[n];
        Px[0] = .1 * w2;
        Px[1] = .1 * w2;
        Px[2] = .5 * w2;
        Px[3] = .9 * w2;
        Px[4] = .9 * w2;
        Py[0] = .1 * h1;
        Py[1] = .9 * h1;
        Py[2] = .9 * h1;
        Py[3] = .9 * h1;
        Py[4] = .1 * h1;

        addMouseMotionListener(this);
        initiated = true;

    }
    public void drawFun(Graphics2D g2d) {
        double step = 1. / w2, t = step;
        double[] B = new double[n1], Bo = new double[n1], Bold = new double[n1];
        B[1] = Bo[1] = h1;
        Color[] iColor = {Color.gray, Color.red, new Color(0f, .7f, 0f),
            Color.blue, Color.magenta, new Color(0f, .8f, .8f), new Color(.9f, .9f, 0f)};
        for (int k = 1; k < w2; k++) {
            System.arraycopy(B, 1, Bold, 1, n);
            System.arraycopy(Bo, 1, B, 1, n);

            for (int j = 1; j < n; j++) //  basis functions calculation
            {
                for (int i = j + 1; i > 0; i--) {
                    B[i] = (1 - t) * B[i] + t * B[i - 1];
                }
            }

            for (int m = 1; m <= n; m++) {
                g2d.setColor(iColor[m % 7]);
                g2d.drawLine(w2 + k - 1, h1 - (int) Bold[m], w2 + k, h1 - (int) B[m]);
            }
            t += step;
        }
    }

    public void drawSpline(Graphics2D g2d) {
        double step = 1. / w2, t = step;
        double[] Pxi = new double[n], Pyi = new double[n];
        int X, Y, Xold = (int) Px[0], Yold = h1 - (int) Py[0];

        g2d.setColor(Color.blue);
        for (int i = 0; i < n; i++) {
            X = (int) Px[i];
            Y = h1 - (int) Py[i];
            g2d.drawRect(X - 1, Y - 1, 3, 3);
        }
        if (n > 2) {
            int Xo = Xold, Yo = Yold;
            for (int i = 1; i < n; i++) {
                X = (int) Px[i];
                Y = h1 - (int) Py[i];
                g2d.drawLine(Xo, Yo, X, Y);
                Xo = X;
                Yo = Y;
            }
        }
        g2d.setColor(Color.red);
        for (int k = 1; k < w2; k++) {
            System.arraycopy(Px, 0, Pxi, 0, n);
            System.arraycopy(Py, 0, Pyi, 0, n);

            for (int j = n - 1; j > 0; j--) //  points calculation
            {
                for (int i = 0; i < j; i++) {
                    Pxi[i] = (1 - t) * Pxi[i] + t * Pxi[i + 1];
                    Pyi[i] = (1 - t) * Pyi[i] + t * Pyi[i + 1];
                }
            }

            X = (int) Pxi[0];
            Y = h1 - (int) Pyi[0];
            g2d.drawLine(Xold, Yold, X, Y);
            Xold = X;
            Yold = Y;
            t += step;
        }
    }

    protected void clear(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void paintComponent(Graphics g) {

        if (initiated){
        clear(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);

        drawSpline(g2d);
        }

    }

    public void destroy() {
        removeMouseMotionListener(this);
    }

    public void mouseMoved(MouseEvent e) {
    }  //1.1 event handling

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        if (x < 0) {
            x = 0;
        }
        if (x > w2 - 3) {
            x = w2 - 3;
        }
        int y = h1 - e.getY();
        if (y < 0) {
            y = 0;
        }
        if (y > h1) {
            y = h1;
        }
        int iMin = 0;
        double Rmin = 1e10, r2, xi, yi;
        for (int i = 0; i < n; i++) {
            xi = (x - Px[i]);
            yi = (y - Py[i]);
            r2 = xi * xi + yi * yi;
            if (r2 < Rmin) {
                iMin = i;
                Rmin = r2;
            }
        }
        Px[iMin] = x;
        Py[iMin] = y;

        drawSpline((Graphics2D) this.getGraphics());
        repaint();
    }


    @Override
    public void repaint(){
        super.repaint();

        h = this.getHeight();
        w = this.getWidth();

        h1 = h - 1;
        w2 = w ;
    }
}
