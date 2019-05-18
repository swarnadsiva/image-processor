package observer;

import java.awt.Color;
import java.util.List;

import utilities.Orientation;

/**
 * Represents a controlling object for the ImageProcessor model.
 *
 * @author Durga Sivamani, Carlo Mutuc
 * @version 0.1
 */
public interface ImageController extends ImageObserver {

  /**
   * Sends a call to the ImageProcessor to blur the observed image.
   */
  void doBlur();

  /**
   * Sends a call to the ImageProcessor to sharpen the observed image.
   */
  void doSharpen();

  /**
   * Sends a call to the ImageProcessor to greyscale the observed image.
   */
  void doGreyScale();

  /**
   * Sends a call to the ImageProcessor to sepia the observed image.
   */
  void doSepia();

  /**
   * Sends a call to the ImageProcessor to create a rainbow with a specified orientation, height,
   * width and colors.
   *
   * @param orientation orientation of the rainbow stripes (horizontal or vertical)
   * @param height      the desired height of the rainbow image
   * @param width       the desired width of the rainbow image
   * @param colors      the colors of the rainbow stripes
   * @throws IllegalStateException if the orientation is of an unsupported value
   */
  void doRainbow(Orientation orientation, int height, int width, List<Color> colors)
          throws IllegalStateException;

  /**
   * Sends a call to the ImageProcessor to create a checkerboard with a specified size, primary and
   * secondary colors.
   *
   * @param squareSize the desired height and width of the image
   * @param primary    the primary color of the checkerboard
   * @param secondary  the secondary color of the checkerboard
   */
  void doCheckerboard(int squareSize, Color primary, Color secondary);

  /**
   * Sends a call to the ImageProcessor to dither the observed image.
   */
  void doDither();

  /**
   * Sends a call to the ImageProcessor to mosaic the observed image.
   *
   * @param seeds the number of sets of points representing the "glass pieces" in the mosaic
   */
  void doMosaic(int seeds);

  /**
   * Sends a call to the ImageProcess to create/load the specified image.
   *
   * @param name the image file to load
   * @throws IllegalArgumentException if the specified file cannot be loaded
   */
  void loadImage(String name) throws IllegalArgumentException;

  /**
   * Sends a call to the ImageProcessor to save the specified image.
   *
   * @param name the name of the image to save as
   */
  void saveImage(String name);
}
