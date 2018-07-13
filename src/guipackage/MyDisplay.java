package guipackage;

import processing.core.PApplet;

public class MyDisplay extends PApplet{

	public void setup() {
		//Setup size of Applet window(canvas)
		size(400,400);
		//setting up background color
		background(204,255,255);
	}
	//creating a smiley face
	public void draw() {
		//filling color of the below given shapes
		//color is filled till any new fill() method is not called
		fill(255,255,0);
		//ellipse method for creating circles and ellipses
		ellipse(200,200,350,350);
		//filling black colors for eyes
		fill(0,0,0);
		//eyes of the smiley
		ellipse(130,120,50,70);
		ellipse(270,120,50,70);
		//removing the filling of black color in smile using noFill()
		noFill();
		//making smile of the smiley
		arc(200, 220, 180, 180, 0, PI);
	}
}
