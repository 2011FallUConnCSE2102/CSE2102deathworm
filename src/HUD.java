import java.awt.*;

public class HUD
{
	Main parent;
	
	private double totalLives, totalHealth;
	public double lives, health, score = 0;
	
	public HUD(Main parent, int initialLives, int initialHealth)
	{
		lives = totalLives = initialLives;
		health = totalHealth = initialHealth;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.red);
		g.fillRect(10, 10, 102, 20);
		g.setColor(Color.gray);
		g.drawRect(10, 10, 102, 20);

		double width = (100 / totalHealth) * health;
		g.setColor(Color.green);
		g.fillRect(11, 11, (int)width, 19);
	}
}
