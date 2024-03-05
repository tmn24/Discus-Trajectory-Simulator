import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Arrays;
import java.awt.image.BufferedImage;
public class Panel extends JPanel{
   int cW = 600, cH = 600;
   BufferedImage bi = new BufferedImage(cW,cH,BufferedImage.TYPE_INT_RGB);
   double dt = 1/1000.0;
   double scale = 1/10.0;
   Instant start, end;
   boolean end1 = false;
   boolean start1 = false;
   int noiDiscuses = 10;
   int nojDiscuses = 10;
   int nokDiscuses = 10;
   Discus[][][] disc = new Discus[noiDiscuses][nojDiscuses][nokDiscuses];
   
   double[][][] dist = new double[disc.length][disc[0].length][disc[0][0].length];
   double[][][] times = new double[disc.length][disc[0].length][disc[0][0].length];
   double time = 0;
   Panel(){
      /*for(int i = 0; i < disc.length; i++){
         disc[i] = new Discus(0,39,20,0,90*(i/(double)disc.length),1.6);
         disc[i].giveValues(cW,cH,scale);
         disc[i].toJava();
      }*/
      
      //Customizable code meant to change the initial conditions of each discus.
      for(int i = 0; i < disc.length; i++){
         for(int j = 0; j < disc[i].length; j++){
            for(int k = 0; k < disc[i][j].length; k++){
               //These 3 lines of code describe different parameter sets I made
               disc[i][j][k] = new Discus(0,1.7,20*(i/(double)disc.length),90*(j/(double)disc[i].length),90*(k/(double)disc[i][j].length),1.6);
               //disc[i][j][k] = new Discus(0,1.5,18,90*i/(double)disc.length,90*i/(double)disc.length,1.6);
               //disc[i][j][k] = new Discus(0,1.5,18,30.5,30.5,1.6);
               disc[i][j][k].giveValues(cW,cH,scale);
               disc[i][j][k].toJava();
            }
         }
      }
      /*
      disc[0][0] = new Discus(0,1.5,25.8,36,20.1,1.6);
      disc[0][0].giveValues(cW,cH,scale);
      disc[0][0].toJava();
      */
      JFrame frame = new JFrame("Simulation");
      frame.add(this);
      frame.setPreferredSize(new Dimension(cW,cH));
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      while(!end1){
         try{
            if(!start1){
               start = Instant.now();
            }
            start1 = true;
            Thread.sleep(2);
            repaint();
         }catch(Exception e){}
      }
   }
   public void paint(Graphics g){
      boolean bool = true;
      if(start1){
      g.setColor(Color.BLACK);
      end = Instant.now();
      super.paint(g);
      g.drawLine(0,cH-50,cW,cH-50);
      try{
         //dt = 1/1000.0;//(double)Duration.between(start,end).toMillis()/1000;
         dt = (double)Duration.between(start,end).toMillis()/1000;
      } catch(Exception e){
         dt = 1/1000.0;
      }
      start = Instant.now();
      time += dt;
      //System.out.println(dt);
      for(int i = 0; i < disc.length; i++){
         for(int j = 0; j < disc[i].length; j++){
            for(int k = 0; k < disc[i][j].length; k++){
               disc[i][j][k].timeStep(dt);
               disc[i][j][k].display(g);
               disc[i][j][k].drawLine(g);
               
               
               /*if(disc[i].ax > 0){
                  g.fillRect(disc[i].javaX,disc[i].javaY,(int)disc[i].ax,10);
               } else {
                  g.setColor(Color.RED);
                  g.fillRect(disc[i].javaX-(int)disc[i].ax,disc[i].javaY,(int)disc[i].ax,10);
               }*/
               //start = Instant.now();
               if(disc[i][j][k].y >= 0){
                  bool = false;
                  //System.out.println(end1);
               } else if(dist[i][j][k] == 0){
                  dist[i][j][k] = disc[i][j][k].x;
                  times[i][j][k] = time;
               }
            }
         }
      }
      if(bool){
         end1 = true;
         System.out.println(Arrays.deepToString(dist));
         System.out.println(Arrays.deepToString(times));
         double max = 0;
         int index1 = 0, index2 = 0, index3 = 0;
         double[] bestMaxes = new double[disc.length];
         int[][] bestIndices = new int[disc.length][2];
         for(int i = 0; i < disc.length; i++){
            for(int j = 0; j < disc[i].length; j++){
               for(int k = 0; k < disc[i][j].length; k++){
                  if(dist[i][j][k] > max){
                     max = dist[i][j][k];
                     index1 = i;
                     index2 = j;
                     index3 = k;
                  }
                  if(dist[i][j][k] > bestMaxes[i]){
                     bestMaxes[i] = dist[i][j][k];
                     bestIndices[i][0] = j;
                     bestIndices[i][1] = k;
                  }
               }   
            }
         }
         //System.out.println(max+","+index1*90/(double)disc.length+","+index2*90/(double)disc[0].length);
         //System.out.println(max+","+(15+index1*15/(double)disc.length));
         System.out.println("Best Indicies:"+Arrays.deepToString(bestIndices));
         System.out.println("bestMaxes:"+Arrays.toString(bestMaxes));
         System.out.println(max+","+index1+","+index2+","+index3);
      } else {
         end1 = false;
      }
      }
   }
   public static void main(String[] args){
      Panel p = new Panel();
   }
}