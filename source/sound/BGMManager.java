package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * BGMの再生を管理するクラスです。
 */
public class BGMManager {
      private Clip clip;
      private FloatControl volumeControl;

      /**
       * 指定されたパスの音声ファイルを読み込みます。
       * 
       * @param filePath 音声ファイルのパス (例: "../resources/sound/bgm.wav")
       */
      public void load(String filePath) {
            try {
                  File audioFile = new File(filePath);
                  if (!audioFile.exists()) {
                        System.err.println("BGMファイルが見つかりません: " + audioFile.getAbsolutePath());
                        return;
                  }

                  AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                  clip = AudioSystem.getClip();
                  clip.open(audioStream);

                  // 音量コントロールを取得
                  if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        volumeControl =
                                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                  } else {
                        System.err.println("音量コントロールはサポートされていません。");
                  }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                  System.err.println("BGMの読み込みに失敗しました。");
                  e.printStackTrace();
            }
      }

      /**
       * BGMの音量を設定します。
       * 
       * @param volume 0.0 (無音) から 1.0 (最大音量) の間の値
       */
      public void setVolume(float volume) {
            if (volumeControl == null)
                  return;
            // 入力値を 0.0f から 1.0f の範囲にクランプ
            volume = Math.max(0.0f, Math.min(1.0f, volume));
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            volumeControl.setValue(volumeControl.getMinimum() + (range * volume));
      }

      /**
       * 読み込んだBGMをループ再生します。
       */
      public void loop() {
            if (clip != null) {
                  clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
      }

      /**
       * BGMの再生を停止します。
       */
      public void stop() {
            if (clip != null && clip.isRunning()) {
                  clip.stop();
            }
      }
}
