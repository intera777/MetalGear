package sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundEffectManager {

      private SoundEffectManager() {}

      public static Clip loadClip(String path, float volume) {
            try {
                  URL url = SoundEffectManager.class.getResource(path);
                  if (url == null) {
                        System.err.println("SEファイルが見つかりません: " + path);
                        return null;
                  }
                  AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                  Clip clip = AudioSystem.getClip();
                  clip.open(audioStream);

                  if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl gainControl =
                                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float range = gainControl.getMaximum() - gainControl.getMinimum();
                        float gain = (range * volume) + gainControl.getMinimum();
                        gainControl.setValue(gain);
                  }
                  return clip;
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                  e.printStackTrace();
                  return null;
            }
      }

      public static void playOverlap(String path, float volume) {
            new Thread(() -> {
                  try {
                        URL url = SoundEffectManager.class.getResource(path);
                        if (url == null) {
                              System.err.println("SEが見つかりません: " + path);
                              return;
                        }
                        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                        Clip clip = AudioSystem.getClip();
                        clip.open(ais);

                        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                              FloatControl gainControl = (FloatControl) clip
                                          .getControl(FloatControl.Type.MASTER_GAIN);
                              float range = gainControl.getMaximum() - gainControl.getMinimum();
                              float gain = (range * volume) + gainControl.getMinimum();
                              gainControl.setValue(gain);
                        }

                        clip.addLineListener(event -> {
                              if (event.getType() == LineEvent.Type.STOP) {
                                    clip.close();
                                    try {
                                          ais.close();
                                    } catch (IOException e) {
                                          e.printStackTrace();
                                    }
                              }
                        });

                        clip.start();
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
            }).start();
      }
}
