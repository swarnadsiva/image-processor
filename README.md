# Image Controller
This program can generate rainbow striped images and also process existing images with the following filters:
* sepia
* greyscale
* blur
* sharpen
* dither
* mosaic

## Programmers
Carlo Mutuc
Durga Sivamani

## Usage
Create a .txt file with instructions of what you want to do and include that in a directory with the .jar file. For example, to blur an image, the text file commands would be:
> load (image_name)  
> blur  
> save (name)

Please note that when generating an image you do not need to load. You also can apply multiple filters on image, however you need load the original image again if you would like to apply a different filter.

Below are the commands available and the syntax to use them:
```java
load <image_name> # image must be in the same directory as jar file
save <image_name>
blur
sharpen
sepia
greyscale
rainbow <orienation, height, width, colors> # eg. rainbow vertical 500 200 red,green,blue
checkerboard <size, primary color, secondary color> # eg. checkerboard 1000 white black
dither
mosaic <num_of_seeds>
```

## Program Design
Created a controller class, ImageController, to handle input and output between our driver, ImageDriver, and model, ImageProcessor. In addition, we implemented the observer pattern where the controller is the observer and the model is the subject. The observer pattern interfaces, ImageObserver and ImageListener, are separate from the controller and model interface so that we would have to make minimal changes to existing code.
