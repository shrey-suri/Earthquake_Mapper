package module6;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
// TODO: Implement the comparable interface
public abstract class EarthquakeMarker extends CommonMarker implements Comparable<EarthquakeMarker>
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	// The radius of the Earthquake marker
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude. 
	protected float radius;
	
	
	// constants for distance
	protected static final float kmPerMile = 1.6f;
	
	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors

	
	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
		String age = properties.get("age").toString();
		//System.out.println(age);
	}
	
	// TODO: Add the method:
	 public int compareTo(EarthquakeMarker marker) {
		 if(this.getMagnitude()>marker.getMagnitude()) {
			 return 1;
		 }
		 else if(this.getMagnitude()<marker.getMagnitude()) {
			 return -1;
		 }
			 return 0;
	 }
	
	
	
	// calls abstract method drawEarthquake and then checks age and draws X if needed
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
		pg.strokeWeight(1);
		pg.stroke(0);
		// determine color of marker from depth
		colorDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		// IMPLEMENT: add X over marker if within past day		
		pg.strokeWeight(2);
		String age = properties.get("age").toString();
		//System.out.println(age);
		if(age.equalsIgnoreCase("Past Hour")) {
			pg.stroke(100,100,100);
			pg.line(x - radius, y - radius, x + radius, y + radius);
			pg.line(x - radius, y + radius , x  + radius, y - radius);
		}
		if(age.equalsIgnoreCase("Past Day")) {
			pg.stroke(255,0,127);
			pg.line(x - radius, y - radius, x + radius, y + radius);
			pg.line(x - radius, y + radius , x  + radius, y - radius);
		}
	/*	if(age.equalsIgnoreCase("Past Week")) {
			pg.stroke(100,100,100);
			pg.line(x - radius, y - radius, x + radius, y + radius);
			pg.line(x - radius, y + radius , x  + radius, y - radius);
		}
		if(age.equalsIgnoreCase("Past Month")) {
			pg.stroke(0,255,0);
			pg.line(x - radius, y - radius, x + radius, y + radius);
			pg.line(x - radius, y + radius , x  + radius, y - radius);
		}*/
		
		// reset to previous styling
		pg.popStyle();
		
	}

	/** Show the title of the earthquake if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		//String title = getTitle();
		pg.pushStyle();
		pg.strokeWeight(1);
		pg.stroke(0);
		pg.fill(255);
		pg.rect(x, y, 15 + 7*getTitle().length(), 30);
		pg.fill(0);
		pg.textSize(12);
		pg.text(getTitle(),x+5,y+13);
		
		
		pg.popStyle();
		
	}

	
	/**
	 * Return the "threat circle" radius, or distance up to 
	 * which this earthquake can affect things, for this earthquake.   
	 * DISCLAIMER: this formula is for illustration purposes
	 *  only and is not intended to be used for safety-critical 
	 *  or predictive applications.
	 */
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
		double km = (miles * kmPerMile);
		return km;
	}
	
	// determine color of marker from depth
	// We use: Deep = red, intermediate = blue, shallow = yellow
	private void colorDetermine(PGraphics pg) {
		float depth = getDepth();
		
		if (depth < THRESHOLD_INTERMEDIATE) {
			pg.fill(255, 255, 0);
		}
		else if (depth < THRESHOLD_DEEP) {
			pg.fill(0, 0, 255);
		}
		else {
			pg.fill(255, 0, 0);
		}
	}
	
	
	/** toString
	 * Returns an earthquake marker's string representation
	 * @return the string representation of an earthquake marker.
	 */
	public String toString()
	{
		return getTitle();
	}
	/*
	 * getters for earthquake properties
	 */
	
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {
		return (String) getProperty("title");	
		
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	

	
	
}
