package com.tutorial.main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
public class SmartEnemy extends GameObject{
	
	private Handler handler;
	private GameObject player;
	
	public  SmartEnemy( int x,int y,ID id,Handler handler) {
		super(x,y,id);
		this.handler = handler;
		for(int i=0;i< handler.object.size();i++) {
			if(handler.object.get(i).getId()==ID.Player) player = handler.object.get(i);
		}
			
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,16,16);
	}
	
	
	public void tick() {
		x += velX;
		y += velY;
		
		float diffX = x - player.getX() - 16;
		float diffY = y - player.getY() - 16;
		float distance = (float) Math.sqrt((x-player.getX())*(x-player.getX()) + (y-player.getY())*(y-player.getY()));
		
		velX =  ((-1/distance) * diffX); 
		velY = 	((-1/distance) * diffY);
		
		if(y<=0 || y>= Game.HEIGHT -32) velY *= -1;
		if(x<=0 || x>= Game.HEIGHT -16) velX *= -1;
		
		//Trail code
		handler.addObject(new Trail(x,y,ID.Trail, Color.green, 16, 16, 0.02f, handler));
	}

	
	public void render(Graphics g) {
		/*Graphics2D g2d = (Graphics2D) g;
		
		g.setColor(Color.green);
		g2d.draw(getBounds());*/
		
		g.setColor(Color.green);
		g.fillRect((int)x,(int)y, 16, 16);
		
	}

}
