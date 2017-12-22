import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import Acme.*;
import objectdraw.*;
import java.awt.Color;
import java.awt.event.*;



public class Dot implements ActionListener, MouseListener
{
  private double centerX;
  private double centerY;
  private static final double RADIUS = 10;
  private DrawingCanvas canvas;

  private Color color;
  private boolean exist = true;

  private FilledOval dot;
  private FramedRect frame;

  private boolean ifSelected;
  private boolean ifDraw = true;
  private int index;


  
  public Dot(double cX, double cY, DrawingCanvas canvas, Color c, int i)
  {
    centerX = cX;
    centerY = cY;
    this.canvas = canvas;
    color = c;
    index = i;

    dot = new FilledOval(centerX - RADIUS, centerY - RADIUS,
        RADIUS * 2, RADIUS * 2, canvas);
    dot.setColor(c);
  }


  
  public void mouseClicked(MouseEvent evt)
  {
    Location pt = new Location(evt.getPoint());
    if (!ifDraw)
    {
      if(dot.contains(pt))
      {
        setIfSelected(!ifSelected);
      }
    }
  }



  public void actionPerformed(ActionEvent evt)
  {
    ifDraw = !ifDraw;
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



  // report
  public double getX()
  {
    return centerY;
  }


  
  public int getIndex()
  {
    return index;
  }
  


  public double getY()
  {
    return centerY;
  }



  public Color getColor()
  {
    return color;
  }


  
  public void setColor(Color c)
  {
    color = c;
    dot.setColor(color);
  }

  

  public boolean getIfSelected()
  {
    return ifSelected;
  }


  
  public void setIfSelected(boolean update)
  {
    ifSelected = update;
    
    if (ifSelected)
    {
      if(frame == null)
      {
        frame = new FramedRect(centerX - RADIUS - 5, centerY - RADIUS - 5,
            RADIUS * 2 + 10, RADIUS * 2 + 10, canvas);
      }
    }
    else
    {
      if (frame != null)
      {
        frame.removeFromCanvas();
        frame = null;
      }
    }
  }



  // destroy itself
  public void destroy()
  {
    if (exist)
    {
      dot.removeFromCanvas();
      exist = false;
      if (frame != null)
      {
        frame.removeFromCanvas();
        frame = null;
      }
    }
  }
}
