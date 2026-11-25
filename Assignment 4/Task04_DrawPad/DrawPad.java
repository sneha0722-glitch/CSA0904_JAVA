import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawPad extends Frame {
    private final List<Point> points = new ArrayList<>();
    private final Label status = new Label("Clicks: 0");

    public DrawPad() {
        super("Draw Pad");
        add(status, BorderLayout.SOUTH);
        addMouseListener(new MouseAdapter() {
            // TODO 1 completed: correct method name and signature.
            @Override public void mouseClicked(MouseEvent e) {
                points.add(e.getPoint());
                status.setText("Clicks: " + points.size());
                repaint();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(400, 300);
    }

    @Override public void paint(Graphics g) {
        // TODO 2 completed: draw every stored point centred at its click location.
        for (Point p : points) g.fillOval(p.x - 3, p.y - 3, 6, 6);
        status.setText("Clicks: " + points.size());
    }

    public static void main(String[] args) { new DrawPad().setVisible(true); }
}
/*
The original method was mouseclicked (wrong case), so it did not override
MouseAdapter.mouseClicked and the callback was never invoked. The label was
never updated. Minimise and restore after clicking: direct getGraphics drawing
is not retained by AWT repainting. This version stores points and redraws them.
@Override makes a misspelled/non-overriding method a compile-time error.
Alternative: draw to a BufferedImage and blit once per repaint. Point list:
O(n) draw time per repaint and O(n) point storage. BufferedImage: O(width*height)
pixel storage and roughly O(width*height) image copy per repaint, but incremental
drawing can avoid redrawing all individual points.
*/