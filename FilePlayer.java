import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Classs that will play music files at given paths
 * 
 * @author Jesus Molina
 *
 */
public class FilePlayer {

	/**
	 * Plays an audio clip located at the given path
	 * 
	 * @param filePath
	 *            the path to the audio clip that should be played
	 */
	public void play(String filePath) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();

		} catch (Exception e) {
			System.out.println("Error with playing sound.");
			e.printStackTrace();
		}

	}

	/**
	 * Plays an audio clip located at the given path and waits until it finishes.
	 * This makes playback sequential and prevents overlapping notes.
	 *
	 * @param filePath the path to the audio clip that should be played
	 */
	public void playBlocking(String filePath) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();

			final Object lock = new Object();
			clip.addLineListener(event -> {
				if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
					synchronized (lock) {
						lock.notify();
					}
				}
			});

			clip.open(audioInputStream);
			clip.start();

			// Wait until the clip stops
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			clip.close();

		} catch (Exception e) {
			System.out.println("Error with playing sound (blocking).");
			e.printStackTrace();
		}
	}
}
