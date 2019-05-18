package model;

import observer.ImageObserver;

/**
 * This interface represents an image processor as an Observable model. Containing methods to
 * implement the observer pattern for the ImageProcessor subject.
 *
 * @author Durga Sivamani, Carlo Mutuc
 * @version 0.1
 */
public interface ImageListener {

  /**
   * Registers the specified observer to this ImageListener.
   *
   * @param observer the observer to register Registers an observer by adding it into the list of
   *                 observers.
   */
  void registerObserver(ImageObserver observer);

  /**
   * Removes the specified observer from this ImageListener.
   *
   * @param observer the observer to remove Removes an observer from the list of observers.
   */
  void removeObserver(ImageObserver observer);

  /**
   * Updates all observers with the specified action/operation that has just been completed.
   *
   * @param completedAction the action/operation that has just been completed
   */
  void updateObservers(String completedAction);
}
