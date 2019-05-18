package observer;

/**
 * This interface represents an image controller as an Observer to an ImageListener (i.e. the image
 * processor model).
 *
 * @author Durga Sivamani, Carlo Mutuc
 * @version 0.1
 */
public interface ImageObserver {

  /**
   * Updates this ImageObserver with the completed action of the ImageListener.
   *
   * @param completedAction the action / operation that has just been completed
   */
  void update(String completedAction);

  /**
   * Returns the status of the ImageProcessor model.
   *
   * @return the status of the ImageProcessor model
   */
  String getStatus();
}
