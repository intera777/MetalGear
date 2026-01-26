package sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 効果音（一度だけ再生するClip）の読み込みと再生を管理するユーティリティクラスです。
 */
public final class SoundEffectManager {

      /**
       * インスタンス化を防止するためのプライベートコンストラクタ。
       */
      private SoundEffectManager() {}

      /**
       * 指定されたパスからオーディオクリップを読み込み、音量を設定します。
       * 
       * @param path オーディオファイルのパス
       * @param volume 音量 (0.0f - 1.0f)
       * @return 読み込んだClipオブジェクト、失敗した場合はnull
       */
      public static Clip loadClip(String path, float volume) {
            try {
                  File audioFile = new File(path);
                  if (!audioFile.exists()) {
                        System.err.println("SEファイルが見つかりません: " + audioFile.getAbsolutePath());
                        return null;
                  }
                  AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                  Clip clip = AudioSystem.getClip();
                  clip.open(audioStream);

                  // 音量調整
                  if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl gainControl =
                                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float range = gainControl.getMaximum() - gainControl.getMinimum();
                        float gain = (range * volume) + gainControl.getMinimum();
                        gainControl.setValue(gain);
                  }
                  return clip;
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                  System.err.println("SEファイルの読み込みに失敗: " + path);
                  e.printStackTrace();
                  return null;
            }
      }

      /**
            * ファイルパスを指定して、音を重ねて再生します。
      * 呼び出しのたびに新しいスレッドでClipを生成するため、音が重なります。
      */
      public static void playOverlap(String path, float volume) {
            new Thread(() -> {
                  try {
                        File audioFile = new File(path);
                        try (AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile)) {
                              Clip clip = AudioSystem.getClip();
                              clip.open(ais);

                              // 音量設定
                              if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                                    float range = gainControl.getMaximum() - gainControl.getMinimum();
                                    float gain = (range * volume) + gainControl.getMinimum();
                                    gainControl.setValue(gain);
                              }

                              // 終わったらリソースを解放
                              clip.addLineListener(event -> {
                                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                                          clip.close();
                                    }
                              });

                              clip.start();
                        }
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
            }).start();
      }
}
