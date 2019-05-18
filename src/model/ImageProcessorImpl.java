package model;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utilities.ImageUtil;
import utilities.Orientation;

/**
 * This class implements the model.ImageProcessor interface to filter, color transform, and generate
 * various images.
 *
 * @author Carlo Mutuc, Durga Sivamani
 * @version 0.2
 */
public class ImageProcessorImpl extends AbstractImageListener implements ImageProcessor {

  /**
   * Private field representing an image as 3D integer array (height, width, channels).
   */
  private int[][][] image;

  /**
   * Kernel applied to this image to blur it.
   */
  private static final double[][] blurKernel =
          {{1.0 / 16, 1.0 / 8, 1.0 / 16}, {1.0 / 8, 1.0 / 4, 1.0 / 8},
                  {1.0 / 16, 1.0 / 8, 1.0 / 16}};

  /**
   * Kernel applied ot this image to sharpen it.
   */
  private static final double[][] sharpenKernel =
          {{-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
                  {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
                  {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
                  {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
                  {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
                  {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
                  {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}};

  // Constants
  private static final int RED = 0;
  private static final int GREEN = 1;
  private static final int BLUE = 2;
  private static final int MAX_CHANNELS = 3;

  /**
   * Constant representing matrix values for greyscale color transformation.
   */
  private static final double[][] GREYSCALE =

          {{0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}};

  /**
   * Constant representing matrix values for sepia color transformation.
   */
  private static final double[][] SEPIA =
          {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};

  /**
   * Represents value to add as change for dithering right pixel.
   */
  private static final float DITHER_RIGHT = 7.0f / 16.0f;

  /**
   * Represents value to add as change for dithering diagonal bottom-left pixel.
   */
  private static final float DITHER_BELOW_LEFT = 3.0f / 16.0f;

  /**
   * Represents value to add as change for dithering pixel directly below.
   */
  private static final float DITHER_BELOW = 5.0f / 16.0f;

  /**
   * Represents value to add as change for dithering diagonal bottom-right pixel.
   */
  private static final float DITHER_BELOW_RIGHT = 1.0f / 16.0f;

  /**
   * Default constructor, initializes a 0 x 0 x 3 array.
   */
  public ImageProcessorImpl() {
    this(0, 0);
  }


  /**
   * Constructor that takes a filename and creates an model.ImageProcessor by reading the pixels of
   * the image.
   *
   * @param filename name fo the image file
   * @throws IOException if the file is not found
   */
  public ImageProcessorImpl(String filename) throws IOException {
    load(filename);
  }

  /**
   * Protected constructor that creates an model.ImageProcessor from a specified width and height.
   *
   * @param width  width of image
   * @param height height of image
   */
  protected ImageProcessorImpl(int width, int height) {
    this.image = new int[height][width][MAX_CHANNELS];
  }

  @Override
  public void blur() {
    this.image = filter(blurKernel);
    updateObservers("blurred image");
  }

  @Override
  public void sharpen() {
    this.image = filter(sharpenKernel);
    updateObservers("sharpened image");
  }

  @Override
  public void sepia() {
    transform(SEPIA);
    updateObservers("transformed image to sepia color");
  }

  @Override
  public void greyscale() {
    transform(GREYSCALE);
    updateObservers("transformed image to greyscale color");
  }

  @Override
  public void dither() {
    // first convert to greyscale
    transform(GREYSCALE);
    int width = getImageWidth();
    int height = getImageHeight();

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        // get the red/green/blue component of this pixel (they are all the same since it is
        // greyscale)
        int oldColor = image[row][col][RED];
        int newColor = oldColor > Math.abs(oldColor - 255) ? 255 : 0;
        int error = oldColor - newColor;

        // set color of current pixel to new color
        int[] newPixel = {newColor, newColor, newColor};
        setPixelRGB(newPixel, row, col);

        // now add error values to surrounding pixels
        // add to pixel on right
        if (col + 1 < width) {
          setDitherChange(row, col + 1, DITHER_RIGHT, error);
        }

        // add to pixel on next row left
        if (row + 1 < height && col - 1 >= 0) {
          setDitherChange(row + 1, col - 1, DITHER_BELOW_LEFT, error);
        }

        // add to pixel on below in next row
        if (row + 1 < height) {
          setDitherChange(row + 1, col, DITHER_BELOW, error);
        }

        // add to pixel on on next row right
        if (row + 1 < height && col + 1 < width) {
          setDitherChange(row + 1, col + 1, DITHER_BELOW_RIGHT, error);
        }
      }
    }

    updateObservers("added dither effect to image");
  }

  /**
   * Adds the specified change amount to a specified pixel as part of the dithering algorithm.
   *
   * @param row         row index of image
   * @param col         column index of image
   * @param changeValue change value specific to the pixel position
   * @param error       error calculation to add to the specified pixel
   */
  private void setDitherChange(int row, int col, float changeValue, int error) {
    int change = Math.round(image[row][col][RED] + (changeValue * error));
    int[] newCol = {change, change, change};
    setPixelRGB(newCol, row, col);
  }

  //////////////////////////////////////////////////////////////////////
  // Rainbow methods
  //////////////////////////////////////////////////////////////////////

  @Override
  public void rainbowHorizontal(int height, int width, List<Color> colors) {
    drawStripes(height, width, colors, Orientation.HORIZONTAL);
    updateObservers("created horizontal rainbow");
  }

  @Override
  public void rainbowVertical(int height, int width, List<Color> colors) {
    drawStripes(height, width, colors, Orientation.VERTICAL);
    updateObservers("created vertical rainbow");
  }

  @Override
  public void checkerboard(int squareSize, Color primaryColor, Color secondaryColor) {

    int size = squareSize * 8;
    this.image = new int[size][size][3];

    for (int row = 0; row < size; row++) {
      for (int column = 0; column < size; column++) {

        boolean check1 = column / squareSize % 2 == 0;
        boolean check2 = row / squareSize % 2 == 0;

        if (check1 == check2) {
          setPixelRGB(primaryColor, row, column);
        } else {
          setPixelRGB(secondaryColor, row, column);
        }
      }
    }

    updateObservers("created checkerboard");
  }

  //////////////////////////////////////////////////////////////////////
  // Utility methods
  //////////////////////////////////////////////////////////////////////

  @Override
  public void save(String filename) {
    StringBuilder output = new StringBuilder();
    String path = "res/" + filename;

    try {
      ImageUtil.writeImage(image, image[0].length, image.length, path);
      output.append("saved file '");
      output.append(filename);
      output.append("'");
    } catch (IOException e) {
      output.append("unable to write file: ");
      output.append(e.getMessage());
    }

    updateObservers(output.toString());
  }

  @Override
  public void load(String filename) throws IOException {
    this.image = ImageUtil.readImage(filename);
  }

  /**
   * Performs a color transformation on the image based on a specified kernel.
   *
   * @param kernel a 2D double array representing the kernel to transform the image color
   */
  private void transform(double[][] kernel) {
    int height = image.length;
    int width = image[0].length;

    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {

        int r = image[row][column][RED];
        int g = image[row][column][GREEN];
        int b = image[row][column][BLUE];

        int[] newRGB = multiply(kernel, new int[]{r, g, b});

        setPixelRGB(newRGB, row, column);
      }
    }
  }

  /**
   * Performs matrix multiplication on a specified kernel and rgb integer array.
   *
   * @param kernel a 2D double array representing the kernel
   * @param rgb    the current RGB component values in a 1D integer array
   * @return a new RGB component as a 1D integer array
   */
  private int[] multiply(double[][] kernel, int[] rgb) {
    int[] newRgb = {0, 0, 0};
    for (int r = 0; r < kernel.length; r++) {
      for (int c = 0; c < kernel[0].length; c++) {
        newRgb[r] += rgb[c] * kernel[r][c];
      }
      newRgb[r] = clamp(newRgb[r], 0, 255);
    }
    return newRgb;
  }

  /**
   * Clamps integer value to a specified range.
   *
   * @param value value to clamp
   * @param min   minimum value allowed
   * @param max   maximum value allowed
   * @return clamped value
   */
  private int clamp(int value, int min, int max) {
    if (value < min) {
      value = min;
    } else if (value > max) {
      value = max;
    }
    return value;
  }

  /**
   * Generates a striped image according to specified height and width dimensions, colors and
   * orientation.
   *
   * @param height      height of image
   * @param width       width of image
   * @param colors      colors of stripes
   * @param orientation orientation of stripes (horizontal or vertical)
   * @throws IllegalArgumentException if height, width or color List is invalid
   */
  protected void drawStripes(int height, int width, List<Color> colors, Orientation orientation)
          throws IllegalArgumentException {

    if (invalidStripeParameters(height, width, colors)) {
      throw new IllegalArgumentException(
              "Invalid arguments to generate a horizontal rainbow. Image size must be greater "
                      + "than zero, and at least one color must be specified.");
    }

    // determine how many stripes by the number of colors
    int numStripes = colors.size();

    // create image with correct dimensions
    image = new int[height][width][MAX_CHANNELS];

    // determine stripe size from orientation
    int stripeSize;

    switch (orientation) {
      case HORIZONTAL:
        // get stripe size and loop according to HEIGHT
        stripeSize = ImageProcessor.computeStripeDimensions(height, numStripes);
        loop(stripeSize, height, width, orientation, colors);
        break;
      case VERTICAL:
        // get stripe size and loop according to WIDTH
        stripeSize = ImageProcessor.computeStripeDimensions(width, numStripes);
        loop(stripeSize, width, height, orientation, colors);
        break;
      default:
        throw new IllegalStateException("Invalid orientation.");
    }
  }

  /**
   * Loops over the image integer array to draw the stripes according to the specified first and
   * second loop condition variables.
   *
   * @param stripeSize  the size of each stripe in pixels
   * @param firstLoop   the first loop condition
   * @param secondLoop  the second loop condition
   * @param orientation the orientation of the stripes
   * @param colors      the colors in the stripes
   */
  private void loop(int stripeSize, int firstLoop, int secondLoop, Orientation orientation,
                    List<Color> colors) {

    int pixelCount = 0;
    int colorIndex = 0;
    int lastColorIndex = colors.size() - 1;
    Color current = colors.get(colorIndex);

    for (int i = 0; i < firstLoop; i++) {

      for (int j = 0; j < secondLoop; j++) {

        switch (orientation) {
          case HORIZONTAL:
            // pass i as height, j as width according to loop order
            setPixelRGB(current, i, j);
            break;
          case VERTICAL:
            // pass j as height, i as width according to loop order
            setPixelRGB(current, j, i);
            break;
          default:
            throw new IllegalStateException("Invalid orientation.");
        }

      }

      pixelCount++;
      // if this is not the last stripe / color
      if (colorIndex < lastColorIndex) {

        // if we have reached the correct stripe size
        if (pixelCount == stripeSize) {

          // reset the counter
          pixelCount = 0;

          // go to the next color
          colorIndex++;
          current = colors.get(colorIndex);
        }
      }
    }
  }

  /**
   * Sets the image integer array pixel RGB values according to a specified color and pixel to set
   * (determined by row and column positions).
   *
   * @param current color to set the pixel
   * @param row     height position of pixel
   * @param column  width position of pixel
   */
  private void setPixelRGB(Color current, int row, int column) {
    image[row][column][RED] = current.getRed();
    image[row][column][GREEN] = current.getGreen();
    image[row][column][BLUE] = current.getBlue();
  }

  /**
   * Sets the image integer array pixel RGB values according to specified color RGB integer values
   * (between 0-255) and pixel to set (determined by row and column positions).
   *
   * @param rgb    integer array with red, green and blue components of color to set pixel
   * @param row    height position of pixel
   * @param column width position of pixel
   */
  private void setPixelRGB(int[] rgb, int row, int column) {
    image[row][column][RED] = rgb[RED];
    image[row][column][GREEN] = rgb[GREEN];
    image[row][column][BLUE] = rgb[BLUE];
  }

  /**
   * Determines if the specified rainbow parameters are invalid.
   *
   * @param height height of image to generate
   * @param width  width of image to generate
   * @param colors color list to use when generating rainbow
   * @return true if any parameters are invalid, false if not
   */
  private boolean invalidStripeParameters(int height, int width, List<Color> colors) {
    return height < 1 || width < 1 || colors.size() < 1;
  }

  /**
   * Helper method for blur and sharpen. Applies the respective kernel to this
   * model.ImageProcessor.
   *
   * @param kernel the kernel to be applied.
   * @return 3D matrix of the applied
   */
  private int[][][] filter(double[][] kernel) {

    int width = image[0].length;
    int height = image.length;
    int[][][] result = new int[height][width][MAX_CHANNELS];

    for (int channel = 0; channel < MAX_CHANNELS; channel++) {
      for (int row = 0; row < height; row++) {
        for (int column = 0; column < width; column++) {

          int[][] imageKernel = createImageKernel(kernel, row, column, channel);
          double newPixel = 0;

          // Apply kernel to each pixel
          int i;
          int j;
          for (i = 0; i < kernel.length; i++) {
            for (j = 0; j < kernel.length; j++) {
              newPixel += kernel[i][j] * imageKernel[i][j];
            }
          }

          result[row][column][channel] = clamp((int) newPixel, 0, 255);

        }
      }
    }

    return result;
  }

  /**
   * Finds the 2D array around a pixel that is the same size as the kernel so that the kernel can be
   * applied.
   *
   * @param kernel  the kernel to apply
   * @param row     row of the pixel
   * @param column  column of the pixel
   * @param channel color channel of the pixel
   * @return 2D array found around the pixel
   */
  private int[][] createImageKernel(double[][] kernel, int row, int column, int channel) {

    int size = kernel.length;
    int range = size / 2;
    int[][] result = new int[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {

        int rowImage = row - range + i;
        int columnImage = column - range + j;

        //If the index is out of bounds for the image, set the imageKernel value as 0
        if (rowImage < 0 || columnImage < 0 || rowImage >= image.length
                || columnImage >= image[0].length) {
          result[i][j] = 0;
        } else {
          result[i][j] = image[rowImage][columnImage][channel];
        }

      }
    }

    return result;
  }

  @Override
  public void mosaic(int seeds) {

    int width = image[0].length;
    int height = image.length;
    List<int[]> seedList = pickRandomPixels(seeds);


    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {

        int[] imagePixel = {row, column};
        int[] seedPixel = seedList.get(0);
        int[] closestSeedPixel = seedPixel;
        double distance = distanceBetween(seedPixel, imagePixel);

        for (int numSeeds = 1; numSeeds < seeds; numSeeds++) {

          seedPixel = seedList.get(numSeeds);
          double currentDistance = distanceBetween(seedPixel, imagePixel);

          if (currentDistance < distance) {
            distance = currentDistance;
            closestSeedPixel = seedList.get(numSeeds);
          }
        }

        image[row][column] = image[closestSeedPixel[0]][closestSeedPixel[1]];
      }
    }
    updateObservers("added mosaic effect to image");
  }

  private double distanceBetween(int[] current, int[] other) {
    return Math.sqrt(Math.pow((current[0] - other[0]), 2) + Math.pow(current[1] - other[1], 2));
  }

  /**
   *
   */
  private List<int[]> pickRandomPixels(int seeds) {

    int width = image[0].length;
    int height = image.length;

    List<int[]> pixels = new ArrayList<>();
    Random randomGen = new Random();

    int i = 0;
    while (i < seeds) {

      // Generate two random numbers
      int rand1 = randomGen.nextInt(width);
      int rand2 = randomGen.nextInt(height);
      int[] randResult = {rand2, rand1};

      if (!pixels.contains(randResult)) {
        pixels.add(i, randResult);
        i++;
      }
    }

    //System.out.println(pixels.size());
    return pixels;
  }

  private int getImageWidth() {
    return image[0].length;
  }

  private int getImageHeight() {
    return image.length;
  }
}