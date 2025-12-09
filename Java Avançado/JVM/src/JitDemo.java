public class JitDemo {

    private static final int CALLS_PER_BATCH = 10_000;
    private static final int BATCHES = 20;
    private static final int WORK_PER_CALL = 200;

    public static void main(String[] args) throws Exception {
        System.out.println("JIT demo iniciando...");
        System.out.printf("CALLS_PER_BATCH=%d, BATCHES=%d, WORK_PER_CALL=%d%n",
                CALLS_PER_BATCH, BATCHES, WORK_PER_CALL);

        if (args.length == 0) {
            System.out.println("Dica: rode com flags JVM para ver PrintCompilation/Inlining.");
            System.out.println("Aguardando 2s antes do warmup...");
            Thread.sleep(2000);
        }

        warmup(3);

        System.out.println("Iniciando medições por batch (cada batch faz muitas chamadas ao método quente)...");
        for (int batch = 1; batch <= BATCHES; batch++) {
            long t0 = System.nanoTime();
            double acc = 0.0;
            for (int i = 0; i < CALLS_PER_BATCH; i++) {
                acc += hotMethod(i, WORK_PER_CALL);
            }
            long t1 = System.nanoTime();
            double ms = (t1 - t0) / 1_000_000.0;
            System.out.printf("Batch %2d: tempo = %7.3f ms, accChecksum=%.5f%n", batch, ms, acc);
            Thread.sleep(50);
        }

        System.out.println("Executando teste de alocação curta (short-lived objects)...");
        allocateShortLived();
        System.out.println("Fim do demo.");
    }

    private static void warmup(int batches) {
        System.out.println("Warmup (" + batches + " batches)...");
        for (int b = 0; b < batches; b++) {
            double acc = 0.0;
            for (int i = 0; i < CALLS_PER_BATCH; i++) {
                acc += hotMethod(i, WORK_PER_CALL);
            }
            System.out.printf(" warmup batch %d done (acc=%.5f)%n", b + 1, acc);
        }
    }

    private static double hotMethod(int seed, int work) {
        double x = seed + 0.123456;
        double s = 0.0;
        for (int i = 0; i < work; i++) {
            s += Math.sqrt(x + i) * 0.000001;
            x = x * 0.999999 + 0.000001;
        }
        return s;
    }

    private static void allocateShortLived() {
        final int OUTER = 200_000;
        final int INNER = 5;
        for (int i = 0; i < OUTER; i++) {
            SmallObject[] arr = new SmallObject[INNER];
            for (int j = 0; j < INNER; j++) {
                arr[j] = new SmallObject(i, j);
            }
            if (i % 50_000 == 0) {
                System.out.println(" allocate progress: " + i);
            }
        }
        System.out.println("allocateShortLived done.");
    }

    private static final class SmallObject {
        final int a;
        final int b;
        final byte[] padding = new byte[64];
        SmallObject(int a, int b) { this.a = a; this.b = b; }
    }
}
