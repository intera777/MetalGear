package sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BGMManager {
      private Clip clip;

      // 音声ファイルを読み込む
      public void load(String path) {
            try {
                  // JAR内のリソースとして取得
                  URL url = getClass().getResource(path);
                  if (url == null) {
                        System.err.println("BGMが見つかりません: " + path);
                        return;
                  }
                  AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                  clip = AudioSystem.getClip();
                  clip.open(ais);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                  e.printStackTrace();
            }
      }

      // 音量を設定する
      public void setVolume(float volume) {
            if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                  FloatControl gainControl =
                              (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                  float range = gainControl.getMaximum() - gainControl.getMinimum();
                  float gain = (range * volume) + gainControl.getMinimum();
                  gainControl.setValue(gain);
            }
      }

      // ループ再生
      public void loop() {
            if (clip != null) {
                  clip.setFramePosition(0);
                  clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
      }

      // 停止
      public void stop() {
            if (clip != null && clip.isRunning()) {
                  clip.stop();
            }
      }

      public void close() {
            if (clip != null)
                  clip.close();
      }
}
