import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.Random;

public class Segment
{
    double x,y,lastx,lasty;
    Image img;
    Image bg;
    Image mask;
    Main parent;

    public Rectangle bounds = new Rectangle(0,0,1,1);

    boolean upkey = false;
    boolean leftkey = false;
    boolean rightkey = false;

    double direction = 0;
    double speed = 5;

    double speedMax = 10;
    double speedMin = 5;
    double friction = .25;
    double acceleration = .25;
    double gravity = 1;
    double turn = 5;
    double slowturn = 3;
    double distance = 30;
    int shakeamount = 0;

    boolean shaking = false;

    double scroll = 0;

    boolean head = false;

    AffineTransform identity = new AffineTransform();
    Random random = new Random();

    public Segment(Image img, int x, int y, Main parent, boolean head, int shakeamount)
    {
        this.img = img;
        this.x = lastx = x;
        this.y = lasty = y;
        this.parent = parent;
        this.head = head;
        this.shakeamount = shakeamount;
    }

    public Rectangle bounds()
    {
        return bounds;
    }

    public void shake(boolean shake)
    {
        shaking = shake;
    }

    public void scroll(double amnt)
    {
        scroll = amnt;
    }

    public void keepInBounds(Dimension dim)
    {
        if(x < 0)
            x = 0;
        if(x > dim.width)
            x = dim.width;
        if(y > dim.height)
            y = dim.height;
    }

    public void upkey(boolean upkey)
    {
        this.upkey = upkey;
    }
    public void leftkey(boolean leftkey)
    {
        this.leftkey = leftkey;
    }
    public void rightkey(boolean rightkey)
    {
        this.rightkey = rightkey;
    }

    public boolean isHead()
    {
        return head;
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

    public double getSpeed()
    {
        return Math.sqrt(getXspeed()*getXspeed() + getYspeed()*getYspeed());
    }

    public static double getXspeed(double direction, double speed)
    {
        return Math.cos(Math.toRadians(direction))*speed;
    }

    public static double getYspeed(double direction, double speed)
    {
        return Math.sin(Math.toRadians(direction))*speed;
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
    }
    public void setYspeed(double y2)
    {
        double x2 = getXspeed();
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

    public static double pointDirection(double x1, double y1, double x2, double y2)
    {
        double H = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)); // The hypotenuse
        double x = x2-x1; // The opposite
        double y = y2-y1; // The adjacent
        double angle = Math.acos(x/H);
        angle = Math.toDegrees(angle);
        if(y < 0)
        {
            angle = 360-angle;
        }
        return angle;
    }

    public static double pointDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }

    public double getDirection()
    {
        return direction;
    }

    public void setDirection(double direction)
    {
        this.direction = direction;
    }

    public double getLastX()
    {
        return lastx;
    }

    public double getLastY()
    {
        return lasty;
    }

    public void setSpeed(double speed)
    {
        if(speed <= 0)
        {
            //setXYspeed(0,0);
        	this.speed = 0;
        	
            return;
        }
        double currentSpeed = this.speed;
        setXYspeed(getXspeed() * speed / currentSpeed, getYspeed() * speed / currentSpeed);
    }

    public static double getSpeed(double xspeed, double yspeed)
    {
    	return Math.sqrt((xspeed)*(xspeed) + (yspeed)*(yspeed));
    }

    public void move()
    {
        lastx = x;
        lasty = y;
        
        while(direction < 0)
        	direction += 360;
        while(direction > 360)
        	direction -= 360;
        	
        if(y > parent.getSize().height/2)
        {
            if(leftkey)
                setDirection(getDirection()-turn);
            if(rightkey)
                setDirection(getDirection()+turn);
            if(!upkey)
            {
                if(speed > speedMin)
                {
                    speed -= friction;
                }
            }
            else
            {
                speed += acceleration;
                if(speed > speedMax)
                    speed = speedMax;
            }
        }
        else
        {
            if(leftkey)
                setDirection(getDirection()-slowturn);
            if(rightkey)
                setDirection(getDirection()+slowturn);
            setYspeed(getYspeed() + gravity);
        }
        x += getXspeed();
        y += getYspeed();
        int width = img.getWidth(null)/2;
        int height = img.getHeight(null)/2;
        bounds = new Rectangle((int)x-width, (int)y-height, width*2, height*2);
    }

    public void movetail(Segment seg)
    {
        double goDist = pointDistance(x,y,seg.getX(),seg.getY())-distance;
        double goDir = pointDirection(x,y,seg.getX(),seg.getY());

        direction = goDir;
        
        x += getXspeed(goDir,goDist);
        y += getYspeed(goDir,goDist);
    }

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform trans = new AffineTransform();
        int imgw = img.getWidth(null);
        int imgh = img.getHeight(null);
        int move = parent.getSize().width/2;
        move -= scroll;

        int ymove = 0;
        if(shaking)
        {
            move += (random.nextInt(shakeamount*2)-shakeamount);
            ymove = (random.nextInt(shakeamount*2)-shakeamount);
        }


        
        trans.setToTranslation(move+x-(imgw/2),ymove+y-(imgh/2));
        trans.rotate(Math.toRadians(direction), imgw/2, imgh/2);
        
        g2d.drawImage(img, trans, parent);
    }
}