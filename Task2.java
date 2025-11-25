public class Task2 {  

    private final int DELAY_MS = 500;
    private final FilePlayer fp = new FilePlayer();

    // Task 1 melody
    private static final String[] SCALE_MELODY = {
        "do", "re", "mi", "fa", "sol", "la", "si", "do-octave"
    };

    // Task 2 melody
    private static final String[] TWINKLE_MELODY = {
        "do","do","sol","sol","la","la","sol",
        "fa","fa","mi","mi","re","re","do",
        "sol","sol","fa","fa","mi","mi","re","sol",
        "sol","fa","fa","mi","mi","re",
        "do","do","sol","sol","la","la","sol",
        "fa","fa","mi","mi","re","re","do"
    };

    class PlayerThread extends Thread {
        private final String[] allowedNotes;
        private final String name;

        PlayerThread(String name, String... allowedNotes) {
            this.name = name;
            this.allowedNotes = allowedNotes;
        }

        @Override
        public void run() {
            for (String note : TWINKLE_MELODY) {
                if (canPlay(note)) {
                    System.out.println(name + " plays " + note);
                    fp.play("sounds/" + note + ".wav");
                    try {
                        Thread.sleep(DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }

        private boolean canPlay(String note) {
            for (String n : allowedNotes)
                if (n.equals(note))
                    return true;
            return false;
        }
    }

    public void playTwinkleSeparate() {
        Thread t1 = new PlayerThread("Thread 1", "do", "mi", "sol", "si", "do-octave");
        Thread t2 = new PlayerThread("Thread 2", "re", "fa", "la", "do-octave");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void playTwinkleSlow() {
        final int gap = 50; 

        for (String note : TWINKLE_MELODY) {
            System.out.println(Thread.currentThread().getName() + " plays " + note);
            fp.playBlocking("sounds/" + note + ".wav");

            try {
                Thread.sleep(gap);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void playScaleSlow() {
        final int gap = 50;

        for (String note : SCALE_MELODY) {
            System.out.println(Thread.currentThread().getName() + " plays " + note);
            fp.playBlocking("sounds/" + note + ".wav");

            try {
                Thread.sleep(gap);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("Main");        

        Task2 two = new Task2(); 
        two.playScaleSlow();
        two.playTwinkleSlow();

    }
}
