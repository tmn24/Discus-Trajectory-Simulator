import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
import java.util.ArrayList;
public class Discus{
   double x, y, vx, vy, ax, ay, angle, mass;
   ArrayList<Integer> prevX = new ArrayList<Integer>();
   ArrayList<Integer> prevY = new ArrayList<Integer>();
   int cW, cH;
   double scale = 1;
   int javaX = 0, javaY = 0;
   double g = 9.8;
   ArrayList<Integer> X = new ArrayList<Integer>(10000);
   ArrayList<Integer> Y = new ArrayList<Integer>(10000);
   Discus(double x, double y, double v, double theta, double angle, double mass){
      this.x = x;
      this.y = y;
      vx = v*Math.cos(theta*Math.PI/180);
      vy = v*Math.sin(theta*Math.PI/180);
      this.angle = angle*Math.PI/180;
      this.mass = mass;
   }
   public void giveValues(int a, int b,double c){
      cW = a;
      cH = b;
      scale = c;
   }
   public void timeStep(double dt){
      x += vx*dt+ax*dt*dt*0.5;
      y += vy*dt+ay*dt*dt*0.5;
      vx += ax*dt;
      vy += ay*dt;
      double v = Math.sqrt(vx*vx+vy*vy);
      if(v == 0){
         ax = 0;
         ay = -g;
         return;
      }
      double travelAngle = Math.atan(vy/vx);
      double angleOfAttack = Math.PI/2-Math.abs(Math.PI/2-Math.abs((angle - travelAngle)));
      double CD = -0.986052/(1+Math.exp(-2.16849+0.0707458*angleOfAttack*180/Math.PI))+0.885848;
      double CL = 0;
      double wingArea = 0.22*0.22*Math.sin(angleOfAttack)*Math.PI;
      if(angleOfAttack*180/Math.PI > 30){
         CL = -0.0116014*angleOfAttack*180/Math.PI+1.04425;
      } else if(angleOfAttack*180/Math.PI <= 30){
         CL = 0.0301209*angleOfAttack*180/Math.PI;  
      }
      //System.out.println(CD);
      double drag = CD*wingArea*1.225*v*v*0.5;
      double lift = CL*wingArea*1.225*v*v*0.5;
      if(Math.abs(angle-travelAngle) < Math.PI/2){
         int mul = 1;
         if(angle-travelAngle < 0){
            mul = -1;
         }
         ax = (drag*Math.cos(travelAngle+Math.PI)+lift*Math.cos(travelAngle+mul*Math.PI/2))/mass;
         ay = -g+(drag*Math.sin(travelAngle+Math.PI)+lift*Math.sin(travelAngle+mul*Math.PI/2))/mass;
      } else { 
         int mul = 1;
         ax = (drag*Math.cos(travelAngle+Math.PI)+lift*Math.cos(travelAngle-mul*Math.PI/2))/mass;
         ay = -g+(drag*Math.sin(travelAngle+Math.PI)+lift*Math.sin(travelAngle-mul*Math.PI/2))/mass;
      }
   }
   public void toJava(){
      javaX = (int)(x/scale);
      javaY = (int)(cH-50-y/scale);
      //System.out.println(javaX+","+javaY);
   }
   public void drawLine(Graphics g){
      g.setColor(Color.RED);
      if(X.indexOf(javaX) == -1 || Y.indexOf(javaY) == -1|| Y.get(X.indexOf(javaX)) != javaY || X.get(Y.indexOf(javaY)) != javaX){
         X.add(javaX);
         Y.add(javaY);
      }
      for(int i = 0; i < X.size()-1; i++){
         g.drawLine(X.get(i),Y.get(i),X.get(i+1),Y.get(i+1));
      }
   }
   public void display(Graphics g){
      double height = 5;
      double width = 10;
      toJava();
      //drawLine(g);
      g.setColor(Color.BLACK);
      //g.setColor(color);
      double a = Math.atan(height/width);
      int d = 10;
      double diagonal = Math.sqrt(height*height*0.25+width*width*0.25);
      g.fillPolygon(new Polygon(new int[]{(int)(javaX+d*Math.cos(-angle)),(int)(javaX+diagonal*Math.cos(-angle+a)),(int)(javaX+diagonal*Math.cos(-angle+Math.PI-a)),(int)(javaX-d*Math.cos(-angle)),(int)(javaX+diagonal*Math.cos(-angle+Math.PI+a)),(int)(javaX+diagonal*Math.cos(-angle-a)),},new int[]{(int)(javaY+d*Math.sin(-angle)),(int)(javaY+diagonal*Math.sin(-angle+a)),(int)(javaY+diagonal*Math.sin(-angle+Math.PI-a)),(int)(javaY-d*Math.sin(-angle)),(int)(javaY+diagonal*Math.sin(-angle+Math.PI+a)),(int)(javaY+diagonal*Math.sin(-angle-a))},6));
   }
}