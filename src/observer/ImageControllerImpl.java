package observer;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import model.ImageProcessor;
import utilities.Orientation;

/**
 * This class represents a controller (and observer) of the ImageProcessor class.
 *
 * @author Durga Sivamani, Carlo Mutuc
 */
public class ImageControllerImpl extends AbstractImageObserver implements ImageController {

  /**
   * The ImageProcessor model/subject that this ImageObserver is observing and tracking.
   */
  private ImageProcessor imageProcessor;

  /**
   * Creates a new controller from a specified imageProcessor
   *
   * @param imageProcessor the ImageProcessor object to observe
   */
  public ImageControllerImpl(ImageProcessor imageProcessor) {
    this.imageProcessor = imageProcessor;
    imageProcessor.registerObserver(this);
  }

  @Override
  public void doBlur() {
    imageProcessor.blur();
  }

  @Override
  public void doSharpen() {
    imageProcessor.sharpen();
  }

  @Override
  public void doGreyScale() {
    imageProcessor.greyscale();
  }

  @Override
  public void doSepia() {
    imageProcessor.sepia();
  }

  @Override
  public void doRainbow(Orientation orientation, int height, int width, List<Color> colors)
          throws IllegalStateException {
    switch (orientation) {
      case HORIZONTAL:
        imageProcessor.rainbowHorizontal(height, width, colors);
        break;
      case VERTICAL:
        imageProcessor.rainbowVertical(height, width, colors);
        break;
      default:
        throw new IllegalStateException("Unsupported rainbow orientation.");
    }
  }

  @Override
  public void doCheckerboard(int squareSize, Color primary, Color secondary) {
    imageProcessor.checkerboard(squareSize, primary, secondary);
  }

  @Override
  public void doDither() {
    imageProcessor.dither();
  }

  @Override
  public void doMosaic(int seeds) {
    imageProcessor.mosaic(seeds);
  }

  @Override
  public void loadImage(String name) throws IllegalArgumentException {
    try {
      imageProcessor.load(name);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not load image '" + name + "'");
    }
  }

  @Override
  public void saveImage(String name) {
    imageProcessor.save(name);
  }
}
