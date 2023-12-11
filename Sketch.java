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
  float balloonX;
  float balloonY;
  float dartX;
  float dartY;
  float dartMonkeyX;
  float dartMonkeyY;
  float dartMonkeySpeedX;
  float dartMonkeySpeedY;
  double gravity;
  float dartSpeed;
  float dartSpeedX;
  float dartSpeedY;
  double dartAngle;
  boolean isPopped;
  
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
    dartMonkeyX = 0;
    dartMonkeyY = height - imgDartMonkey.height;
    gravity = 0.2;
    dartX = dartMonkeyX + imgDartMonkey.width / 2;
    dartY = dartMonkeyY;
    balloonX = balloRandom.nextInt(width / 2) + width / 2;
    balloonY = balloRandom.nextInt(height - imgBalloon.height);
  }

  public void draw() {
   
    // Draw background
    background(imgBackground);

    // Movement
    movement();

    // Shooting: Calculate angle and speed
    dartAngle = Math.atan((double)(mouseY - dartMonkeyY) / (mouseX - dartMonkeyX + imgDartMonkey.width));
    dartSpeed = (float)(Math.sqrt(Math.pow(mouseX - dartMonkeyX + imgDartMonkey.width, 2) + Math.pow(mouseY - dartMonkeyY, 2)) / 10);

    // Draws a line from the dart monkey to the mouse
    line(dartMonkeyX + imgDartMonkey.width, dartMonkeyY, mouseX, mouseY);

    // Shoots dart if mouse is pressed
    if (mousePressed) {
      shoot();
    }
    
    // Draws balloon
    image(imgBalloon, balloonX, balloonY);
  }

  public void movement() {

    // Left-right movement
    if (keyPressed) {
      if (key == 'd' || keyCode == RIGHT) {
        dartMonkeySpeedX = 5;
      }
      else if (key == 'a' || keyCode == LEFT) {
        dartMonkeySpeedX = -5;
      }
    }
    else {
      dartMonkeySpeedX = 0;
    }

    // If the monkey is not on the ground, it falls
    if (dartMonkeyY < height - imgDartMonkey.height) {
      dartMonkeySpeedY += gravity;
    }
    // Jumping
    else if ((keyPressed && key == 'w') || (keyPressed && key == ' ') || (keyPressed && keyCode == UP)) {
        dartMonkeySpeedY = -5;
    }
    // If the monkey is on the ground, then it stops falling
    else {
      dartMonkeySpeedY = 0;
    }
    dartMonkeyX += dartMonkeySpeedX;
    dartMonkeyY += dartMonkeySpeedY;

    // Collision, prevents the monkey from going off the screen
    if (dartMonkeyX < 0) {
      dartMonkeyX = 0;
    }
    else if (dartMonkeyX > width - imgDartMonkey.width) {
      dartMonkeyX = width - imgDartMonkey.width;
    }

    image(imgDartMonkey, dartMonkeyX, dartMonkeyY);
  }

  public void shoot() {

    // Shooting: Calculate angle and speed
    dartSpeedX = (float)(dartSpeed * Math.cos(dartAngle));
    dartSpeedY = (float)(dartSpeed * Math.sin(dartAngle));
    dartX += dartSpeedX;
    dartY += dartSpeedY;
    image(imgDart, dartX, dartY);

    // If the dart goes off screen, the position resets
    if (dartX > width || dartY > height) {
      dartX = dartMonkeyX + imgDartMonkey.width;
      dartY = dartMonkeyY;
    }
  }

  public void mousePressed() {
    
    // If the mouse is pressed on the balloon, the balloon moves to a random location
    if (mouseX > balloonX && mouseX < balloonX + imgBalloon.width && mouseY > balloonY && mouseY < balloonY + imgBalloon.height) {
      
      balloonX = balloRandom.nextInt(width / 2 - imgBalloon.width) + width / 2 - imgBalloon.width;
      balloonY = balloRandom.nextInt(height - imgBalloon.height);
    }
  }

  public void keyPressed() {

    // If the shift key is pressed while the monkey is on the balloon, it moves to a random location
    if (keyCode == SHIFT && dartMonkeyX < balloonX + imgBalloon.width &&
        dartMonkeyX + imgDartMonkey.width > balloonX &&
        dartMonkeyY < balloonY + imgBalloon.height &&
        dartMonkeyY + imgDartMonkey.height > balloonY) {

      balloonX = balloRandom.nextInt(width / 2 - imgBalloon.width) + width / 2 - imgBalloon.width;
      balloonY = balloRandom.nextInt(height - imgBalloon.height);
    }
  }
}
