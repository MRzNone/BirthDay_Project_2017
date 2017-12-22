import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import Acme.*;
import objectdraw.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;



public class Orb extends ActiveObject
  implements ActionListener, ChangeListener,
       MouseMotionListener, MouseListener
{
  private double centerX;
  private double centerY;
  private Location center;
  private double radius;
  private DrawingCanvas canvas;
  private GUI gui;

  private static Color color;

  private FramedOval orb;
  private ArrayList<Dot> dots;
  
  private static boolean ifDraw = true;
  private static int currentR;
  private static int currentG;
  private static int currentB;

  private static JButton drawSelectButton;

  private static final int DRAWING_BEAR = 1;

  public static final int LOOP_CYCLE = 345;
  private boolean[] positions;
  private static double radIncre;
  private int[][] rgbs = new int[LOOP_CYCLE][3];



  public Orb(double cX, double cY, double r, DrawingCanvas canvas,
      GUI gui, JButton button)
  {
    radIncre = Math.toRadians(360.0/LOOP_CYCLE);
    positions = new boolean[LOOP_CYCLE];

    centerX = cX;
    centerY = cY;
    center = new Location(centerX, centerY);
    radius = r;
    this.canvas = canvas;
    this.gui = gui;

    drawSelectButton = button;

    dots = new ArrayList<Dot>();

    // draw the circle
    orb = new FramedOval(centerX - radius, centerY - radius,
        radius * 2, radius * 2, canvas);
  }



  public void actionPerformed(ActionEvent evt)
  {
    String name = evt.getActionCommand();
    switch(name)
    {
      case "Delete":
        delete();
        break;
      case "Reset":
        reset();
        break;
      case "Draw":
        ifDraw = false;
        break;
      case "Select":
        ifDraw = true;
        break;
      case "All":
        for(Dot dot : dots)
        {
          dot.setIfSelected(true);
        }
        break;
      case "Single":
        for(Dot dot : dots)
        {
          dot.setIfSelected(false);
        }
        break;
      case "Generate":
        break;
    }
  }

  

  public void delete()
  {
    Dot dot;
    for (int i = 0; i < dots.size();)
    {
      dot = dots.get(i);
      if(dot.getIfSelected())
      {
        dot.destroy();
        dots.remove(i);
      }
      else
      {
        i++;
      }
    }
  }



  public void reset()
  {
    for (Dot dot : dots)
    {
      dot.destroy();
    }
    dots.clear();
  }


  
  public void stateChanged(ChangeEvent evt)
  {
    if (!ifDraw)
    {
      for (Dot dot : dots)
      {
        if(dot.getIfSelected())
        {
          dot.setColor(color);
          int index = dot.getIndex();
          rgbs[index][0] = color.getRed(); 
          rgbs[index][1] = color.getBlue(); 
          rgbs[index][2] = color.getGreen(); 
        }
      }
    }
  }

  

  public void updateColor()
  {
    int R = gui.getR();
    int G = gui.getG();
    int B = gui.getB();
    color = new Color(R, G, B);
  }


  
  public void mouseClicked(MouseEvent evt)
  {
  }


  
  public void mouseEntered(MouseEvent evt)
  {
  }


  
  public void mouseExited(MouseEvent evt)
  {
  }


  
  public void mousePressed(MouseEvent evt)
  {
  }


  
  public void mouseReleased(MouseEvent evt)
  {
  }



  public int[][] getRGB()
  {
    return rgbs;
  }

  
  public void mouseDragged(MouseEvent evt)
  {
    Location pt = new Location(evt.getPoint());

    if(ifDraw && orb.contains(pt) &&
        (radius - pt.distanceTo(center)) < DRAWING_BEAR)
    {
      int index = roundToPos(pt);
      if (positions[index] == false)
      {
        // create new dot
        Location loc = posToLocation(index);
        Dot dot = new Dot(loc.getX(), loc.getY(), canvas, color, index);
        dots.add(dot);

        // add listeners
        drawSelectButton.addActionListener(dot);
        canvas.addMouseListener(dot);

        // record
        positions[index] = true;
       
        rgbs[index][0] = color.getRed(); 
        rgbs[index][1] = color.getBlue(); 
        rgbs[index][2] = color.getGreen(); 
      }
    }
  }


  
  private Location posToLocation(int pos)
  {
    double radian = pos * radIncre - Math.PI / 2.0;

    double xLoc = radius * Math.cos(radian) + center.getX();
    double yLoc = radius * Math.sin(radian) + center.getY();

    return new Location(xLoc, yLoc);
  }


  
  private int roundToPos(Location pt)
  {
    // assume rotate counterclockwise from holder
    double xInterval = pt.getX() - center.getX();
    double yInterval = pt.getY() - center.getY();
    double tan = yInterval / xInterval;
    double radian = Math.atan(tan);


    if (xInterval < 0)
    {
      radian = 3.0/2 * Math.PI + radian;
    }
    else
    {
      radian += Math.PI/2.0;
    }
    

    int index = (int) (radian / radIncre);
    double remainer = radian % radIncre;

    if(remainer > radIncre/2.0)
    {
      index++;
    }

    if(index == positions.length)
    {
      index = 0;
    }
    
    return index;
  }


  
  public void mouseMoved(MouseEvent evt)
  {
  }
}
