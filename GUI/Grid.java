import java.util.*;
import java.lang.Math;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Component;

public class Grid extends JPanel {

  public ArrayList<GridPanel> gridPanels;
  public Dimension size;
  private int panelsVertical;
  private int panelsHorizontal;
  private GridBagConstraints c;
  protected static int init_squareSize;
  private Ch_Color homeColor = Ch_Color.RED; //user determined
  private Ch_Color awayColor = Ch_Color.BLACK; //user determined
  public ArrayList<Checker> homeCheckers;
  public ArrayList<Checker> awayCheckers;
  public int[] checkerLocations={0,2,4,6,8,10,12,14};

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  public Boolean validGrid(GridPanel gp) {
  return true; //TODO 4 later
  }

  public GridPanel getGrid(Point p) {
    int defaultIndex=1;
  for(int i=0;i<gridPanels.size();i++) {
    Rectangle bounds = gridPanels.get(i).getBounds();
    if ((p.x>=bounds.x &&
         p.x<=bounds.x+bounds.width) &&
        (p.y>=bounds.y &&
         p.y<=bounds.y+bounds.height)) {
      return gridPanels.get(i);
    }
  }
  return gridPanels.get(defaultIndex);
  }

  public void handleCheckerEvent(Ch_Event e, byte parameters, int idx, Boolean away) {
  Checker c=(away)?awayCheckers.get(idx):homeCheckers.get(idx);
  //move:
  int idx_location = (int)parameters;
  moveChecker(c.gpIndex, idx_location);
  }

  public void updateGridPanels() {
    Component[] cp = getComponents();
    for(int e=0;e<cp.length;e++) {
          //System.out.println("OOOO:"+cp);
      remove(cp[e]);
    }
    for(int i=0;i<gridPanels.size();i++) {
      c.weightx=0.5;
      c.weighty=0.5;
      c.gridx=i%panelsHorizontal;
      c.gridy=(int)Math.floor((double)i/(double)panelsHorizontal);
      add(gridPanels.get(i),c);
      //System.out.println("Added gridPanel ["+i+"]   |   "+
                      //    c.gridx+", "+c.gridy+". Width:"+gridPanels.get(i).getWidth());
    }
    repaint();
    //System.out.println("OOOO:"+getComponents());
  }
  private void addCheckers() {
      for(int i=0;i<homeCheckers.size();i++){
        Checker hc = homeCheckers.get(i);
        Checker ac = awayCheckers.get(i);
        gridPanels.set(hc.gpIndex,hc);
        gridPanels.set(ac.gpIndex,ac);
        updateGridPanels();
      }
  }
  public void moveChecker(int idx_source, int idx_location) { //should throw error at some point
    GridPanel p = gridPanels.get(idx_source);
    gridPanels.set(idx_location, p);
    gridPanels.set(idx_source, new GridPanel(init_squareSize,
                                            gridPanels.get(idx_source).getBgColor(),
                                            idx_source));
    p.setIndex(idx_location);
    updateGridPanels();
  }
  public void moveChecker(GridPanel source, GridPanel dest) { //should throw error at some point
    gridPanels.set(dest.getIndex(), source);
    gridPanels.set(source.getIndex(), new GridPanel(init_squareSize,
                                                gridPanels.get(source.getIndex()).getBgColor(),
                                                source.getIndex()
                                              )
                                              );
    source.setIndex(dest.getIndex());
    updateGridPanels();
  }

  public Grid(int _panelsHorizontal,int _panelsVertical,int panelSize) {
    init_squareSize=panelSize;
    panelsHorizontal=_panelsHorizontal;
    panelsVertical=_panelsVertical;
    int nPanels = panelsHorizontal*panelsVertical;
    gridPanels = new ArrayList<GridPanel>();
    homeCheckers = new ArrayList<Checker>();
    awayCheckers = new ArrayList<Checker>();
    setLayout(new GridBagLayout());
    c=new GridBagConstraints();
    c.gridheight = 1;
    c.gridwidth = 1;
    c.fill=GridBagConstraints.BOTH;
    this.size=new Dimension(panelsHorizontal*panelSize,panelsVertical*panelSize);
    //setRatio(panelsHorizontal*panelSize,panelsVertical*panelSize);
    for(int i=0; i<nPanels; i++) {
      int j = i;
      if(Math.floor(i/panelsHorizontal)%2==0) {j++;}
      if(j%2==0) {
        gridPanels.add(new GridPanel(panelSize, Color.WHITE,gridPanels.size()));
      } else {
        gridPanels.add(new GridPanel(panelSize, Color.BLACK,gridPanels.size()));
      }
    }
      // if gamestarted then
      for(int ii=0;ii<checkerLocations.length;ii++){
        homeCheckers.add(new Checker(
                          init_squareSize, //size
                          Color.WHITE, //bg color
                          homeColor, //color
                          Ch_Type.PAWN, //type
                          checkerLocations[ii], //index within grid
                          this//panel object within grid
                            )
                        );
        awayCheckers.add(new Checker(
                            init_squareSize, //size
                            Color.WHITE, //bg color
                            awayColor,
                            Ch_Type.PAWN,
                            (gridPanels.size()-(checkerLocations[ii]+1)), //black checker indeces are reversed (opposite end of board)
                            this
                          )
                        );
      }
    addCheckers();
    updateGridPanels();
    setBackground(Color.RED);
    setPreferredSize(this.size);
    setSize(this.size);
    setMinimumSize(getMinimumSize());
  //  System.out.println(this.getClass().getSimpleName()+" "+this.getPreferredSize());
  //  System.out.println("DEBUG:Rendered Size: "+this.getClass().getSimpleName()+": "+this.getSize());
    revalidate();
    repaint();
  }
}
