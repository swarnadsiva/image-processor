import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.LinkedList;

import observer.ImageController;
import observer.ImageControllerImpl;
import model.ImageProcessor;
import model.MockImageProcessorImpl;
import utilities.Orientation;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for the ImageController and related subclasses.
 */
public class ImageControllerTest {

  /**
   * Private variable for testing.
   */
  private ImageController a_Controller;

  /**
   * Sets up variables for testing.
   */
  @Before
  public void setup() {
    ImageProcessor mock = new MockImageProcessorImpl();
    a_Controller = new ImageControllerImpl(mock);
  }

  /**
   * Tests doBlur().
   */
  @Test
  public void testBlurImage() {
    a_Controller.doBlur();
    assertEquals("blurred image", a_Controller.getStatus());
  }

  /**
   * Tests doSharpen().
   */
  @Test
  public void testSharpenImage() {
    a_Controller.doSharpen();
    assertEquals("sharpened image", a_Controller.getStatus());
  }

  /**
   * Tests doSepia().
   */
  @Test
  public void testDoSepia() {
    a_Controller.doSepia();
    assertEquals("transformed image to sepia color", a_Controller.getStatus());
  }

  /**
   * Tests doGreyscale().
   */
  @Test
  public void testDoGreyScale() {
    a_Controller.doGreyScale();
    assertEquals("transformed image to greyscale color", a_Controller.getStatus());
  }

  /**
   * Tests doRainbow() horizontally.
   */
  @Test
  public void testDoRainbowHorizontal() {
    LinkedList<Color> colors = new LinkedList<>();
    colors.add(Color.CYAN);
    colors.add(Color.BLACK);
    colors.add(Color.WHITE);
    a_Controller.doRainbow(Orientation.HORIZONTAL, 500, 500, colors);
    assertEquals("created horizontal rainbow 500x500", a_Controller.getStatus());
  }

  /**
   * Tests doRainbow() vertically.
   */
  @Test
  public void testDoRainbowVertical() {
    LinkedList<Color> colors = new LinkedList<>();
    colors.add(Color.CYAN);
    colors.add(Color.WHITE);
    a_Controller.doRainbow(Orientation.VERTICAL, 896, 1235, colors);
    assertEquals("created vertical rainbow 896x1235", a_Controller.getStatus());
  }

  /**
   * Tests doCheckerboard().
   */
  @Test
  public void testDoCheckerboard() {
    a_Controller.doCheckerboard(399, Color.BLACK, Color.RED);
    assertEquals("created checkerboard 399x399", a_Controller.getStatus());
  }

  /**
   * Tests doDither().
   */
  @Test
  public void testDither() {
    a_Controller.doDither();
    assertEquals("added dither effect to image", a_Controller.getStatus());
  }

  /**
   * Tests doMosaic().
   */
  @Test
  public void testMosaic() {
    a_Controller.doMosaic(305);
    assertEquals("added mosaic effect to image with 305 seeds", a_Controller.getStatus());
  }

  /**
   * Tests doSave().
   */
  @Test
  public void testSave() {
    a_Controller.saveImage("test_save.png");
    assertEquals("saved image 'test_save.png'", a_Controller.getStatus());
  }

  /**
   * Tests loadImage().
   */
  @Test
  public void testLoadImage() {
    a_Controller.loadImage("test_load.png");
    assertEquals("loaded image 'test_load.png'", a_Controller.getStatus());
  }
}