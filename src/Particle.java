import java.awt.*;
import java.util.Random;

public class Particle
{
    private Random random = new Random();
    private double x = 0, y = 0, direction = random.nextInt(90)+225, speed = 2, age = 0, life = 50, scroll = 0;
    private double radius = 0;
    private Color color;
    private Main parent;
    private double alpha = 1;

    private double gravity = .1;

    public boolean alive = true;
    public boolean bullet = false;

    public Particle(double x, double y, Color color, double radius, double radius_rand, Main parent)
    {
/*    	if(direction_weight != -1)
    	{
    		direction = direction_weight + random.nextInt(45)-(45/2);
    	}*/
    	
    	
        speed = 2 + ((random.nextDouble()*2)-1);
        this.radius = radius+((random.nextDouble()*(radius_rand*2))-radius_rand);
        this.x = x;
        this.y = y;
        this.color = color;
        this.parent = parent;
    }
    
    public Particle(double x, double y, Color color, double radius, double radius_rand, Main parent, boolean bullet)
    {
        speed = 2 + ((random.nextDouble()*2)-1);
        this.radius = radius+((random.nextDouble()*(radius_rand*2))-radius_rand);
        this.x = x;
        this.y = y;
        this.color = color;
        this.parent = parent;
    	this.bullet = bullet;
    	if(bullet)
    		gravity = gravity / 2;
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
        return Math.cos(direction/57.2960285258)*speed;
    }

    public double getYspeed()
    {
        return Math.sin(direction/57.2960285258)*speed;
    }

    public void setXYspeed(double x2, double y2)
    {
        int x1 = 0, y1 = 0;
        double H = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)); // The hypotenuse
        double x = x2-x1; // The opposite
        double y = y2-y1; // The adjacent
        double angle = Math.acos(x/H);
        angle = angle * 57.2960285258;
        if(y < 0)
        {
            angle = 360-angle;
        }
        direction = angle;
    }

    public void move(Dimension bounds)
    {
    	if(x > bounds.getHeight()/2)
    		alive = false;
    	if(!alive)
    		return;
        age += 1;
        if(age >= life)
            alive = false;
        if(age >= life/4*3)
        	alpha -= .04;
        setXYspeed(getXspeed(), getYspeed() + gravity);
        x += getXspeed();
        y += getYspeed();
    }

    public void draw(Graphics g)
    {
    	if(!alive)
    		return;
    	color = new Color((float)((float)color.getRed()/255.0f),(float)((float)color.getGreen()/255.0f),(float)((float)color.getBlue()/255.0f), (float)alpha);
        g.setColor(color);
        int move = parent.getSize().width/2;
        move -= scroll;
        g.fillOval(move+(int)(x-radius),(int)(y-radius),(int)(radius),(int)(radius));
    }
}