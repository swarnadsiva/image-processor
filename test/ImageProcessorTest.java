import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.ImageProcessor;
import model.ImageProcessorImpl;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for selected methods in the model.ImageProcessor interface and related subclasses.
 */
public class ImageProcessorTest {

  /**
   * Private variable for testing horizontal rainbow generation.
   */
  private ImageProcessor rainbowH;

  /**
   * Sets up variables for testing.
   */
  @Before
  public void setup() {
    rainbowH = new ImageProcessorImpl();
  }

  /**
   * Tests computeStripeDimensions() method works correctly.
   */
  @Test
  public void testGetStripeSize() {
    assertEquals(342, ImageProcessor.computeStripeDimensions(1024, 3));
    assertEquals(256, ImageProcessor.computeStripeDimensions(1024, 4));
    assertEquals(70, ImageProcessor.computeStripeDimensions(629, 9));

    // vertical
    assertEquals(342, ImageProcessor.computeStripeDimensions(1024, 3));
    assertEquals(384, ImageProcessor.computeStripeDimensions(1920, 5));
    assertEquals(154, ImageProcessor.computeStripeDimensions(767, 5));
  }

  /**
   * Tests invalid height parameter for rainbowHorizontal().
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetHorizontalStripeSizeInvalidHeight() {
    List<Color> colors = new ArrayList<>();
    colors.add(Color.white);
    rainbowH.rainbowHorizontal(0, 8, colors);
  }

  /**
   * Tests invalid width parameter for rainbowHorizontal().
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetHorizontalStripeSizeInvalidWidth() {
    List<Color> colors = new ArrayList<>();
    colors.add(Color.white);
    rainbowH.rainbowHorizontal(1920, 0, colors);
  }

  /**
   * Tests invalid color parameter for rainbowHorizontal().
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetHorizontalStripeSizeInvalidColors() {
    rainbowH.rainbowHorizontal(1920, 780, new ArrayList<>());
  }

}