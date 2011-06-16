import java.awt.event.*;
import java.applet.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Main extends Applet implements Runnable, KeyListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image dbImage;
    private Graphics dbg;
    private Thread th = new Thread(this);
    private int fps = 50;
    private boolean stopped = false;
    
    private long gameTime = fps*25;
    private int counts[]={0,0,0,0,0,0,0,0};
    
    private boolean paused = false;
    private boolean pausequeue = false;
    
    private long lasttime = 0;
    private long mscounter = 0;
    private int framecounter = 0;
    private long lmscounter = 0;
    private int lframecounter = 0;

    private boolean leftkey = false;
    private boolean rightkey = false;
    private boolean upkey = false;

    private boolean shaking = false;
    private double shakemargin = 45;
    private int shakeamount = 5;

    private int scroll = 0;

    public int maxWidth=640*3;
    
    private int segmentCount = 8; // 25;
    private int segmentSpacing = 30; // 8;

    private List<Particle> particles = new ArrayList<Particle>();
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private List<NPC> npcs = new ArrayList<NPC>();

    String text = "";

    Segment[] segs = new Segment[segmentCount];
    Scenery[] scene = new Scenery[10];

    NPC npc1,npc2;

    Random rand = new Random();
    
    HUD hud = new HUD(this, 4, 100);

    public long getMS()
    {
    	return new Date().getTime();  	
    }
    
    public void init()
    {  	
    	lasttime = getMS();
    	
        Image segimg = getImage(getCodeBase(), "Images/segment.png");
        Image headimg = getImage(getCodeBase(), "Images/oldnewhead.png");
        Image bush = getImage(getCodeBase(), "Images/bush.png");

        for(int i = 0; i < scene.length; i++)
        {
            scene[i] = new Scenery(bush,this, shakeamount);
            scene[i].setPosition(rand.nextInt(maxWidth),(getSize().height/2)-32);
        }
        int x = 500, y = 128;
        for(int i = 0; i < segs.length; i++)
        {
            if(i == 0)
                segs[i] = new Segment(headimg, x,y,this,true, shakeamount);
            else
                segs[i] = new Segment(segimg, x,y,this,false, shakeamount);
            x -= segmentSpacing;
        }

        setBackground(new Color(0,255,255));
        addKeyListener(this);
    }

    public void start()
    {
        th.start();
    }

    public void stop()
    {
        stopped = true;
    }

    public void destroy()
    {
        stopped = true;
    }

    public void run()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while(!stopped)
        {
        	Dimension size = this.getSize();
            int seconds = (int)(gameTime/fps);
        	framecounter++;
        	long time = getMS();
        	mscounter += time - lasttime;
        	lasttime = time;
        	if(mscounter >= 1000)
        	{
        		lframecounter = framecounter;
        		lmscounter = mscounter;
        		framecounter = 0;
        		mscounter = 0;
        	}
        	
    		text = lframecounter+" FPS -- Accuracy: "+lmscounter+"/1000       Victims: "+counts[NPC.VICTIM]+"      "+gameTime+" -- "+seconds;
        	
        	if(paused && !pausequeue)
        		continue;
        	else if(pausequeue)
        		pausequeue = false;

        	gameTime ++;
        	
            scroll = (int)segs[0].getX();

            if(scroll < getSize().width/2)
                scroll = getSize().width/2;
            if(scroll > maxWidth-getSize().width/2)
                scroll = maxWidth-getSize().width/2;

            if(segs[0].getY() < getSize().height/2 && segs[0].getY() > getSize().height/2-shakemargin)
            {
                if(!shaking)
                {
                    for(int i = 0; i < 50; i++)
                    {
                        particles.add(new Particle(segs[0].getX(),getSize().width/2-112,new Color(127,55,0),4,2,this));
                    }
                }
                shaking = true;
            }
            else
            {
                shaking = false;
            }

            if((counts[NPC.VICTIM] < 10 && rand.nextInt(200) == 1) || rand.nextInt(800) == 1)
            {
                if(rand.nextInt(2) == 0)
                    npcs.add(new NPC(-64,(getSize().height/2)-32,false,NPC.VICTIM,this,shakeamount));
                else
                    npcs.add(new NPC(maxWidth+64,(getSize().height/2)-32,true,NPC.VICTIM,this,shakeamount));
                counts[NPC.VICTIM]++;
            }
            if(seconds > 50)
            {
            	if((counts[NPC.LITE_GUNMAN] < 10 && rand.nextInt(250) == 1))
            	{
                    if(rand.nextInt(2) == 0)
                        npcs.add(new NPC(-64,(getSize().height/2)-32,false,NPC.LITE_GUNMAN,this,shakeamount));
                    else
                        npcs.add(new NPC(maxWidth+64,(getSize().height/2)-32,true,NPC.LITE_GUNMAN,this,shakeamount));
                    counts[NPC.LITE_GUNMAN]++;            		
            	}
            }
            if(seconds > 120)
            {
            	if((counts[NPC.HEAVY_GUNMAN] < 10 && rand.nextInt(300) == 1))
            	{
                    if(rand.nextInt(2) == 0)
                        npcs.add(new NPC(-64,(getSize().height/2)-32,false,NPC.HEAVY_GUNMAN,this,shakeamount));
                    else
                        npcs.add(new NPC(maxWidth+64,(getSize().height/2)-32,true,NPC.HEAVY_GUNMAN,this,shakeamount));
                    counts[NPC.HEAVY_GUNMAN]++;            		
            	}
            }
            if(seconds > 180)
            {
            	if((counts[NPC.TANK] < 10 && rand.nextInt(300) == 1))
            	{
                    if(rand.nextInt(2) == 0)
                        npcs.add(new NPC(-64,(getSize().height/2)-32,false,NPC.TANK,this,shakeamount));
                    else
                        npcs.add(new NPC(maxWidth+64,(getSize().height/2)-32,true,NPC.TANK,this,shakeamount));
                    counts[NPC.TANK]++;            		
            	}
            }
            

            for(int i = 0; i < particles.size(); i++)
            {
                particles.get(i).move(size);
                particles.get(i).scroll(scroll);
                if(!particles.get(i).alive)
                {
                    particles.remove(i);
                }
            }

            for(int i = 0; i < bullets.size(); i++)
            {
            	bullets.get(i).move(size);
            	bullets.get(i).scroll(scroll);
                if(bullets.get(i).isColliding(segs[0]))
                {
                	hud.health -= NPC.speeds[bullets.get(i).parentType][NPC.ATTACK_POWER];
                	bullets.remove(i);
                	continue;
                }
                if(!bullets.get(i).alive)
                {
                	bullets.remove(i);
                	continue;
                }
            }

            for(int i = 0; i < npcs.size(); i++)
            {
                npcs.get(i).move(size,segs[0]);
                npcs.get(i).shake(shaking);
                npcs.get(i).scroll(scroll);
            	int amount = 50;
/*            	if(lframecounter < 40)
            		amount = 30;
            	if(lframecounter < 30)
            		amount = 5;*/
            	if(npcs.get(i).wants_to_fire)
            	{
            		bullets.add(npcs.get(i).bullet_to_fire);
            		npcs.get(i).wants_to_fire = false;
            	}
            	
                if(npcs.get(i).bounds.intersects(segs[0].bounds))
                {
                    for(int j = 0; j < amount; j++)
                    {
                    	Color diecolor = new Color(rand.nextInt(191)+63,rand.nextInt(191)+63,rand.nextInt(191)+63);
                        particles.add(new Particle(npcs.get(i).getX(),npcs.get(i).getY(),diecolor,5,2,this)); 
                    }
                    counts[npcs.get(i).getType()]--;
                    npcs.remove(i);
                }
            }

//            text = ""+scroll;
            segs[0].leftkey(leftkey);
            segs[0].rightkey(rightkey);
            segs[0].upkey(upkey);

            Dimension dim = this.getSize();
            dim.width=maxWidth;

            for(int i = 0; i < segs.length; i++)
            {
                segs[i].scroll(scroll);
                if(segs[i].isHead())
                    segs[i].move();
                else
                    segs[i].movetail(segs[i-1]);
                segs[i].keepInBounds(dim);
                segs[i].shake(shaking);
            }
            for(int i = 0; i < scene.length; i++)
            {
                scene[i].shake(shaking);
                scene[i].scroll(scroll);
            }
            repaint();
            try
            {
                Thread.sleep(1000/fps);
            }
            catch(Exception e)
            {

            }
        }
    }

    public void keyPressed(KeyEvent e)
    {
        /*
         * 37 = left arrow
         * 39 = right arrow
         * 40 = down arrow
         * 38 = up arrow
         */
        if(e.getKeyCode() == 37) // Left arrow
        {
            leftkey = true;
        }
        if(e.getKeyCode() == 39) // Right arrow
        {
            rightkey = true;
        }
        if(e.getKeyCode() == 38) // Up arrow
        {
            upkey = true;
        }
    }
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == 37) // Left arrow
        {
            leftkey = false;
        }
        if(e.getKeyCode() == 39) // Right arrow
        {
            rightkey = false;
        }
        if(e.getKeyCode() == 38) // Up arrow
        {
            upkey = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_P)
        {
        	paused = !paused;
        }
        if(e.getKeyCode() == KeyEvent.VK_O)
        {
        	if(!paused)
        		paused = true;
        	else
        		pausequeue = true;
        }
    }
    public void keyTyped(KeyEvent e)
    {

    }

    public void update(Graphics g)
    {
        if (dbImage == null)
        {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics ();
        }

        dbg.setColor (getBackground ());
        dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

        dbg.setColor (getForeground());
        paint (dbg);

        g.drawImage (dbImage, 0, 0, this);
    }

    public void paint(Graphics g)
    {
        Dimension size = getSize();

        int r = 0;
        if(shaking)
            r = rand.nextInt(shakeamount*2)-shakeamount;

        g.setColor(Color.decode("#AA5303"));
        g.fillRect(0,size.height/2+r,size.width,size.height/2-r);


        for(int i = 0; i < scene.length; i++)
        {
            scene[i].draw(g);
        }

        for(int i = segs.length-1; i >= 0; i--)
        {
            segs[i].draw(g);
        }

        for(int i = 0; i < npcs.size(); i++)
        {
            npcs.get(i).draw(g);
        }

        for(int i = 0; i < particles.size(); i++)
        {
            particles.get(i).draw(g);
        }

        for(int i = 0; i < bullets.size(); i++)
        {
        	bullets.get(i).draw(g);
        }

        g.setColor(Color.BLACK);
        hud.draw(g);
//        g.drawString(text,10,10);
    }

}