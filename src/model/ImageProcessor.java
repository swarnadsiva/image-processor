package model;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

/**
 * This interface represents an image processing utility. It contains methods to process / generate
 * images that are implemented in the concrete class model.ImageProcessorImpl.
 *
 * @author Carlo Mutuc, Durga Sivamani
 * @version 0.2
 */
public interface ImageProcessor extends ImageListener {

  /**
   * Blurs the current image.
   */
  void blur();

  /**
   * Sharpens the current image.
   */
  void sharpen();

  /**
   * Transforms the image color to a sepia tint.
   */
  void sepia();

  /**
   * Transforms the image color to shades of grey.
   */
  void greyscale();

  /**
   * Generates an image with the specified image colors as a horizontal rainbow according to the
   * specified width and height.
   *
   * @param height the height of the image to generate
   * @param width  the width of the image to generate
   * @param colors the colors of the rainbow in the image to generate
   */
  void rainbowHorizontal(int height, int width, List<Color> colors);

  /**
   * Generates an image with the specified image colors as a vertical rainbow according to the
   * specified width and height.
   *
   * @param height the height of the image to generate
   * @param width  the width of the image to generate
   * @param colors the colors of the rainbow in the image to generate
   */
  void rainbowVertical(int height, int width, List<Color> colors);

  /**
   * Generates an image with the specified colors as a checkerboard pattern according to the
   * specified square size.
   *
   * @param squareSize     the size of the image (this will be used to create an image as a square)
   * @param primaryColor   the primary color of the checkerboard
   * @param secondaryColor the secondary color of the checkerboard
   */
  void checkerboard(int squareSize, Color primaryColor, Color secondaryColor);

  /**
   * Dithers the current image by modifying it to black and white spots.
   */
  void dither();

  /**
   * Performs a "stained-glass" effect on the image with a specified number of seeds indicating the
   * number of "glass pieces".
   *
   * @param seeds the number of sets of points in the image representing the "glass pieces" in the
   *              mosaic.
   */
  void mosaic(int seeds);

  /**
   * Saves the current image as a specified file in the res/ folder.
   *
   * @param filename the filename to save the image
   */
  void save(String filename);

  /**
   * Loads the specified image from the file.
   *
   * @param filename file to load
   * @throws IOException if the file is not found
   */
  void load(String filename) throws IOException;

  /**
   * Computes and returns the correct stripe size from a given size (width or height) and number of
   * stripes. If the number of stripes cannot evenly divide the size, a stripe size is computed to
   * ensure all stripes are the same size, except the last which will be up to 7 pixels shorter.
   *
   * @param size       the size of the image (width or height) in pixels
   * @param numStripes the number of stripes that must be drawn for that size
   * @return the correct stripe size for the image
   */
  static int computeStripeDimensions(int size, int numStripes) {
    while (size % numStripes != 0) {
      size++;
    }

    return size / numStripes;
  }
}
