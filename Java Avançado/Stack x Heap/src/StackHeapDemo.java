public class StackHeapDemo {

    static int repeticao = 10_000;

    static class SmallObject {
        int x;
        int y;
        SmallObject(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class LargeObject {
        byte[] data;
        LargeObject() {
            this.data = new byte[100_000]; // 100 KB
        }
    }

    public static void main(String[] args) {

        long t0 = System.nanoTime();
        for(int i = 0; i < repeticao; i++){
            new SmallObject(i, i + 1);
        }
        long t1 = System.nanoTime();

        double ms = (t1 - t0) / 1_000_000.0;

        long t2 = System.nanoTime();
        for(int i = 0; i < repeticao; i++){
            new LargeObject();
        }
        long t3 = System.nanoTime();

        double ms2 = (t3 - t2) / 1_000_000.0;

        System.out.println("Tempo criando objetos pequenos: " + ms + " ms");
        System.out.println("Tempo criando objetos grandes: " + ms2 + " ms");
    }
}
