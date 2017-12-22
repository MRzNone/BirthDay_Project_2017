import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import Acme.*;
import objectdraw.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class GUI extends WindowController implements ChangeListener
{
  private static final int ALL_WIDTH = 1100;
  private static final int ALL_HEIGHT = 900;
  private static final int WINDOW_SIZE = 900;

  private boolean ifDraw = true;
  private boolean ifAll = true;

  private JPanel rightPanel = new JPanel();
  private JPanel buttonCollection = new JPanel();
  private JButton resetButton = new JButton("Reset");
  private JButton drawSelectButton = new JButton("Draw");
  private JButton allSingleButton = new JButton("All");
  private JSlider RSlider = new JSlider(0, 255, 127);
  private JSlider GSlider = new JSlider(0, 255, 127);
  private JSlider BSlider = new JSlider(0, 255, 127);
  private JButton generateButton = new JButton("Generate");

  // private static final double MM_TO_PIXEL = 5.08;
  private static final double MM_TO_PIXEL = 1.2;
  private static final int NUM_OF_ORBS = 15;
  private static final double FIRST_OFFSET = 15;
  private static final double RADIUS_INCRE = 20;

  private ArrayList<Orb> orbs = new ArrayList<Orb>();

  private FilledOval colorOrb;

  

  public void begin()
  {
    // initialize GUI
    setUpGUI();
    setUpOrbs();
  }



  public void setUpOrbs()
  {
    double centerX = canvas.getWidth()/2.0;
    double centerY = canvas.getHeight()/2.0;

    double radius = FIRST_OFFSET;
    int i = 0;
    while(i < NUM_OF_ORBS)
    {
      Orb o = new Orb(centerX, centerY, radius * MM_TO_PIXEL, canvas,
          this, drawSelectButton);
      orbs.add(o);
      radius += RADIUS_INCRE;
      i++;

      //add mouse listeners
      canvas.addMouseListener(o);
      canvas.addMouseMotionListener(o);

      //add button listeners
      resetButton.addActionListener(o);
      drawSelectButton.addActionListener(o);
      allSingleButton.addActionListener(o);
      generateButton.addActionListener(o);
      RSlider.addChangeListener(o);
      GSlider.addChangeListener(o);
      BSlider.addChangeListener(o);
    }
    orbs.get(0).updateColor();
  }


  
  public void stateChanged(ChangeEvent evt)
  {
    colorOrb.setColor(new Color(getR(), getG(), getB()));
    orbs.get(0).updateColor();
  }


  
  public int getR()
  {
    return RSlider.getValue();
  }



  public int getG()
  {
    return GSlider.getValue();
  }



  public int getB()
  {
    return BSlider.getValue();
  }



  public void setUpGUI()
  {
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); 
    buttonCollection.add(resetButton);
    buttonCollection.add(drawSelectButton);
    rightPanel.add(buttonCollection);
    rightPanel.add(allSingleButton);
    rightPanel.add(RSlider);
    rightPanel.add(GSlider);
    rightPanel.add(BSlider);
    rightPanel.add(generateButton);

    this.add(rightPanel, BorderLayout.EAST);
    this.validate();

    RSlider.addChangeListener(this);
    GSlider.addChangeListener(this);
    BSlider.addChangeListener(this);

    // set up listeners
    generateButton.addActionListener(new GenerateListener());
    drawSelectButton.addActionListener(new DrawSelectHandler());
    allSingleButton.addActionListener(new AllSingleHandler());

    colorOrb = new FilledOval(20, 20, 30, 30, canvas);
    colorOrb.setColor(new Color(getR(), getG(), getB()));
  }

  public static void main(String[] args)
  {
    new Acme.MainFrame(new GUI(), ALL_WIDTH, ALL_HEIGHT); 
  }

  
  private class DrawSelectHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent evt)
    {
      ifDraw = !ifDraw;

      if(resetButton.getText().equals("Reset"))
      {
        resetButton.setText("Delete");
      }
      else
      {
        resetButton.setText("Reset");
      }

      if(drawSelectButton.getText().equals("Draw"))
      {
        drawSelectButton.setText("Select");
      }
      else
      {
        drawSelectButton.setText("Draw");
      }
    }
  }



  private class AllSingleHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent evt)
    {
      ifAll = !ifAll;

      if(allSingleButton.getText().equals("All"))
      {
        allSingleButton.setText("Single");
      }
      else
      {
        allSingleButton.setText("All");
      }
    }
  }



  private class GenerateListener implements ActionListener
  {
    int[][][] rgbs = new int[NUM_OF_ORBS][Orb.LOOP_CYCLE][3];

    public void actionPerformed(ActionEvent evt)
    {
      int i = 0;
      // get RGB array
      for(Orb o : orbs)
      {
        rgbs[i++] = o.getRGB();
      }

      int[][][] temp = new int[NUM_OF_ORBS][Orb.LOOP_CYCLE][3];

      // reorder
      int headIndex = 0;
      int tailIndex = NUM_OF_ORBS - 1;
      for (i = 0 ; i < NUM_OF_ORBS; i++)
      {
        // odd
        if (i % 2 == 1)
        {
          temp[headIndex++] = rgbs[i];
        }
        // even
        else
        {
          temp[tailIndex--] = rgbs[i];
        }
      }
      rgbs = temp;
      
      // export to file
      // open the file

      byte[] rgb = new byte[3];
      try
      {
        OutputStream writer = new FileOutputStream("export");
        
        int cycles = Orb.LOOP_CYCLE;

        // loop cycle
        for (int cycle = 0; cycle < cycles; cycle++)
        {
          // loop orbs
          for (int o = 0; o < NUM_OF_ORBS; o++)
          {
            // write rgb in byte
            rgb[0] = (byte) rgbs[o][cycle][0];
            rgb[1] = (byte) rgbs[o][cycle][1];
            rgb[2] = (byte) rgbs[o][cycle][2];
            writer.write(rgb);
          }
        }

        writer.close();
      }
      catch (IOException e)
      {
      }

      /*
      PrintWriterdd writer;

      try
      {
        writer = new PrintWriter("export");

        // append each moment
        // loop orbs
        writer.println("int name[15][345][3] =");
        writer.println("{");
        int rgb[] = new int[3];

        int cycles = Orb.LOOP_CYCLE;

        for (int o = 0; o < NUM_OF_ORBS; o++)
        {
          writer.print("  {");
          // loop cycles
          for(int cycle = 0; cycle < cycles; cycle++)
          {
            rgb = rgbs[o][cycle];
            writer.printf("{%d,%d,%d}", rgb[0], rgb[1],rgb[2]);
            // print comma if not last one
            if (cycle < cycles - 1)
            {
              writer.print(",");
            }
          }
          writer.print("}");
          // print comma if not last one
          if (o < NUM_OF_ORBS - 1)
          {
            writer.print(",");
          }
          writer.println("");
        }
        writer.println("};");

        writer.close();
      }
      catch(FileNotFoundException e)
      {
      }
      */
    }
  }
}
