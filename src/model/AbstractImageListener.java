package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import observer.ImageObserver;

/**
 * This class implements the ImageListener interface to encapsulate image processors as
 * observable models.
 *
 * @author Durga Sivamani, Carlo Mutuc
 * @version 0.1
 */
public abstract class AbstractImageListener implements ImageListener {

  /**
   * List of observers.
   */
  private List<ImageObserver> observerList;

  /**
   * Default constructor, creates an empty list.
   */
  public AbstractImageListener() {
    observerList = new ArrayList<>();
  }

  @Override
  public void registerObserver(ImageObserver observer) {
    // ensure the observer is not already observing
    if (!observerList.contains(observer)) {
      observerList.add(observer);
    }
  }

  @Override
  public void removeObserver(ImageObserver observer) {
    observerList.remove(observer);
  }

  @Override
  public void updateObservers(String completedAction) {
    for (ImageObserver o : observerList) {
      o.update(completedAction);
    }
  }

}
