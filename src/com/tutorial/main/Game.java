package com.tutorial.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1550691097823471818L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH/ 12*9;
	private Thread thread;
	private boolean running = false;
	
	private Random r;
	private Handler handler;
	private HUD hud;
	private Spawn spawner;
	public enum	STATE {
		Menu,
		Game
	};
	
	public STATE gameState = STATE.Game;
	
	
	public Game() {
		handler = new Handler();
		
		
		this.addKeyListener(new KeyInput(handler));
		
		new Window(WIDTH,HEIGHT,"Lets build a game!",this);
		
		hud = new HUD();
		spawner = new Spawn(handler,hud);
		r = new Random();
		
		if(gameState == STATE.Game)
		{
			handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32,ID.Player,handler));		
			handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH -50),r.nextInt(Game.HEIGHT -100),ID.BasicEnemy,handler));
		}
		
		//handler.addObject(new EnemyBoss((Game.WIDTH/2)-48 ,-120,ID.EnemyBoss,handler));
		//handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH),r.nextInt(Game.HEIGHT),ID.BasicEnemy,handler));
		//handler.addObject(new SmartEnemy (r.nextInt(Game.WIDTH -50),r.nextInt(Game.HEIGHT -50),ID.SmartEnemy,handler));
		//for(int i=0;i<20;i++)
		//handler.addObject(new BasicEnemy(r.nextInt(WIDTH),r.nextInt(HEIGHT),ID.BasicEnemy,handler));
		
		//handler.addObject(new Player(WIDTH/2+64,HEIGHT/2-32,ID.Player2));
		
		//for( int i = 0; i < 50; i++) {
			//handler.addObject(new Player(r.nextInt(WIDTH),r.nextInt(HEIGHT),ID.Player));				
		//}
		
	}
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	public void run() {
		this.requestFocus();	
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			while (delta>=1) {
				tick();
				delta--;
			}
			
			if(running)
				render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	private void tick() {
		handler.tick();
		if(gameState == STATE.Game)
		{
			hud.tick();
			spawner.tick();	
		}
		
		
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		handler.render(g);
		
		if(gameState == STATE.Game)
		{
			hud.render(g);
		}else {
			g.drawString("Menu", 100, 100);
		}
		
		hud.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public static float clamp(float var,float min,float max) {
		if (var >= max)
			return var = max;
		else if(var <=min)
			return var =min;
		else 
			return var;
	}
	
	public static void main(String args[]) {
		new Game();
	}
}
