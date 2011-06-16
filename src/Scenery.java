import java.awt.*;
import java.applet.*;
import java.util.Random;

public class Scenery
{
    int x,y,scrollx;
    Applet parent;
    Image img;
    boolean shaking = false;
    int shakeamount = 0;

    Random random = new Random();

    public Scenery(Image image, Applet parent, int shakeamount)
    {
        this.parent = parent;
        this.img = image;
        this.shakeamount = shakeamount;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void shake(boolean shake)
    {
        shaking = shake;
    }

    public void scroll(int amnt)
    {
        scrollx = amnt;
    }

    public void draw(Graphics g)
    {
        int move = parent.getSize().width/2;
        move -= scrollx;
        int ymove = 0;
        if(shaking)
        {
            move += (random.nextInt(shakeamount*2)-shakeamount);
            ymove = (random.nextInt(shakeamount*2)-shakeamount);
        }
        g.drawImage(img,move+x,ymove+y,parent);
    }
}