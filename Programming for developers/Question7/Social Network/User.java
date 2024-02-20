package application;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Random;

/**
 * GraphNodes hold string information and information about successors
 */
public class User {
  // instance fields
  private Image image;
  private String name;
  private String description;
  private ArrayList<User> friends = new ArrayList<User>();

  /**
   * Initialize with the vertex value
   *
   * @param name user name
   */
  public User(String name) {
    this.name = name;

    Random rand = new Random();
    this.image = getRandomImage(rand);
    this.description = getRandomDescription(rand);

  }

  /**
   * Get Image
   * @return Image
   */
  public Image getImage() {
    return image;
  }

  /**
   * Get User Name
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Get Profile Description  
   * @return Description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get Friends
   * @return List of friends
   */
  public ArrayList<User> getFriends() {
    return friends;
  }

  /**
   * Updates to random image
   * @param rand - instance of random
   * @return Image
   */
  private Image getRandomImage(Random rand) {
	// Create list of picture strings
    String[] defaultDisplayPictures = new String[5];
    defaultDisplayPictures[0] = "default1.jpg";
    defaultDisplayPictures[1] = "default2.jpg";
    defaultDisplayPictures[2] = "default3.jpg";
    defaultDisplayPictures[3] = "default4.jpg";
    defaultDisplayPictures[4] = "default5.jpg";

    // Randomly Select
    int num = rand.nextInt(5);
    return new Image("file:" + defaultDisplayPictures[num]);
  }

  /**
   * Gets a random Description 
   * @param rand - instance of random
   * @return random Description
   */
  private String getRandomDescription(Random rand) {
	// Create List of Strings
    String[] defaultDescriptions = new String[5];
    defaultDescriptions[0] = "Today is a great day :)";
    defaultDescriptions[1] = "Never give up!";
    defaultDescriptions[2] = "What is your favorite TV show?";
    defaultDescriptions[3] = "Strive to be the best";
    defaultDescriptions[4] = "Excited for finals week!";

    // Randomly select string from list
    int num = rand.nextInt(5);
    return defaultDescriptions[num];
  }

}
