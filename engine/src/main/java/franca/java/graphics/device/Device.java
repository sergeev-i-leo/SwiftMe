package franca.java.graphics.device;

import franca.java.graphics.animations.Animation;

public class Device {

  // animation orchestration

  private int lastAnimationId = 0;
  private Animation firstAnimation = null;

  public long getTime() {
    return 0L;
  }

  public void readFile(String path, StringConsumer callback) {
  }

  public void writeFile(String path, String content, IntegerConsumer callback) {
  }

  public synchronized void requestRepainting() {
    // one-shot animation
    Animation animation = new Animation(0f, 0f, 1L);
    registerAnimation(animation);
  }

  public synchronized void registerAnimation(Animation animation) {

    // the new animation becomes the first in the chain because registerAnimation can be called from Animation.needsRepainting

    lastAnimationId++;
    animation.animationId = lastAnimationId;
    animation.registeredTime = getTime();

    animation.nextAnimation = firstAnimation;
    if (firstAnimation != null) {
      firstAnimation.previousAnimation = animation;
    }
    firstAnimation = animation;

    startRepainting();
  }

  public void startRepainting() {
  }

  public synchronized void removeAnimation(Animation animation) {

    Animation currentAnimation = firstAnimation;
    while (currentAnimation != null) {
      if (currentAnimation.animationId == animation.animationId) {
        Animation previousAnimation = currentAnimation.previousAnimation;
        Animation nextAnimation = currentAnimation.nextAnimation;

        if (previousAnimation != null) {
          previousAnimation.nextAnimation = nextAnimation;
        }
        if (nextAnimation != null) {
          nextAnimation.previousAnimation = previousAnimation;
        }
        if (currentAnimation == firstAnimation) {
          firstAnimation = nextAnimation;
        }
        break;
      }
      currentAnimation = currentAnimation.nextAnimation;
    }
    if (firstAnimation == null) {
      lastAnimationId = 0;
    }
  }

  public synchronized boolean needsRepainting() {

    boolean result = false;

    long time = getTime();

    Animation currentAnimation = firstAnimation;
    while (currentAnimation != null) {
      result = currentAnimation.needsRepainting(this, time) || result;
      if (currentAnimation.duration == 1L) {
        // it's one-shot animation
        Animation previousAnimation = currentAnimation.previousAnimation;
        Animation nextAnimation = currentAnimation.nextAnimation;
        if (previousAnimation != null) {
          previousAnimation.nextAnimation = nextAnimation;
        }
        if (nextAnimation != null) {
          nextAnimation.previousAnimation = previousAnimation;
        }
        if (currentAnimation.animationId == firstAnimation.animationId) {
          firstAnimation = nextAnimation;
        }
        currentAnimation.destroy();
        currentAnimation = nextAnimation;
      } else {
        currentAnimation = currentAnimation.nextAnimation;
      }
    }
    return result;
  }

  public synchronized boolean needsNextRepainting() {

    boolean result = false;

    long time = getTime();

    Animation currentAnimation = firstAnimation;
    while (currentAnimation != null) {
      if (currentAnimation.duration == 1L) {
        // wow! one-shot animation has been created during paint(), welcome to next needsRepainting
        result = true;
      } else {
        // always run needsNextRepainting
        result = currentAnimation.needsNextRepainting(this, time) || result;
      }
      currentAnimation = currentAnimation.nextAnimation;
    }
    return result;
  }

}

