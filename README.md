# Earthquake Mapper

This project is an Earthquake Mapper application implemented in Java, utilizing JavaFX, Java Swing, Applet, Open GL, and Google Maps libraries. The application provides a live feed of earthquake data and displays it on a map. It offers various features to visualize and explore earthquake information.

## Features

The Earthquake Mapper application includes the following key features:

- **Rectangle Icon**: Clicking on the rectangle icon displays the top 5 earthquakes. The text changes to "Click to Display All" when clicked again, returning to the original view.

- **Threat Circle**: The threat circle appears only for the selected earthquake marker. It provides a visual representation of the affected area or potential impact of the earthquake. The threat circle disappears when the marker is deselected or when the user moves outside the marker area.

- **Cross Icon**: The cross icon is used to represent earthquakes that occurred in the past hour or past day. Different colors are used to distinguish between these time frames, providing visual context and helping identify recent seismic activity.

- **Colored Countries**: Countries on the map are colored based on the number of earthquakes that occurred within them. The color scale ranges from black (indicating a higher number of earthquakes) to white (indicating zero earthquakes). This coloring scheme allows users to identify regions with significant seismic activity at a glance.

- **Key**: The application provides a key or legend that explains the color scale used for the countries, assisting users in interpreting the map's visual representation.

## Dependencies

This project relies on the following dependencies:

- **Language**: Java
- **GUI Libraries**: JavaFX, Java Swing, Applet
- **Mapping Libraries**: Open GL, Google Maps

Ensure that you have the necessary libraries installed and properly configured to run the Earthquake Mapper application.

## Usage

To use the Earthquake Mapper application, follow these steps:

1. Install Java on your system if it is not already installed.

2. Compile and run the Java source files.

3. Ensure that the required dependencies, including JavaFX, Java Swing, Applet, Open GL, and Google Maps libraries, are available and properly configured.

4. Launch the application and allow it to access live earthquake data.

5. The application will display the map with earthquake markers and colored countries. Interact with the various icons and markers to explore the earthquake data and view additional information.

6. Use the rectangle icon to display the top 5 earthquakes or toggle back to the original view.

7. Observe the threat circle that appears for the selected earthquake marker, indicating the affected area or potential impact.

8. Take note of the cross icons representing earthquakes that occurred in the past hour or past day, with different colors for each time frame.

9. Analyze the colored countries to identify regions with varying levels of seismic activity based on the color scale provided in the key.

Feel free to explore the application's features and interact with the map to gain insights into earthquake occurrences and their impact.

## Future Enhancements

While the current version of the Earthquake Mapper application provides essential functionality, there are several areas that can be improved in future updates:

- **Real-time Updates**: Implement real-time updates of earthquake data to ensure users have the most up-to-date information.

- **Interactive Filters**: Allow users to apply filters to the displayed earthquake markers, such as filtering by magnitude, location, or time frame, to customize their view.

- **Detailed Information**: Provide additional details and statistics about individual earthquakes when clicked or selected on the map.

- **Data Visualization**: Enhance data visualization techniques to represent earthquake data in different visual formats, such as heatmaps or animated timelines.

- **User Preferences**: Enable users to customize the application's settings, such as map styles, default zoom level, or earthquake threshold for coloring countries.

## Contributors

- [rayin19](https://github.com/rayin19): Project Developer

Feel free to contribute to the project by submitting pull requests, reporting issues, or suggesting improvements.
