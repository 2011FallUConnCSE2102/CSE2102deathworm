import java.awt.*;
import java.util.Random;

public class NPC
{
    public static final int VICTIM = 0;
    public static final int LITE_GUNMAN = 1;
    public static final int HEAVY_GUNMAN = 2;
    public static final int TANK = 3;

    public Rectangle bounds = new Rectangle(0,0,1,1);

    public static final int WALK_SPEED = 0;
    public static final int FLEE_SPEED = 1;
    public static final int ATTACK_SPEED = 2;
    public static final int ATTACK_RANDOM = 3;
    public static final int ATTACK_BULLET_SPEED = 4;
    public static final int ATTACK_POWER = 5;

    private int type;
    private int mode = 0;

    private Image left_img;
    private Image right_img;
    private Main parent;
    private double x, y;

    private boolean shaking = false;
    private int shakeamount = 0;

    private double scroll = 0;

    private boolean left = true;

    public static final int speeds[][] = {
                                {2,3,0,50,10,3},
                                {2,3,2,40,15,7},
                                {3,2,2,30,20,10},
                                {2,3,3,20,25,15}
                             };

    Random random = new Random();


    public boolean wants_to_fire = false;
    public Bullet bullet_to_fire = null;


    public NPC(double x, double y, boolean left, int type, Main parent, int shakeamount)
    {
        this.shakeamount = shakeamount;
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.left = left;
        this.type = type;

        left_img = parent.getImage(parent.getCodeBase(), "Images/NPC/"+(type+1)+"_left.png");
        right_img = parent.getImage(parent.getCodeBase(), "Images/NPC/"+(type+1)+"_right.png");
    }

    public int getType()
    {
        return type;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void shake(boolean shake)
    {
        shaking = shake;
    }

    public void scroll(int amnt)
    {
        scroll = amnt;
    }

    public void setLeft(Image image)
    {
        left_img = image;
    }

    public void setRight(Image image)
    {
        right_img = image;
    }

    public void move(Dimension dim, Segment head)
    {
//        left = random.nextInt(350) == 1 ? !left : left;
        if(left)
            x -= speeds[type][mode];
        else
            x += speeds[type][mode];
        if(x < -64 && left)
            x = parent.maxWidth;
        if(x > parent.maxWidth && !left)
            x = -64;

        if(head.getY() < dim.getHeight()/2)
        {
            if(random.nextInt(speeds[type][ATTACK_RANDOM]) == 1)
            {
                bullet_to_fire = new Bullet(x, y, Color.black, 4, parent);
                double direction = Segment.pointDirection(x, y, head.getX(), head.getY());
                double speed = speeds[type][ATTACK_BULLET_SPEED];
                bullet_to_fire.setXYspeed(Segment.getXspeed(direction,speed), Segment.getYspeed(direction,speed));
                bullet_to_fire.parentType = getType();
                wants_to_fire = true;
            }
        }

        bounds = new Rectangle((int)x,(int)y,left_img.getWidth(null),left_img.getHeight(null));
    }

    public void draw(Graphics g)
    {
        Image drawImg = left ? left_img : right_img;
        int move = parent.getSize().width/2;
        move -= scroll;
        int ymove = 0;
        if(shaking)
        {
            move += (random.nextInt(shakeamount*2)-shakeamount);
            ymove = (random.nextInt(shakeamount*2)-shakeamount);
        }
        g.drawImage(drawImg,(int)(move+x),(int)(ymove+y),parent);
    }
}