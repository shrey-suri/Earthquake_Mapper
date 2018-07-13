package module6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private boolean t=false;
	

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(1366, 600);
		if (offline) {
		    map = new UnfoldingMap(this, 0, 0, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 0, 0, 600, 600, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		//System.out.println(map.getZoomLevel());
		//TODO: set This if you want precise threat circle
		//map.setZoomRange(2, 6);
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// Uncomment this line to take the quiz
		//earthquakesURL = "quiz2.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(countryMarkers);
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    //System.out.println(quakeMarkers.size());
	    	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		//sortAndPrint(5);
		fill(255);
		noStroke();
		rect(720,50,260,30);
		fill(0);
		if(t) {
			text("Click to Display All",725,62);
			sortAndPrint(5);
			//selectMarkerIfHover(quakeMarkers);
		}
		else {
			text("Click to Display Top 5 Earthquakes",725,62);
		}
		addKey();
		drawThreatCircle();
		//printQuakes();
	}
	
	// TODO: Add the method:
	//   private void sortAndPrint(int numToPrint)
	// and then call that method from setUp
	 private void sortAndPrint(int numToPrint) {
		 EarthquakeMarker[] markers = quakeMarkers.toArray(new EarthquakeMarker[quakeMarkers.size()] );
		 int i=markers.length-1,t=0;
		 stroke(0);
		 strokeWeight(1);
		 fill(255);
		rect(960,370,370,150);
		textSize(15);
		fill(0);
		text("Top 5 Earthquakes (From Past Week)",1025,387);
		textSize(11);
		 /*for(int j=1;j<markers.length;j++) {
			 currInd=j;
			 while(currInd>0&&markers[currInd].compareTo(markers[currInd-1])==1) {
				 EarthquakeMarker cm = markers[currInd-1];
				 markers[currInd-1]= markers[currInd];
				 markers[currInd]= cm;
				 currInd -= 1;
			 }
		 }*/
		 if(i<numToPrint)
			 numToPrint=i;
		 Arrays.sort(markers);
		 hideMarkers();
		 while(i>-1 ) {
			 if(i>=(markers.length-numToPrint)) {
			text(markers[i].getTitle(),970,410+t);
			 t+=20;
			 markers[i].setHidden(false);}
			 i--;
		 }
	 }
		private void hideMarkers() {	
			for(Marker marker : quakeMarkers) {
				marker.setHidden(true);
			}
			for(Marker marker : cityMarkers) {
				marker.setHidden(true);
			}
		}
		
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)&&!marker.isHidden()) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		if(mouseX>720&&mouseX<980&&mouseY>50&&mouseY<80) {
			if(t) {
				unhideMarkers();
				t=false;
			}
			else {
				//sortAndPrint(5);
				t=true;
			}
		}
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkEarthquakesForClick();
			drawThreatCircle();
			if (lastClicked == null) {
				checkCitiesForClick();
			}
		}
	}
	private void drawThreatCircle() {
		stroke(255,0,0);
		strokeWeight(2);
		noFill();
		if(lastSelected == null) {
			rect(0,0,0,0);
		}
		else if(lastClicked== null) {
			rect(0,0,0,0);
		}
		else {
			int t;
		if(map.getZoomLevel()==2)
			t=10;
		else if(map.getZoomLevel()==3)
			t=7;
		else if(map.getZoomLevel()==4)
			t=5;
		else if(map.getZoomLevel()==5)
			t=3;
		else
			t=1;
		//System.out.println(map.getZoomLevel());
		//System.out.println(map.getScaleFromZoom(map.getZoom()));
		if(lastClicked instanceof EarthquakeMarker)
		ellipse((int)mouseX,(int)mouseY,(int)(((EarthquakeMarker)lastClicked).threatCircle()/t),(int)(((EarthquakeMarker)lastClicked).threatCircle()/t));
	}}
	
	// Helper method that will check if a city marker was clicked on
	// and respond appropriately
	private void checkCitiesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker marker : cityMarkers) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : cityMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : quakeMarkers) {
					EarthquakeMarker quakeMarker = (EarthquakeMarker)mhide;
					if (quakeMarker.getDistanceTo(marker.getLocation()) 
							> quakeMarker.threatCircle()) {
						quakeMarker.setHidden(true);
					}
				}
				return;
			}
		}		
	}
	
	// Helper method that will check if an earthquake marker was clicked on
	// and respond appropriately
	private void checkEarthquakesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker m : quakeMarkers) {
			EarthquakeMarker marker = (EarthquakeMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : quakeMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : cityMarkers) {
					if (mhide.getDistanceTo(marker.getLocation()) 
							> marker.threatCircle()) {
						mhide.setHidden(true);
					}
				}
				return;
			}
		}
	}
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		strokeWeight(1);
		stroke(0,0,0);
		fill(255, 255, 255);
		rect(1000, 50, 230, 285);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(20);
		text("Earthquake Key", 1025, 70);
		
		//shapes and their colors 
		fill(255,128,0);
		//for city marker: triangle x = 1030, y = 98 , size = 5
		triangle(1030,93,1025,103,1035,103);
		//for landQuake Marker: circle x = 1030,y=117 , width = 10, height = 10
		noFill();
		ellipse(1030,117,10,10);
		//for QceanQuake Marker: square
		noFill();
		rect(1025,133,10,10);
		textSize(13);
		//Colors
		//shallow : yellow
		fill(255,255,0);
		ellipse(1030,200,10,10);
		//intermediate : blue
		fill(0,0,255);
		ellipse(1030,220,10,10);
		//deep: red
		fill(255,0,0);
		ellipse(1030,240,10,10);
		//past hour, past day, past week , past month
		strokeWeight(2);
		//Past Hour
		stroke(100,100,100);
		line(1027,257,1032,262);
		line(1027,262,1032,257);
		//Past Day
		stroke(255,0,127);
		line(1027,272,1032,277);
		line(1027,277,1032,272);
	/*	//Past Week
		stroke(100,100,100);
		line(1027,287,1032,292);
		line(1027,292,1032,287);
		//Past Month
		stroke(0,255,0);
		line(1027,302,1032,307);
		line(1027,307,1032,302);*/
		//Threat Region
		stroke(255,0,0);
		noFill();
		ellipse(1030,158,10,10);
		
		//text fields
		fill(0, 0, 0);
		text("City Marker",1050,98);
		text("Land Quake",1050,115);
		text("Ocean Quake",1050,137);
		text("Shallow",1050,198);
		text("Intermediate",1050,218);
		text("Deep", 1050, 238);
		text("Past Hour",1050,259);
		text("Past Day",1050,274);
		//text("Past Week",1050,289);
		//text("Past Month",1050,304);
		text("Threat Region",1050,157);
		text("No. of Earthquakes in a Country:",1010,297);
		textSize(15);
		text("Size ~ Magnitude", 1025, 175);
		text("Increased Black Color",1025,315);
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	// You will want to loop through the country markers or country features
	// (either will work) and then for each country, loop through
	// the quakes to count how many occurred in that country.
	// Recall that the country markers have a "name" property, 
	// And LandQuakeMarkers have a "country" property set.
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				int colorLevel = (int) map(numQuakes, 0, 12, 30, 255);
				country.setColor(color(255-colorLevel,255-colorLevel,255-colorLevel));
				//System.out.println(countryName + ": " +numQuakes);
			}
			else {
				noFill();
				country.setColor(255);
			}
		}
		//System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
