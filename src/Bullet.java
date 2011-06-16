import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Bullet
{
    private Random random = new Random();
    private double x = 0, y = 0, direction = 0, speed = 0, scroll = 0;
    private double radius = 0;
    private Color color;
    private Main parent;
    
    public int parentType = 0;

    private double gravity = .01;

    public boolean alive = true;

    public Bullet(double x, double y, Color color, double radius, Main parent)
    {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = color;
        this.parent = parent;
    }

    public double getSpeed()
    {
    	return speed;
    }
    
    public void scroll(int scroll)
    {
        this.scroll = scroll;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getXspeed()
    {
        return Math.cos(Math.toRadians(direction))*speed;
    }

    public double getYspeed()
    {
        return Math.sin(Math.toRadians(direction))*speed;
    }

    public boolean isColliding(Segment head)
    {
    	return head.bounds.intersects(new Rectangle2D.Double(x-radius,y-radius,radius*2,radius*2));
    }
    
    public void setXYspeed(double x, double y)
    {
        double H = Math.sqrt((x)*(x) + (y)*(y)); // The hypotenuse
        double angle = Math.acos(x/H);
        angle = angle * 57.2960285258;
        if(y < 0)
        {
            angle = 360-angle;
        }
        direction = angle;
        speed = Segment.getSpeed(x, y);
    }

    public void move(Dimension bounds)
    {
    	if(y > bounds.getHeight()/2)
    		alive = false;
    	if(!alive)
    		return;
     	setXYspeed(getXspeed(), getYspeed() + gravity);
        x += getXspeed();
        y += getYspeed();
    }

    public void draw(Graphics g)
    {
    	if(!alive)
    		return;
    	color = new Color((float)((float)color.getRed()/255.0f),(float)((float)color.getGreen()/255.0f),(float)((float)color.getBlue()/255.0f));
        g.setColor(color);
        int move = parent.getSize().width/2;
        move -= scroll;
        g.fillOval(move+(int)(x-radius),(int)(y-radius),(int)(radius),(int)(radius));
    }
}