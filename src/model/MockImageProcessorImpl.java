package model;

import java.awt.Color;
import java.util.List;

/**
 * A mock class of the ImageProcessor interface for use in testing.
 *
 * @author Durga Sivamani, Carlo Mutuc
 * @version 0.1
 */
public class MockImageProcessorImpl extends AbstractImageListener implements ImageProcessor {

  @Override
  public void blur() {
    updateObservers("blurred image");
  }

  @Override
  public void sharpen() {
    updateObservers("sharpened image");
  }

  @Override
  public void sepia() {
    updateObservers("transformed image to sepia color");
  }

  @Override
  public void greyscale() {
    updateObservers("transformed image to greyscale color");
  }

  @Override
  public void rainbowHorizontal(int height, int width, List<Color> colors) {
    updateObservers("created horizontal rainbow " + height + "x" + width);
  }

  @Override
  public void rainbowVertical(int height, int width, List<Color> colors) {
    updateObservers("created vertical rainbow " + height + "x" + width);
  }

  @Override
  public void checkerboard(int squareSize, Color primaryColor, Color secondaryColor) {
    updateObservers("created checkerboard " + squareSize + "x" + squareSize);
  }

  @Override
  public void dither() {
    updateObservers("added dither effect to image");
  }

  @Override
  public void mosaic(int seeds) {
    updateObservers("added mosaic effect to image with " + seeds + " seeds");
  }

  @Override
  public void save(String filename) {
    updateObservers("saved image '" + filename + "'");
  }

  @Override
  public void load(String filename) {
    updateObservers("loaded image '" + filename + "'");
  }
}
