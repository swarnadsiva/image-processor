package model;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import observer.ImageController;
import observer.ImageControllerImpl;
import utilities.Orientation;

/**
 * Driver class to generate images and process images loaded in.
 */
public class ImageDriver {

  /**
   * Main entry point of program.
   *
   * @param args string array with single element indicating filename of input commands to perform
   *             on image.
   */
  public static void main(String[] args) {

    // file name will be initial argument
    String filename = args[0];
    Scanner reader;
    try {
      reader = new Scanner(new File(filename));

      // create model and controller
      ImageProcessor processor = new ImageProcessorImpl();
      ImageController controller = new ImageControllerImpl(processor);

      boolean isValidScript = true;
      while (isValidScript && reader.hasNext()) {
        String command = reader.next().toLowerCase();

        try {
          // interpret and perform command
          interpretCommand(command, controller, reader);
          System.out.println(controller.getStatus());
        } catch (Exception e) {
          System.out.println("Unable to parse command '" + command
                  + "' in input script. Please verify proper format asspecified in README.md");
          isValidScript = false;
        }

      }
    } catch (FileNotFoundException e) {
      System.out.println("Could not open file '" + filename + "'");
    }
  }

  /**
   * Interprets command and attempts to execute it via the controller using any additional arguments
   * specified by the reader.
   *
   * @param command    String command for controller to perform
   * @param controller ImageController object to execute commands
   * @param reader     Scanner object holding filestream
   * @throws IllegalStateException if an invalid command is passed in
   */
  private static void interpretCommand(String command, ImageController controller, Scanner reader)
          throws IllegalStateException {

    switch (command) {
      case "load":
        String filename = reader.next();
        controller.loadImage(filename);
        break;
      case "save":
        filename = reader.next();
        controller.saveImage(filename);
        break;
      case "blur":
        controller.doBlur();
        break;
      case "sharpen":
        controller.doSharpen();
        break;
      case "sepia":
        controller.doSepia();
        break;
      case "greyscale":
        controller.doGreyScale();
        break;
      case "rainbow":
        // argument order must be specified as orientation, height, width, colors
        String strOrientation = reader.next().toLowerCase();
        Orientation orientation = getOrientationFromString(strOrientation);

        int height = reader.nextInt();
        int width = reader.nextInt();

        // take colors formatted as single token with commas
        // ex: "Red,Blue,Green"
        List<Color> colors = parseColorsToList(reader.next().toLowerCase());

        controller.doRainbow(orientation, height, width, colors);

        break;
      case "checkerboard":
        // argument order must be specified as size, primary color, secondary color
        int size = reader.nextInt();
        Color primary = parseColorFromString(reader.next().toLowerCase());
        Color secondary = parseColorFromString(reader.next().toLowerCase());

        controller.doCheckerboard(size, primary, secondary);

        break;
      case "dither":
        controller.doDither();
        break;
      case "mosaic":
        // argument will be number of seeds
        int seeds = reader.nextInt();

        controller.doMosaic(seeds);
        break;
      default:
        throw new IllegalStateException("Unsupported command '" + command + "'.");

    }
  }

  /**
   * Parses a String version of an orientation and returns the correct Orientation enum value.
   *
   * @param strOrientation string version of an orientation
   * @return the correct orientation enum value
   * @throws IllegalStateException if an unsupported orientation string is passed in
   */
  private static Orientation getOrientationFromString(String strOrientation)
          throws IllegalStateException {
    switch (strOrientation) {
      case "vertical":
        return Orientation.VERTICAL;
      case "horizontal":
        return Orientation.HORIZONTAL;
      default:
        throw new IllegalStateException(
                "Unsupported orientation '" + strOrientation + "' " + "specified for rainbow "
                        + "command");
    }
  }

  /**
   * Parses a single string and returns a List of Colors.
   *
   * @param strColors string to parse with colors
   * @return List of Color objects
   */
  private static List<Color> parseColorsToList(String strColors) {
    List<Color> colors = new ArrayList<>();
    for (String color : strColors.split(",")) {
      Color col = parseColorFromString(color);
      colors.add(col);
    }
    return colors;
  }

  /**
   * Parses a Color object from its string representation. Uses code written by Erick Robertson
   * from https://stackoverflow.com/questions/2854043/converting-a-string-to-color-in-java.
   *
   * @param strColor string representation of the color
   * @return the Color object representing the string version
   * @throws IllegalArgumentException if an invalid color is passed in
   */
  private static Color parseColorFromString(String strColor) throws IllegalArgumentException {
    Color col;
    try {
      // use reflection to access static member of Color class - according to Erick Robertson
      Field field = Color.class.getField(strColor);
      col = (Color) field.get(null);
    } catch (Exception e) {
      throw new IllegalArgumentException("Unsupported color '" + strColor + "'");
    }
    return col;
  }
}
