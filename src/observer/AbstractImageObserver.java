package observer;


/**
 * This class implements the ImageObserver interface to represent an observer to an ImageListener
 * object.
 *
 * @author Durga Sivamani, Carlo Mutuc
 * @version 0.1
 */
public abstract class AbstractImageObserver implements ImageObserver {

  /**
   * Represents the last completed action of the ImageProcessor model.
   */
  private String status;

  /**
   * Default constructor.
   */
  public AbstractImageObserver() {
    this.status = "";
  }

  @Override
  public void update(String completedAction) {
    this.status = completedAction;
  }

  @Override
  public String getStatus() {
    return this.status;
  }

}
