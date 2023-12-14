import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class Sketch extends PApplet {
  
  // Image variables
  PImage imgBalloon;
  PImage imgDart;
  PImage imgDartMonkey;
  PImage imgBackground;

  // Other variables
  float fltBalloonX;
  float fltBalloonY;
  float fltDartX;
  float fltDartY;
  float fltDartMonkeyX;
  float fltDartMonkeyY;
  float fltDartMonkeySpeedX;
  float dblDartMonkeySpeedY;
  double dblGravity;
  float fltDartSpeed;
  float fltDartSpeedX;
  float fltDartSpeedY;
  double dblDartAngle;
  int intBalloonsPopped;

  Random balloRandom = new Random();

  public void settings() {
    size(1324, 699);
  }

  public void setup() {

    // Load images
    imgBalloon = loadImage("balloon.png");
    imgDart = loadImage("dart.png");
    imgDartMonkey = loadImage("dartmonkey.png");
    imgBackground = loadImage("terraria background.png");

    // Resize images
    imgDartMonkey.resize(imgDartMonkey.width / 4, imgDartMonkey.height / 4);
    imgDart.resize(imgDart.width / 8, imgDart.height / 8);
    imgBalloon.resize(imgBalloon.width / 8, imgBalloon.height / 8);

    // Initialize variables
    fltDartMonkeyX = 0;
    fltDartMonkeyY = height - imgDartMonkey.height;
    dblGravity = 0.2;
    fltDartX = fltDartMonkeyX + imgDartMonkey.width / 2;
    fltDartY = fltDartMonkeyY;
    fltBalloonX = balloRandom.nextInt(width / 2) + width / 2;
    fltBalloonY = balloRandom.nextInt(height - imgBalloon.height);

    // Initialize counters
    intBalloonsPopped = 0;
  }

  public void draw() {
   
    // Draw background
    background(imgBackground);

    // Movement
    movement();

    // Shooting: Calculate angle and speed
    dblDartAngle = Math.atan((double)(mouseY - fltDartMonkeyY) / (mouseX - fltDartMonkeyX + imgDartMonkey.width));
    fltDartSpeed = (float)(Math.sqrt(Math.pow(mouseX - fltDartMonkeyX + imgDartMonkey.width, 2) + Math.pow(mouseY - fltDartMonkeyY, 2)) / 10);

    // Draws a line from the dart monkey to the mouse
    line(fltDartMonkeyX + imgDartMonkey.width, fltDartMonkeyY, mouseX, mouseY);

    // Shoots dart if mouse is pressed
    if (mousePressed) {
      shoot();
    }
    
    // Draws balloon
    image(imgBalloon, fltBalloonX, fltBalloonY);

    // Displays the number of balloons popped and darts used
    fill(0);
    textSize(50);
    text("Balloons popped: " + intBalloonsPopped, 10, 50);

    if (keyPressed && key == 'q' && key == 'r') {
      setup();
    }
  }

  /*
   * Controls the movement of the dart monkey
   * @author: Joshua Yin
   * @param: none
   * @return: void
   */

  public void movement() {

    // Left-right movement
    if (keyPressed) {
      if (key == 'd' || keyCode == RIGHT) {
        fltDartMonkeySpeedX = 5;
      }
      else if (key == 'a' || keyCode == LEFT) {
        fltDartMonkeySpeedX = -5;
      }
    }
    else {
      fltDartMonkeySpeedX = 0;
    }

    // If the monkey is not on the ground, it falls
    if (fltDartMonkeyY < height - imgDartMonkey.height) {
      dblDartMonkeySpeedY += dblGravity;
    }
    // Jumping
    else if ((keyPressed && key == 'w') || (keyPressed && key == ' ') || (keyPressed && keyCode == UP)) {
        dblDartMonkeySpeedY = -5;
    }
    // If the monkey is on the ground, then it stops falling
    else {
      dblDartMonkeySpeedY = 0;
    }
    fltDartMonkeyX += fltDartMonkeySpeedX;
    fltDartMonkeyY += dblDartMonkeySpeedY;

    // Collision, prevents the monkey from going off the screen
    if (fltDartMonkeyX < 0) {
      fltDartMonkeyX = 0;
    }
    else if (fltDartMonkeyX > width - imgDartMonkey.width) {
      fltDartMonkeyX = width - imgDartMonkey.width;
    }

    image(imgDartMonkey, fltDartMonkeyX, fltDartMonkeyY);
  }

  public void shoot() {

    // Shooting: Calculate angle and speed
    fltDartSpeedX = (float)(fltDartSpeed * Math.cos(dblDartAngle));
    fltDartSpeedY = (float)(fltDartSpeed * Math.sin(dblDartAngle));
    fltDartX += fltDartSpeedX;
    fltDartY += fltDartSpeedY;
    image(imgDart, fltDartX, fltDartY);

    // If the dart goes off screen, the position resets
    if (fltDartX > width || fltDartY > height) {
      fltDartX = fltDartMonkeyX + imgDartMonkey.width;
      fltDartY = fltDartMonkeyY;
    }
  }

  /*
   * If the mouse is pressed on the balloon, the balloon moves to a random location
   * @author: Joshua Yin
   * @param: none
   * @return: void
   */
  public void mousePressed() {
    
    // If the mouse is pressed on the balloon, the balloon moves to a random location
    if (mouseX > fltBalloonX && mouseX < fltBalloonX + imgBalloon.width && mouseY > fltBalloonY && mouseY < fltBalloonY + imgBalloon.height) {

      fltBalloonX = balloRandom.nextInt(width / 2 - imgBalloon.width) + width / 2 - imgBalloon.width;
      fltBalloonY = balloRandom.nextInt(height - imgBalloon.height);
      intBalloonsPopped++;
    }
  }

  /*
   * If the shift key is pressed and the monkey is on the balloon, it moves to a random location
   * @author: Joshua Yin
   * @param: keyCode, the key that is pressed
   * @return: void
   */
  public void keyPressed() {

    // If the shift key is pressed and the monkey is on the balloon, it moves to a random location
    if (keyCode == SHIFT && fltDartMonkeyX < fltBalloonX + imgBalloon.width &&
        fltDartMonkeyX + imgDartMonkey.width > fltBalloonX &&
        fltDartMonkeyY < fltBalloonY + imgBalloon.height &&
        fltDartMonkeyY + imgDartMonkey.height > fltBalloonY) {

      fltBalloonX = balloRandom.nextInt(width / 2 - imgBalloon.width) + width / 2 - imgBalloon.width;
      fltBalloonY = balloRandom.nextInt(height - imgBalloon.height);
    }
  }
} 
